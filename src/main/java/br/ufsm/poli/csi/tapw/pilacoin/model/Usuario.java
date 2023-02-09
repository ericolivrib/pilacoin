package br.ufsm.poli.csi.tapw.pilacoin.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
public class Usuario {

    private Long id;
    private String nome;
    private byte[] chavePublica;
    private String email;
    private String senha;
    private String autoridade;

    public Usuario(Object[][] objeto) {
        this.id = (Long) objeto[0][0];
        this.nome = (String) objeto[0][1];
        this.email = (String) objeto[0][2];
        this.senha = (String) objeto[0][3];
        this.chavePublica = (byte[]) objeto[0][4];
        this.autoridade = (String) objeto[0][5];
    }

    public Usuario() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getChavePublica() {
        return chavePublica;
    }

    public void setChavePublica(byte[] chavePublica) {
        this.chavePublica = chavePublica;
    }

    @Column(unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getAutoridade() {
        return autoridade;
    }

    public void setAutoridade(String autoridades) {
        this.autoridade = autoridades;
    }

    public enum Autoridade {ADMIN}

}
