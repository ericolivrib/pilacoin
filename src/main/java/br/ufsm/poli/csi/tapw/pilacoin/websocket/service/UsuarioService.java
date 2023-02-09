package br.ufsm.poli.csi.tapw.pilacoin.websocket.service;

import br.ufsm.poli.csi.tapw.pilacoin.model.Usuario;
import br.ufsm.poli.csi.tapw.pilacoin.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UsuarioService {

    private static final String SERVER_URL = "http://srv-ceesp.proj.ufsm.br:8097";
    private final RestTemplate template;
    private final HttpHeaders headers;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioService() {
        template = new RestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public void salvarUsuarios() {
        String url = SERVER_URL + "/usuarios/all";

        try {
            HttpEntity<List<Usuario>> request = new HttpEntity<>(headers);
            ResponseEntity<Usuario> response = template.getForEntity(url, Usuario.class, request);
        } catch (HttpClientErrorException e) {
            System.out.println("Deu ruim!");
        }
    }

}
