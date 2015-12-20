package br.ufms.vagner.cardapiovirtual.model;

import java.io.Serializable;

public class Lanche implements Serializable {
    private Integer id;
    private String nome;
    private String descricao;
    private Float preco;

    /**
     * Construtor Default
     */
    public Lanche() {

    }

    /**
     * Construtor com Parâmetros
     *
     * @param nome
     * @param descricao
     * @param preco
     */
    public Lanche(String nome, String descricao, Float preco) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }
}