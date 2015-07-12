package com.rf17.nexpenses.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.adapters.AppAdapter;
import com.rf17.nexpenses.dao.LancamentoDao;
import com.rf17.nexpenses.model.Lancamento;
import com.rf17.nexpenses.utils.AppPreferences;
import com.rf17.nexpenses.utils.UtilsApp;
import com.rf17.nexpenses.utils.UtilsUI;
import com.mikepenz.materialdrawer.Drawer;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.Date;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    // Load Settings
    private AppPreferences appPreferences;

    // General variables
    private List<Lancamento> lancamentos;

    private List<String> appListName;
    private List<String> appListAPK;
    private List<String> appListVersion;
    private List<String> appListSource;
    private List<String> appListData;
    private List<Drawable> appListIcon;

    private AppAdapter appAdapter;

    // Configuration variables
    private Boolean doubleBackToExitPressedOnce = false;
    private Toolbar toolbar;
    private Context context;
    private RecyclerView recyclerView;
    private PullToRefreshView pullToRefreshView;
    private ProgressWheel progressWheel;
    private Drawer drawer;
    private MenuItem searchItem;
    private SearchView searchView;
    private static VerticalRecyclerViewFastScroller fastScroller;
    private static LinearLayout noResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.appPreferences = NexpensesApplication.getAppPreferences();
        this.context = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        UtilsApp.setAppColor(getWindow(), toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.appList);
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        fastScroller = (VerticalRecyclerViewFastScroller) findViewById(R.id.fast_scroller);
        progressWheel = (ProgressWheel) findViewById(R.id.progress);
        noResults = (LinearLayout) findViewById(R.id.noResults);

        fastScroller.setRecyclerView(recyclerView);
        recyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
        pullToRefreshView.setEnabled(false);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        drawer = UtilsUI.setNavigationDrawer((Activity) context, context, toolbar, appAdapter, recyclerView);

        progressWheel.setBarColor(appPreferences.getPrimaryColorPref());
        progressWheel.setVisibility(View.VISIBLE);
        new getLancamentos().execute();

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

    }

    class getLancamentos extends AsyncTask<Void, String, Void> {
        private Integer totalApps;
        private Integer actualApps;

        public getLancamentos() {
            try {
                actualApps = 0;

                LancamentoDao lancamentoDao = new LancamentoDao(context);
                lancamentoDao.open();
                lancamentos = lancamentoDao.ListAll(new Date());
                lancamentoDao.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            super.onProgressUpdate(progress);
            progressWheel.setProgress(Float.parseFloat(progress[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                super.onPostExecute(aVoid);



                //lancamentos = createList(appListName, appListAPK, appListVersion, appListSource, appListData, appListIcon, false);
                appAdapter = new AppAdapter(lancamentos, context);
                //appSystemList = createList(appSystemListName, appSystemListAPK, appSystemListVersion, appSystemListSource, appSystemListData, appSystemListIcon, true);
                //appSystemAdapter = new AppAdapter(appSystemList, context);
                //appFavoriteAdapter = new AppAdapter(getFavoriteList(appList, appSystemList), context);

                //fastScroller.setVisibility(View.GONE);
                recyclerView.setAdapter(appAdapter);
                pullToRefreshView.setEnabled(true);
                progressWheel.setVisibility(View.GONE);
                //searchItem.setVisible(true);

                setPullToRefreshView(pullToRefreshView);
                drawer = UtilsUI.setNavigationDrawer((Activity) context, context, toolbar, appAdapter, recyclerView);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void setPullToRefreshView(final PullToRefreshView pullToRefreshView) {
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                appAdapter.clear();
                recyclerView.setAdapter(null);
                new getLancamentos().execute();

                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }

    /*
    private List<Lancamento> createList(List<String> apps, List<String> apks, List<String> versions, List<String> sources, List<String> data, List<Drawable> icons, Boolean isSystem) {
        List<Lancamento> res = new ArrayList<>();
        for (int i = 0; i < apps.size(); i++) {
            Lancamento lancamento = new Lancamento();
            lancamento.setId_lancamento(1);
            res.add(lancamento);
        }

        return res;
    }
    */

    @Override
    public boolean onQueryTextChange(String search) {
        if (search.isEmpty()) {
            ((AppAdapter) recyclerView.getAdapter()).getFilter().filter("");
        } else {
            ((AppAdapter) recyclerView.getAdapter()).getFilter().filter(search);
        }

        return false;
    }

    public static void setResultsMessage(Boolean result) {
        if (result) {
            noResults.setVisibility(View.VISIBLE);
            fastScroller.setVisibility(View.GONE);
        } else {
            noResults.setVisibility(View.GONE);
            fastScroller.setVisibility(View.VISIBLE);
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
