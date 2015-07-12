package com.rf17.nexpenses.model;

import java.util.Date;

public class Lancamento {

    private Integer id_lancamento;
    private String tipo; // R-Receita / C-Despesa
    //private Categoria categoria;
    private double valor;
    private Date data;
    private Date data_atual;
    private String descricao;

    public Lancamento() {
        super();
        setData(new Date());
        setData_atual(new Date());
    }

    public Integer getId_lancamento() {
        return id_lancamento;
    }

    public void setId_lancamento(Integer id_lancamento) {
        this.id_lancamento = id_lancamento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getData_atual() {
        return data_atual;
    }

    public void setData_atual(Date data_atual) {
        this.data_atual = data_atual;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
