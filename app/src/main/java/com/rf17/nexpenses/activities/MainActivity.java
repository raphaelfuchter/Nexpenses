package com.rf17.nexpenses.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.adapters.LancamentoAdapter;
import com.rf17.nexpenses.dao.LancamentoDao;
import com.rf17.nexpenses.model.Data_filtro;
import com.rf17.nexpenses.model.Lancamento;
import com.rf17.nexpenses.services.LancamentoService;
import com.rf17.nexpenses.utils.AppPreferences;
import com.rf17.nexpenses.utils.UtilsApp;
import com.rf17.nexpenses.utils.UtilsUI;
import com.mikepenz.materialdrawer.Drawer;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LancamentoDao lancamentoDao = new LancamentoDao(this);

    private Context context;
    public static RecyclerView recyclerView;
    public static TextView saldo_txt;
    private Drawer drawer;
    //private static LinearLayout noResults; TODO Criar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            this.context = this;

            recyclerView = (RecyclerView) findViewById(R.id.appList);
            saldo_txt = (TextView) findViewById(R.id.saldo);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);//Define a ActionBar
            if (getSupportActionBar() != null) { getSupportActionBar().setTitle(R.string.app_name); }//Define o nome
            UtilsApp.setAppColor(getWindow(), toolbar);//Define a cor do app
            drawer = setNavigationDrawer((Activity) context, context, toolbar);//Define NavigationDrawer

            //recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);

            // Spinner Período
            lancamentoDao.open();
            List<Data_filtro> list_periodo = lancamentoDao.ListMonths();
            lancamentoDao.close();
            final Spinner spinner = (Spinner) findViewById(R.id.spinner_data);
            ArrayAdapter<Data_filtro> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner, list_periodo);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

            // Listener Período
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Data_filtro data = (Data_filtro) spinner.getSelectedItem();
                    filtrar(data.getDate());//Filtra pelo periodo/mês selecionado
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) { }
            });

            // Botão nova despesa
            findViewById(R.id.fab_despesa).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MainActivity.this, LancamentoActivity.class);
                        intent.putExtra("tipo", "D");//Despesa
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // Botão nova receita
            findViewById(R.id.fab_receita).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MainActivity.this, LancamentoActivity.class);
                        intent.putExtra("tipo", "R");//Receita
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtrar(Date data) {
        try {
            lancamentoDao.open();
            List<Lancamento> lancamentos = lancamentoDao.ListAll(data);
            lancamentoDao.close();

            LancamentoAdapter lancamentoAdapter = new LancamentoAdapter(lancamentos, context);
            recyclerView.setAdapter(lancamentoAdapter);

            LancamentoService.calculaDefineSaldo(lancamentos);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            finish();
            System.exit(0);
        }
    }

    public Drawer setNavigationDrawer (Activity activity, final Context context, Toolbar toolbar) {
        int header;
        AppPreferences appPreferences = NexpensesApplication.getAppPreferences();

        if (UtilsUI.getDayOrNight() == 1) {
            header = R.drawable.header_day;
        } else {
            header = R.drawable.header_night;
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(header)
                .build();

        return new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .withStatusBarColor(UtilsUI.darker(appPreferences.getPrimaryColorPref(), 0.8))
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_lancamentos)).withIcon(FontAwesome.Icon.faw_money),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_apoie_desenvolvimento)).withIcon(FontAwesome.Icon.faw_heart).withCheckable(false),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_settings)).withIcon(FontAwesome.Icon.faw_cog).withCheckable(false),
                        new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_about)).withIcon(FontAwesome.Icon.faw_info_circle).withCheckable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        switch (position) {
                            case 2://Apoie o Desenvolvimento
                                UtilsApp.showToast(((Activity) context), "Em breve");
                                break;
                            case 4://Configurações
                                Intent intent_settings = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(intent_settings);
                                finish();
                                break;
                            case 5://Ajuda e Feedback
                                Intent intent_about = new Intent(MainActivity.this, AboutActivity.class);
                                startActivity(intent_about);
                                finish();
                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                }).build();

    }

}
