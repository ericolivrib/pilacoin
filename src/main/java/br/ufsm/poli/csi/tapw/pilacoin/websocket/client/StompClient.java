package br.ufsm.poli.csi.tapw.pilacoin.websocket.client;

import br.ufsm.poli.csi.tapw.pilacoin.dto.Dificuldade;
import br.ufsm.poli.csi.tapw.pilacoin.model.Bloco;
import br.ufsm.poli.csi.tapw.pilacoin.model.Pila;
import br.ufsm.poli.csi.tapw.pilacoin.websocket.service.BlocoService;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Service
public class StompClient {

    private static final String WS_URL = "ws://srv-ceesp.proj.ufsm.br:8097/websocket/websocket";
    private final StompSessionHandlerImpl handler = new StompSessionHandlerImpl();
    private final Collection<Object> storedPayloads = new ArrayList<>();
    @Autowired
    private BlocoService blocoService;

    private void connectToWS() {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        var messageConverter = new MappingJackson2MessageConverter();
        stomp.setMessageConverter(messageConverter);
        stomp.connectAsync(WS_URL, handler);
    }

    public BigInteger getDificuldade() {
        handler.endPoint = "/topic/dificuldade";
        handler.payloadClass = Dificuldade.class;

        connectToWS();

        while (handler.dificuldade == null) {
            Thread.onSpinWait();
        }

        return handler.dificuldade;
    }

    public Pila getPilaToValidate() {
        handler.endPoint = "/topic/validaMineracao";
        handler.payloadClass = Pila.class;

        connectToWS();

        while (handler.pila == null) {
            Thread.onSpinWait();
        }

        return handler.pila;
    }

    public Bloco getBlocoToMining() {
        handler.endPoint = "/topic/descobrirNovoBloco";
        handler.payloadClass = Bloco.class;

        connectToWS();

        while (handler.bloco == null || handler.bloco.getNumeroBloco() == null) {
            Thread.onSpinWait();
        }

        Bloco bloco = blocoService.getBlocoByNumero(handler.bloco.getNumeroBloco());

        return bloco;
    }

    public Bloco getBlocoToValidate() {
        handler.endPoint = "/topic/validaBloco";
        handler.payloadClass = Bloco.class;

        connectToWS();

        while (handler.bloco == null || handler.bloco.getNumeroBloco() == null) {
            Thread.onSpinWait();
        }

        Bloco bloco = blocoService.getBlocoByNumero(handler.bloco.getNumeroBloco());

        return bloco;
    }

    private static class StompSessionHandlerImpl extends StompSessionHandlerAdapter {

        private static final Logger logger = LoggerFactory.getLogger(StompSessionHandlerImpl.class);
        private volatile BigInteger dificuldade;
        private volatile Pila pila;
        private volatile Bloco bloco;
        private String endPoint;
        private Class<?> payloadClass;

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe(endPoint, this);
//            logger.info("Nova sessão estabelecida: {}", session.getSessionId());
//            logger.info("Inscrito em {}", endPoint);
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
//            logger.error("Exceção encontrada na sessão {}: {}", session.getSessionId(), exception.getMessage());
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            // TODO
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            if (Objects.equals(headers.getDestination(), endPoint)) {
                return payloadClass;
            }
//            logger.error("EndPoint inválido: {}", endPoint);
            return null;
        }

        @Override
        public void handleFrame(StompHeaders headers, @Nullable Object payload) {
            if (payload instanceof Pila) {
                this.pila = (Pila) payload;
//                logger.info("Payload de {} encontrado", Pila.class.getSimpleName());
            } else if (payload instanceof Bloco) {
                this.bloco = (Bloco) payload;
//                logger.info("Payload de {} encontrado", Bloco.class.getSimpleName());
            } else if (payload instanceof Dificuldade dificuldade) {
                this.dificuldade = new BigInteger(dificuldade.getDificuldade(), 16);
//                logger.info("Payload de {} encontrado", Dificuldade.class.getSimpleName());
            }
        }

    }

}
