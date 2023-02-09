package br.ufsm.poli.csi.tapw.pilacoin.websocket.service;

import br.ufsm.poli.csi.tapw.pilacoin.dto.PilaBloco;
import br.ufsm.poli.csi.tapw.pilacoin.model.Bloco;
import br.ufsm.poli.csi.tapw.pilacoin.repository.BlocoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class BlocoService {

    private static final Logger logger = LoggerFactory.getLogger(BlocoService.class);
    private static final String SERVER_URL = "http://srv-ceesp.proj.ufsm.br:8097";
    @Autowired
    private BlocoRepository blocoRepository;
    private final RestTemplate template;
    private final HttpHeaders headers;

    public BlocoService() {
        template = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Transactional
    public void salvar(Bloco bloco) {
        var url = SERVER_URL + "/bloco/";
        var request = new HttpEntity<>(bloco, headers);

        try {
            var response = template.postForEntity(url, request, Bloco.class);
            Bloco blocoResponse = Objects.requireNonNull(response.getBody());
            logger.info("Bloco enviado para validação: {}", blocoResponse.getNonce());
            blocoRepository.save(blocoResponse);
            logger.info("Bloco salvo na base de dados");
        } catch (HttpClientErrorException e) {
            logger.error(e.getMessage());
        }
    }

    public void validar(PilaBloco bloco) {
        var url = SERVER_URL + "/validaBlocoOutroUsuario";
        var request = new HttpEntity<>(bloco, headers);

        try {
            var response = template.postForEntity(url, request, String.class);
            logger.info("Bloco validado enviado para o servidor: {}", response.getBody());
        } catch (HttpClientErrorException e) {
            logger.error("Falhas ao enviar bloco para validação: {}", e.getMessage());
        }
    }

    public Bloco getBlocoByNumero(long numBloco) {
        Bloco bloco = null;
        var url = SERVER_URL + "/bloco/?numBloco=" + numBloco;
        var request = new HttpEntity<>(numBloco, headers);

        try {
            ResponseEntity<Bloco> response = template.getForEntity(url, Bloco.class, request);
            bloco = response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error(e.getMessage());
        }

        return bloco;
    }
}
