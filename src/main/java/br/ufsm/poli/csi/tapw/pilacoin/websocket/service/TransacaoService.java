package br.ufsm.poli.csi.tapw.pilacoin.websocket.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.Transacao;
import br.ufsm.poli.csi.tapw.pilacoin.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransacaoService {

    private static final Logger logger = LoggerFactory.getLogger(TransacaoService.class);
    private static final String SERVER_URL = "http://srv-ceesp.proj.ufsm.br:8097";
    @Autowired
    private TransacaoRepository transacaoRepository;
    private final RestTemplate template;
    private final HttpHeaders headers;

    public TransacaoService() {
        template = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Transactional
    public void transferir(Transacao transacao) {
        // TODO
    }

}
