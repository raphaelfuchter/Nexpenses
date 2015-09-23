package com.rf17.nexpenses.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class StringUtils {

    private static DecimalFormat formato = new DecimalFormat("#,##0.00");

    /**
     Verifica se o valor nao esta nulo e formata ele para double trocando virgula por ponto
     *
     * @return
     *      - Numero Formatado para double ou 0.0
     */
    public static double formataVerificaValor(String valor) throws Exception {
        try{
            //return !valor.isEmpty() ? formato.parse(valor).doubleValue() : 0.0;
            return !valor.toString().equals("") ? Double.parseDouble(valor.toString().replace(",", ".")) : 0.0;
        }catch(Exception e){
            throw new Exception("Erro ao formatar e/ou verificar valor digitado!");
        }
    }

    /**
     * Utilizado para formatar double para reais
     *
     * @param valor numero (Ex.: '12.95')
     * @return String formatada (Ex.: 'R$ 12,95')
     */
    public static String getPrecoFormatado(double valor) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        return numberFormat.format(valor);
    }

    /**
     * Formata Double para String com virgulas e 2 casas decimais
     *
     * @param casasDecimais
     *      - Número de casas decimais maximas e minimas depois da virgula)
     * @param valor
     *      - Número (Ex.: '1212.95')
     *
     * @return String
     *      - String formatada (Ex.: '1.212,95' (se param casasDecimais igual a 2)
     */
    public static String formataDouble(double valor, int casasDecimais) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(casasDecimais);
        numberFormat.setMinimumFractionDigits(casasDecimais);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);//Arrendodamento
        return numberFormat.format(valor);
    }

}
