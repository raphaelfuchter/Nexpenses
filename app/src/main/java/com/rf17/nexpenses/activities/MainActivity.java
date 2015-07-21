package com.rf17.nexpenses.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.adapters.LancamentoAdapter;
import com.rf17.nexpenses.dao.LancamentoDao;
import com.rf17.nexpenses.model.Data_filtro;
import com.rf17.nexpenses.model.Lancamento;
import com.rf17.nexpenses.utils.AppPreferences;
import com.rf17.nexpenses.utils.StringUtils;
import com.rf17.nexpenses.utils.UtilsApp;
import com.rf17.nexpenses.utils.UtilsUI;
import com.mikepenz.materialdrawer.Drawer;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.yalantis.phoenix.PullToRefreshView;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private LancamentoDao lancamentoDao = new LancamentoDao(this);

    private LancamentoAdapter lancamentoAdapter;

    private Boolean doubleBackToExitPressedOnce = false;
    private Toolbar toolbar;
    private Context context;
    private RecyclerView recyclerView;
    //private PullToRefreshView pullToRefreshView;
    //private ProgressWheel progressWheel;
    private Drawer drawer;
    private MenuItem searchItem;
    private SearchView searchView;
    //private static LinearLayout noResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            AppPreferences appPreferences = NexpensesApplication.getAppPreferences();
            this.context = this;

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.app_name);
            }

            UtilsApp.setAppColor(getWindow(), toolbar, getResources());

            recyclerView = (RecyclerView) findViewById(R.id.appList);
            //pullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
            //fastScroller = (VerticalRecyclerViewFastScroller) findViewById(R.id.fast_scroller);
           // progressWheel = (ProgressWheel) findViewById(R.id.progress);
            //noResults = (LinearLayout) findViewById(R.id.noResults);

            //fastScroller.setRecyclerView(recyclerView);
            //recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
            //pullToRefreshView.setEnabled(false);


            //recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);

            drawer = UtilsUI.setNavigationDrawer((Activity) context, context, toolbar);

           // progressWheel.setBarColor(appPreferences.getPrimaryColorPref());
           // progressWheel.setVisibility(View.VISIBLE);

            // ## Spinner Periodo ##
            lancamentoDao.open();
            List<Data_filtro> list_periodo = lancamentoDao.ListMonths();
            lancamentoDao.close();
            final Spinner spinner = (Spinner) findViewById(R.id.spinner_data);
            ArrayAdapter<Data_filtro> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner, list_periodo);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

            //Listener periodo
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Data_filtro data = (Data_filtro) spinner.getSelectedItem();
                    filtrar(data.getDate());//Filtra pelo periodo/mes selecionado
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) { }
            });

            //Botao nova despesa
            findViewById(R.id.fab_despesa).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, LancamentoActivity.class);
                        intent.putExtra("tipo", "D");//Despesa
                        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //Botao nova receita
            findViewById(R.id.fab_receita).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, LancamentoActivity.class);
                        intent.putExtra("tipo", "R");//Receita
                        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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
            //progressWheel.setVisibility(View.VISIBLE);
            //progressWheel.setProgress(100L);

            lancamentoDao.open();
            List<Lancamento> lancamentos = lancamentoDao.ListAll(data);
            lancamentoDao.close();

            lancamentoAdapter = new LancamentoAdapter(lancamentos, context);
            recyclerView.setAdapter(lancamentoAdapter);

            double saldo = 0.0;
            for(Lancamento lancamento : lancamentos){
                if(lancamento.getTipo().equals("R")) {//Receita
                    saldo += lancamento.getValor();
                }else{//Despesa
                    saldo -= lancamento.getValor();
                }
            }
            ((TextView) findViewById(R.id.saldo)).setText(StringUtils.getPrecoFormatado(saldo));

            //pullToRefreshView.setEnabled(true);
            //progressWheel.setVisibility(View.GONE);

            searchItem.setVisible(true);

           // setPullToRefreshView(pullToRefreshView);
            drawer = UtilsUI.setNavigationDrawer((Activity) context, context, toolbar);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setPullToRefreshView(final PullToRefreshView pullToRefreshView) {
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lancamentoAdapter.clear();
                recyclerView.setAdapter(null);
                filtrar(new Date());

                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    @Override
    public boolean onQueryTextChange(String search) {
        if (search.isEmpty()) {
            ((LancamentoAdapter) recyclerView.getAdapter()).getFilter().filter("");
        } else {
            ((LancamentoAdapter) recyclerView.getAdapter()).getFilter().filter(search);
        }

        return false;
    }

    public static void setResultsMessage(Boolean result) {
        if (result) {
           // noResults.setVisibility(View.VISIBLE);
            //fastScroller.setVisibility(View.GONE);
        } else {
           // noResults.setVisibility(View.GONE);
           // fastScroller.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else if (searchItem.isVisible() && !searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.tap_exit, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

}
