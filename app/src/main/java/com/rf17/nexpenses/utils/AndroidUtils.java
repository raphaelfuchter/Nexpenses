package com.rf17.nexpenses.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.rf17.nexpenses.R;

/**
 * Classe que contém metódos que são iguais em todas as telas, para evitar duplicidade de código, utilizar os métodos daqui.
 * 
 * @author Raphael
 *
 */
public class AndroidUtils {

	/**
	 * Esconde o teclado do dispositivo
	 * 
	 * @param activity 
	 * 			- Activity/tela que será utilizado para esconder o teclado
	 * @param view
	 * 			- ...
	 */
	public static void hideKeyboard(Activity activity, View view) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}
	
	/**
	 * Mostra Toast/Mensagem de erro na tela
	 * 
	 * @param activity 
	 * 			- Activity/tela que será utilizado o Toast
	 * @param e 
	 * 			- Exception com o erro/mensagem/aviso
	 */
	public static void showToast(Activity activity, Exception e){
		Toast.makeText(activity, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show();
	}	
	
	/**
     * Troca a cor da status bar (barra superior onde aparece o relógio, WIFI...)
     * Troca a cor da navigation bar (botões inferiores do android (voltar, home..))
     *
     * @param window
     *          - Tela que esta aberta ( getWindow() )
     * @param resources
     *          - Recursos ( getResources() )
     */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static void setBarColor(Window window, Resources resources){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//Apenas se for LOLLIPOP
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//Define que vai ter cor as bar's
			window.setStatusBarColor(resources.getColor(R.color.primary));//Define status bar (barra do relógio, WIFI...)
			window.setNavigationBarColor(resources.getColor(R.color.primary));//Define navigation color (botão voltar, home...)
		}
	}

	public static String getVersionName(){
		return "";
		//return getPackageManager().getPackageInfo(getPackageName(), 0).versionName
	}

}
