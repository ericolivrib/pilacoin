package br.ufsm.poli.csi.tapw.pilacoin.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PilaBloco {

    private byte[] assinatura;
    private byte[] chavePublica;
    private byte[] hashPilaBloco;
    private String nonce;
    private String tipo;

    public byte[] getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(byte[] assinatura) {
        this.assinatura = assinatura;
    }

    public byte[] getChavePublica() {
        return chavePublica;
    }

    public void setChavePublica(byte[] chavePublica) {
        this.chavePublica = chavePublica;
    }

    public byte[] getHashPilaBloco() {
        return hashPilaBloco;
    }

    public void setHashPilaBloco(byte[] hashPilaBloco) {
        this.hashPilaBloco = hashPilaBloco;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
