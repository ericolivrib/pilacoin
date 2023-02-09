package br.ufsm.poli.csi.tapw.pilacoin.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Log {

    private Long id;
    private String mensagem;
    private Date data;

    public Log(String mensagem, Date data) {
        this.mensagem = mensagem;
        this.data = data;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

}
