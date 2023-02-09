package br.ufsm.poli.csi.tapw.pilacoin.service;

import br.ufsm.poli.csi.tapw.pilacoin.dto.PilaBloco;
import br.ufsm.poli.csi.tapw.pilacoin.model.Bloco;
import br.ufsm.poli.csi.tapw.pilacoin.model.Log;
import br.ufsm.poli.csi.tapw.pilacoin.model.Pila;
import br.ufsm.poli.csi.tapw.pilacoin.model.Usuario;
import br.ufsm.poli.csi.tapw.pilacoin.repository.LogRepository;
import br.ufsm.poli.csi.tapw.pilacoin.websocket.client.StompClient;
import br.ufsm.poli.csi.tapw.pilacoin.websocket.service.BlocoService;
import br.ufsm.poli.csi.tapw.pilacoin.websocket.service.PilaService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class PilacoinService {

    private static final Logger logger = LoggerFactory.getLogger(PilacoinService.class);
    private static final Thread[] threads = new Thread[5];
    private List<String> noncesColetados;
    @Autowired
    private StompClient stompClient;
    @Autowired
    private PilaService pilaService;
    @Autowired
    private BlocoService blocoService;
    @Autowired
    private LogRepository logRepository;
    private static final boolean[] running = new boolean[5];

    public void iniciar(int numThread, Usuario usuario) {
        this.noncesColetados = new ArrayList<>();
        running[numThread] = true;
        Runnable runnable = null;

        switch (numThread) {
            case 0 -> runnable = new MineradorPila(usuario);
            case 1 -> runnable = new ValidadorPila(usuario);
            case 2 -> runnable = new MineradorBloco(usuario);
            case 3 -> runnable = new ValidadorBloco(usuario);
            case 4 -> runnable = new TransacaoPila(usuario);
        }

        if (threads[numThread] == null) {
            threads[numThread] = new Thread(runnable);
            threads[numThread].start();
        } else {
            synchronized (this) {
                notify();
            }
        }
    }

    public List<String> parar(int numThread) {
        running[numThread] = false;
        return noncesColetados;
    }

    private BigInteger getNumHash(Object obj) {
        BigInteger numHash;

        try {
            var messageDigest = MessageDigest.getInstance("SHA-256");
            var mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            var objString = mapper.writeValueAsString(obj);
            byte[] objHash = messageDigest.digest(objString.getBytes(StandardCharsets.UTF_8));
            numHash = new BigInteger(objHash).abs();
        } catch (JsonProcessingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return numHash;
    }

    /**
     * Cria uma assinatura para a validação de um bloco ou pila
     *
     * @param numHash      número baseado no hash do objeto
     * @param chavePublica chave pública do usuário validador
     * @return assinatura codificada em bytes
     */
    private static byte[] getAssinatura(BigInteger numHash, byte[] chavePublica) {
        byte[] assinatura;

        try {
            var keyFactory = KeyFactory.getInstance("RSA");
            var encodedKeySpec = new X509EncodedKeySpec(chavePublica);
            var publicKey = keyFactory.generatePublic(encodedKeySpec);

            var rsa = Cipher.getInstance("RSA");
            rsa.init(Cipher.ENCRYPT_MODE, publicKey);

            assinatura = rsa.doFinal(numHash.toByteArray());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException
                 | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return assinatura;
    }

    private class MineradorPila implements Runnable {

        private final Usuario usuario;

        public MineradorPila(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public void run() {
            Pila pila;
            BigInteger numHash;
            BigInteger dificuldade = stompClient.getDificuldade();

            new Log();

            logger.info("{} começou a minerar de pilacoins", usuario.getNome());

            while (threads[0].isAlive()) {
                synchronized (this) {
                    if (!running[0]) {
                        try {
                            logger.info("{} finalizou a mineração de pilacoins", usuario.getNome());
                            wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }

                do {
                    var rnd = new SecureRandom();
                    var magicNumber = new BigInteger(128, rnd).abs();

                    pila = new Pila();
                    pila.setDataCriacao(new Date());
                    pila.setNonce(magicNumber.toString());
                    pila.setChaveCriador(usuario.getChavePublica());

                    numHash = getNumHash(pila);
                } while (numHash.compareTo(dificuldade) > 0);

                logger.info("{} minerou um pilacoin: [{}]", usuario.getNome(), pila.getNonce());
                pilaService.salvar(pila);
                noncesColetados.add(pila.getNonce());
            }
        }

    }

    private class ValidadorPila implements Runnable {

        private final Usuario usuario;

        public ValidadorPila(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public void run() {
            BigInteger dificuldade = stompClient.getDificuldade();
            List<String> noncesInvalidos = new ArrayList<>();

            logger.info("{} começou a validar pilacoins", usuario.getNome());

            while (threads[1].isAlive()) {
                synchronized (this) {
                    if (!running[1]) {
                        try {
                            logger.info("{} finalizou a validação de pilacoins", usuario.getNome());
                            wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }

                var pila = stompClient.getPilaToValidate();

                if (!noncesInvalidos.contains(pila.getNonce())) {
                    var numHash = getNumHash(pila);

                    if (numHash.compareTo(dificuldade) <= 0) {
                        logger.info("{} validou um pilacoin: [{}]", usuario.getNome(), pila.getNonce());

                        byte[] assinatura = getAssinatura(numHash, usuario.getChavePublica());

                        PilaBloco pilaValido = new PilaBloco();
                        pilaValido.setAssinatura(assinatura);
                        pilaValido.setChavePublica(usuario.getChavePublica());
                        pilaValido.setNonce(pila.getNonce());
                        pilaValido.setTipo("PILA");
                        pilaValido.setHashPilaBloco(null);

                        pilaService.validar(pilaValido);
                        noncesColetados.add(pilaValido.getNonce());
                    } else {
                        noncesInvalidos.add(pila.getNonce());
                        logger.warn("{} invalidou um pilacoin: [{}]", usuario.getNome(), pila.getNonce());
                    }
                }
            }
        }

    }

    private class MineradorBloco implements Runnable {

        private final Usuario usuario;

        public MineradorBloco(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public void run() {
            Bloco bloco;
            BigInteger numHash;
            BigInteger dificuldade = stompClient.getDificuldade();

            logger.info("{} começou a minerar blocos", usuario.getNome());

            while (threads[2].isAlive()) {
                synchronized (this) {
                    if (!running[2]) {
                        try {
                            logger.info("{} finalizou a mineração de blocos", usuario.getNome());
                            wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }

                do {
                    var rnd = new SecureRandom();
                    var magicNumber = new BigInteger(128, rnd).abs();

                    bloco = stompClient.getBlocoToMining();
                    bloco.setNonce(magicNumber.toString());
                    bloco.setChaveUsuarioMinerador(usuario.getChavePublica());

                    numHash = getNumHash(bloco);
                } while (numHash.compareTo(dificuldade) > 0);

                logger.info("{} minerou um bloco: [{}]", usuario.getNome(), bloco.getNonce());
                blocoService.salvar(bloco);
                noncesColetados.add(bloco.getNonce());
            }
        }

    }

    private class ValidadorBloco implements Runnable {

        private final Usuario usuario;

        public ValidadorBloco(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public void run() {
            BigInteger numHash;
            BigInteger dificuldade = stompClient.getDificuldade();

            logger.info("{} começou a validar blocos", usuario.getNome());

            while (threads[3].isAlive()) {
                synchronized (this) {
                    if (!running[3]) {
                        try {
                            logger.info("{} finalizou a validação de blocos", usuario.getNome());
                            wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }

                Bloco bloco = stompClient.getBlocoToValidate();
                numHash = getNumHash(bloco);

                if (numHash.compareTo(dificuldade) <= 0) {
                    logger.info("{} validou um bloco: [{}]", usuario.getNome(), bloco.getNonce());

                    byte[] assinatura = getAssinatura(numHash, usuario.getChavePublica());

                    PilaBloco blocoValido = new PilaBloco();
                    blocoValido.setAssinatura(assinatura);
                    blocoValido.setChavePublica(usuario.getChavePublica());
                    blocoValido.setNonce(bloco.getNonce());
                    blocoValido.setTipo("BLOCO");
                    blocoValido.setHashPilaBloco(null);

                    blocoService.validar(blocoValido);
                    noncesColetados.add(blocoValido.getNonce());
                } else {
                    logger.warn("{} invalidou um bloco: [{}]", usuario.getNome(), bloco.getNonce());
                }
            }
        }

    }

    private class TransacaoPila implements Runnable {

        private final Usuario usuario;

        public TransacaoPila(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public void run() {
            while (threads[4].isAlive()) {
                synchronized (this) {
                    if (!running[4]) {
                        try {
                            logger.info("Transferência de pila finalizada");
                            wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }

                // TODO
            }
        }

    }

}
