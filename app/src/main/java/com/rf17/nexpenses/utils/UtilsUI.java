package com.rf17.nexpenses.utils;

import android.graphics.Color;

import java.util.Calendar;

public class UtilsUI {

    public static int darker (int color, double factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green( color );
        int b = Color.blue(color);

        return Color.argb(a, Math.max((int) (r * factor), 0), Math.max((int) (g * factor), 0), Math.max((int) (b * factor), 0));
    }

    public static int getDayOrNight() {
        int actualHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (actualHour >= 6 && actualHour < 18) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Troca a cor da status bar (barra superior onde aparece o relógio, WIFI...)
     * Troca a cor da navigation bar (botões inferiores do android (voltar, home..))
     *
     * @param window
     *          - Tela que esta aberta ( getWindow() )
     * @param resources
     *          - Recursos ( getResources() )

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setBarColor(Window window, Resources resources){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//Apenas se for LOLLIPOP
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//Define que vai ter cor as bar's
            window.setStatusBarColor(resources.getColor(R.color.primary));//Define status bar (barra do relógio, WIFI...)
            window.setNavigationBarColor(resources.getColor(R.color.primary));//Define navigation color (botão voltar, home...)
        }
    }*/

}
