package com.rf17.nexpenses.model;

import java.util.Date;

public class Data_filtro {

    private String descricao;
    private Date date;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    //http://aqueleblogdeandroid.blogspot.com.br/2011/02/utilizando-spinner-com-objetos.html
    public String toString() {
        return (this.getDescricao());
    }

}
