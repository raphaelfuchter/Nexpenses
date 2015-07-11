package com.rf17.nexpenses.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.dao.LancamentoDao;
import com.rf17.nexpenses.model.Lancamento;
import com.rf17.nexpenses.utils.AppPreferences;
import com.rf17.nexpenses.utils.StringUtils;
import com.rf17.nexpenses.utils.UtilsUI;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LancamentoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    // Load Settings
    private AppPreferences appPreferences;

    // General variables
    private Lancamento lancamento;

    private LancamentoDao lancamentoDao = new LancamentoDao(this);
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private ImageView icone;
    private TextView descricao;

    private EditText editText_valor;
    private EditText editText_data;
    private EditText editText_descricao;
    private TextView header;

    private FloatingActionButton fab_salvar;



    // Configuration variables
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamento);
        this.context = this;
        this.appPreferences = NexpensesApplication.getAppPreferences();

        getInitialConfiguration();
        setInitialConfiguration();
        setScreenElements();

    }

    private void setInitialConfiguration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null ) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Voltar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8));
            toolbar.setBackgroundColor(appPreferences.getPrimaryColorPref());
            if (appPreferences.getNavigationColorPref()) {
                getWindow().setNavigationBarColor(appPreferences.getPrimaryColorPref());
            }
        }
    }

    private void setScreenElements() {

        icone = (ImageView) findViewById(R.id.icon);
        descricao = (TextView) findViewById(R.id.icon_description);

        editText_valor = (EditText) findViewById(R.id.editText_valor);
        editText_data = (EditText) findViewById(R.id.editText_data);
        editText_descricao = (EditText) findViewById(R.id.editText_descricao);
        header = (TextView) findViewById(R.id.header);

        fab_salvar = (FloatingActionButton) findViewById(R.id.fab);

        //icone.setImageDrawable(appInfo.getIcon());
        descricao.setText("Despesa");

        // Header
        header.setBackgroundColor(appPreferences.getPrimaryColorPref());

        editText_data.setOnClickListener(new View.OnClickListener() {//Abre datePicker
            @Override
            public void onClick(View v) {
                try {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            LancamentoActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.show(getFragmentManager(), "Datepickerdialog");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // FAB Salvar
        //fab.setIcon(R.drawable.ic_send_white);
        fab_salvar.setColorNormal(appPreferences.getAccentColorPref());
        fab_salvar.setColorPressed(UtilsUI.darker(appPreferences.getAccentColorPref(), 0.8));
        fab_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar(editText_valor.getText().toString(), editText_data.getText().toString(), editText_descricao.getText().toString());

                onBackPressed();
            }
        });

    }


    private void getInitialConfiguration() {
        String id = getIntent().getStringExtra("id");
        if (id != null) {
            //Busca no bd e preeenche as informacoes
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_forward, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //item_favorite = menu.findItem(R.id.action_favorite);
        //UtilsApp.setAppFavorite(context, item_favorite, UtilsApp.isAppFavorite(appInfo.getAPK(), appFavorites));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
            //case R.id.action_favorite:
                //if (UtilsApp.isAppFavorite(appInfo.getAPK(), appFavorites)) {
                //    appFavorites.remove(appInfo.getAPK());
                //    appPreferences.setFavoriteApps(appFavorites);
                //} else {
                //    appFavorites.add(appInfo.getAPK());
                //    appPreferences.setFavoriteApps(appFavorites);
                //}
                //UtilsApp.setAppFavorite(context, item_favorite, UtilsApp.isAppFavorite(appInfo.getAPK(), appFavorites));
             //   return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ## DataPicker ##
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        editText_data.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
    }
    // ## DataPicker ##

    private void salvar(String valor, String data, String descricao){
        try{
            lancamento.setValor(StringUtils.formataVerificaValor(valor));
            lancamento.setData(sdf.parse(data));
            lancamento.setDescricao(descricao);

            if (lancamento.getValor() == 0.0) {
                throw new Exception("Valor deve ser maior que 0,00 (zero)");
            } else {
                lancamentoDao.open();
                lancamentoDao.saveOrUpdate(lancamento);// Atualiza ou salva
                lancamentoDao.close();

                //Salvo com sucesso

                onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
