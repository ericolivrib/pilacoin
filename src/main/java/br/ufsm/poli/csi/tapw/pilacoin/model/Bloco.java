package br.ufsm.poli.csi.tapw.pilacoin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.util.List;

@Entity
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bloco {

    private Long numeroBloco;
    private byte[] chaveUsuarioMinerador;
    private String nonce;
    private String nonceBlocoAnterior;
    private List<Transacao> transacoes;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getNumeroBloco() {
        return numeroBloco;
    }

    public void setNumeroBloco(Long numeroBloco) {
        this.numeroBloco = numeroBloco;
    }

    public byte[] getChaveUsuarioMinerador() {
        return chaveUsuarioMinerador;
    }

    public void setChaveUsuarioMinerador(byte[] chaveUsuarioMinerador) {
        this.chaveUsuarioMinerador = chaveUsuarioMinerador;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getNonceBlocoAnterior() {
        return nonceBlocoAnterior;
    }

    public void setNonceBlocoAnterior(String nonceBlocoAnterior) {
        this.nonceBlocoAnterior = nonceBlocoAnterior;
    }

    @OneToMany
    @JoinColumn(name = "id_bloco")
    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }
}
