package br.ufsm.poli.csi.tapw.pilacoin.websocket.service;

import br.ufsm.poli.csi.tapw.pilacoin.dto.PilaBloco;
import br.ufsm.poli.csi.tapw.pilacoin.model.Pila;
import br.ufsm.poli.csi.tapw.pilacoin.repository.PilaRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class PilaService {

    private static final Logger logger = LoggerFactory.getLogger(PilaService.class);
    private static final String SERVER_URL = "http://srv-ceesp.proj.ufsm.br:8097";
    @Autowired
    private PilaRepository pilaRepository;
    private final RestTemplate template;
    private final HttpHeaders headers;


    public PilaService(PilaRepository pilaRepository) {
        template = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Transactional
    public void salvar(Pila pila) {
        var url = SERVER_URL + "/pilacoin/";
        var request = new HttpEntity<>(pila, headers);

        try {
            var response = template.postForEntity(url, request, Pila.class);
            Pila pilaResponse = Objects.requireNonNull(response.getBody());
            logger.info("Pila enviado para validação: {}", pilaResponse.getNonce());
            pilaRepository.save(pilaResponse);
            logger.info("Pila salvo na base de dados");
        } catch (HttpClientErrorException e) {
            logger.error(e.getMessage());
        }
    }

    public void validar(PilaBloco pila) {
        var url = SERVER_URL + "/validaPilaOutroUsuario";
        var request = new HttpEntity<>(pila, headers);

        try {
            var response = template.postForEntity(url, request, String.class);
            logger.info("Pila validado enviado para o servidor: {}", response);
        } catch (HttpClientErrorException e) {
            logger.error("Falhas ao enviar pila para validação. {}", e.getMessage());
        }
    }

//    @PostConstruct
//    public void buscarPilas() {
//        var url = SERVER_URL + "/pilacoin/all?chaveCriador=" + chave;
//    }



}
