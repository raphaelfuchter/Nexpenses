package com.rf17.nexpenses.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.rf17.nexpenses.NexpensesApplication;
import com.rf17.nexpenses.activities.AppActivity;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.activities.MainActivity;
import com.rf17.nexpenses.model.Lancamento;
import com.rf17.nexpenses.utils.AppPreferences;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> implements Filterable {
    // Load Settings
    private AppPreferences appPreferences;

    // AppAdapter variables
    private List<Lancamento> lancamentos;
    private List<Lancamento> lancamentosSearch;
    private Context context;

    public AppAdapter(List<Lancamento> lancamentos, Context context) {
        this.lancamentos = lancamentos;
        this.context = context;
        this.appPreferences = NexpensesApplication.getAppPreferences();
    }

    @Override
    public int getItemCount() {
        return lancamentos.size();
    }

    public void clear() {
        lancamentos.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
        Lancamento lancamento = lancamentos.get(i);
        appViewHolder.valor.setText(lancamento.getValor().toString());
        appViewHolder.descricao.setText(lancamento.getDescricao());
        appViewHolder.data.setText(lancamento.getData().toString());
        //appViewHolder.vIcon.setImageDrawable(appInfo.getIcon()); FIXME


        setButtonEvents(appViewHolder, lancamento);

    }

    private void setButtonEvents(AppViewHolder appViewHolder, final Lancamento lancamento) {
        CardView cardView = appViewHolder.vCard;

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;

                Intent intent = new Intent(context, AppActivity.class);
                //intent.putExtra("id", lancamento.getId_lancamento());
                //intent.putExtra("app_apk", appInfo.getAPK());
                //intent.putExtra("app_version", appInfo.getVersion());
                //intent.putExtra("app_source", appInfo.getSource());
                //intent.putExtra("app_data", appInfo.getData());
                //Bitmap bitmap = ((BitmapDrawable) appInfo.getIcon()).getBitmap();
                //intent.putExtra("app_icon", bitmap);
                //intent.putExtra("app_isSystem", appInfo.isSystem());

                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.fade_back);
            }
        });

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<Lancamento> results = new ArrayList<>();
                if (lancamentosSearch == null) {
                    lancamentosSearch = lancamentos;
                }
                if (charSequence != null) {
                    if (lancamentosSearch != null && lancamentosSearch.size() > 0) {
                        for (final Lancamento lancamento : lancamentosSearch) {
                            if (lancamento.getDescricao().toLowerCase().contains(charSequence.toString())) {
                                results.add(lancamento);
                            }
                        }
                    }
                    oReturn.values = results;
                    oReturn.count = results.size();
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.count > 0) {
                    MainActivity.setResultsMessage(false);
                } else {
                    MainActivity.setResultsMessage(true);
                }
                lancamentos = (ArrayList<Lancamento>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View appAdapterView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_layout, viewGroup, false);
        return new AppViewHolder(appAdapterView);
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        protected TextView valor;
        protected TextView descricao;
        protected TextView data;
        protected ImageView vIcon;
        protected CardView vCard;

        public AppViewHolder(View v) {
            super(v);
            valor = (TextView) v.findViewById(R.id.txtValor);
            descricao = (TextView) v.findViewById(R.id.txtDescricao);
            data = (TextView) v.findViewById(R.id.txt_data);
            vIcon = (ImageView) v.findViewById(R.id.imgIcon);
            vCard = (CardView) v.findViewById(R.id.app_card);

        }
    }

}
