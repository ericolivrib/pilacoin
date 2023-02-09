package br.ufsm.poli.csi.tapw.pilacoin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pila {

    private Long id;
    private Date dataCriacao;
    private byte[] chaveCriador;
    private byte[] assinaturaMaster;
    private String nonce;
    private String status;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public byte[] getChaveCriador() {
        return chaveCriador;
    }

    public void setChaveCriador(byte[] chaveCriador) {
        this.chaveCriador = chaveCriador;
    }

    public byte[] getAssinaturaMaster() {
        return assinaturaMaster;
    }

    public void setAssinaturaMaster(byte[] assinaturaMaster) {
        this.assinaturaMaster = assinaturaMaster;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
