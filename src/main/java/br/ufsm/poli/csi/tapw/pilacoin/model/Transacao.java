package br.ufsm.poli.csi.tapw.pilacoin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transacao {

    private Long id;
    private byte[] assinatura;
    private byte[] chaveUsuarioDestino;
    private byte[] chaveUsuarioOrigem;
    private Date dataTransacao;
    private String noncePila;
    private String status;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(byte[] assinatura) {
        this.assinatura = assinatura;
    }

    public byte[] getChaveUsuarioDestino() {
        return chaveUsuarioDestino;
    }

    public void setChaveUsuarioDestino(byte[] chaveUsuarioDestino) {
        this.chaveUsuarioDestino = chaveUsuarioDestino;
    }

    public byte[] getChaveUsuarioOrigem() {
        return chaveUsuarioOrigem;
    }

    public void setChaveUsuarioOrigem(byte[] chaveUsuarioOrigem) {
        this.chaveUsuarioOrigem = chaveUsuarioOrigem;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(Date dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public String getNoncePila() {
        return noncePila;
    }

    public void setNoncePila(String noncePila) {
        this.noncePila = noncePila;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
