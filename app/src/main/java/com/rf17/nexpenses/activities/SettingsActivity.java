package com.rf17.nexpenses.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.dao.db.DataBaseHandler;
import com.rf17.nexpenses.utils.AppPreferences;
import com.rf17.nexpenses.utils.UtilsApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class SettingsActivity extends PreferenceActivity {
    // Load Settings
    private AppPreferences appPreferences;
    private Toolbar toolbar;

    // Settings variables
    //private AmbilWarnaPreference prefPrimaryColor;
    //private Preference prefNavigationColor;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        this.context = this;
        this.appPreferences = NexpensesApplication.getAppPreferences();

        UtilsApp.setAppColor(getWindow(), toolbar);

        toolbar.setTitle(getResources().getString(R.string.action_settings));

        // Configurações
        findPreference("prefPasswordBoolean").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue instanceof Boolean) {
                    Boolean value = (Boolean) newValue;
                    appPreferences.setPasswordBooleanPref(value);
                    if(value) {
                        openDialogSenha();
                    }
                }
                return true;
            }
        });

        // Temas
        //prefPrimaryColor = (AmbilWarnaPreference) findPreference("prefPrimaryColor");
        //AmbilWarnaPreference prefAccentColor = (AmbilWarnaPreference) findPreference("prefAccentColor");
        //prefNavigationColor = findPreference("prefNavigationColor");
        //Preference prefDefaultValues = findPreference("prefDefaultValues");

        // Exportar Dados
        findPreference("prefExport").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    if (Environment.getExternalStorageDirectory().canWrite()) {// Verifica se existe permissao de escrita/write
                        final File currentDB = getDatabasePath("nexpenses_db");
                        File backupDB = new File(Environment.getExternalStorageDirectory(), "/download/nexpenses_database_copy.db");

                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();

                        Toast.makeText(getApplicationContext(), "Backup do banco de dados criado com sucesso! O arquivo foi salvo dentro da pasta 'download' do seu aparelho, com o nome de 'nexpenses_database_copy.db'", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Não existe permissão para escrita/leitura de arquivos no dispositivo!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Erro ao realizar backup do banco de dados! (Motivo: " + e.getMessage() + ")", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

        // Importar Dados
        findPreference("prefImport").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                dialogConfirmaImport();
                return true;
            }
        });

        // Sobre
        Preference prefVersion = findPreference("prefVersion");
        prefVersion.setTitle(getResources().getString(R.string.app_name) + " v" + UtilsApp.getAppVersionName(context) + " (" + UtilsApp.getAppVersionCode(context) + ")");
        prefVersion.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
                return true;
            }
        });

    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_settings, new LinearLayout(this), false);
        toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);////TODO Toolbar should load the default style in XML (white title and back arrow), but doesn't happen
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ViewGroup contentWrapper = (ViewGroup) contentView.findViewById(R.id.content_wrapper);
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);
        getWindow().setContentView(contentView);

    }

    private void openDialogSenha(){
        new MaterialDialog.Builder(this)
                .title(R.string.settings_password)
                .inputMaxLengthRes(4, R.color.material_blue_grey_800)
                .inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        appPreferences.setPassword(input.toString());
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        context.startActivity(new Intent(context, MainActivity.class));
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
        finish();
    }

    /**
     * Abre dialog para confirmar import do bd
     */
    public void dialogConfirmaImport() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Você tem certeza que deseja importar/restaurar o banco de dados? (Todas as informações salvas localmente, serão perdidas) ");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {// Se clicar em SIM, exclui o pedido
                dialog.dismiss();// Fecha dialog

                try {
                    DataBaseHandler dbHelper = new DataBaseHandler(context);
                    SQLiteDatabase database = dbHelper.getWritableDatabase();//Open

                    String db_local = database.getPath();
                    String db_import = Environment.getExternalStorageDirectory().getPath() + "/download/sd.db";

                    if (dbHelper.importDatabase(db_local, db_import)) {
                        Toast.makeText(SettingsActivity.this, "Importação/Restauração do banco de dados realizado com sucesso!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SettingsActivity.this, "Arquivo para ser restaurado não encontrado! O arquivo deve estar localizado dentro da pasta 'download' do seu aparelho, com o nome de 'nx.db'", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SettingsActivity.this, "Erro ao realizar importação/restauração do banco de dados! (Motivo: " + e.getMessage() + ")", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {// Se clicar em NAO, nao faz nada
                dialog.dismiss();// Fecha dialog
            }
        });

        AlertDialog alert = builder.create();
        alert.show();// Mostra dialog
    }

}
