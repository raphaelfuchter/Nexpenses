package com.rf17.nexpenses.services;

import com.rf17.nexpenses.activities.MainActivity;
import com.rf17.nexpenses.model.Lancamento;
import com.rf17.nexpenses.utils.StringUtils;

import java.util.List;

public class LancamentoService {

    public static void calculaDefineSaldo(List<Lancamento> lancamentos){
        double saldo = 0.0;
        for(Lancamento lancamento : lancamentos){
            if(lancamento.getTipo().equals("R")) {//Receita
                saldo += lancamento.getValor();
            }else{//Despesa
                saldo -= lancamento.getValor();
            }
        }

        MainActivity.saldo_txt.setText(StringUtils.getPrecoFormatado(saldo));
    }

}
