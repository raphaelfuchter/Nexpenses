package com.rf17.nexpenses.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rf17.nexpenses.activities.LancamentoActivity;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.activities.MainActivity;
import com.rf17.nexpenses.model.Lancamento;
import com.rf17.nexpenses.utils.StringUtils;
import com.rf17.nexpenses.utils.UtilsApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LancamentoAdapter extends RecyclerView.Adapter<LancamentoAdapter.AppViewHolder> implements Filterable {

    private List<Lancamento> lancamentos;
    private List<Lancamento> lancamentosSearch;
    private Context context;

    public LancamentoAdapter(List<Lancamento> lancamentos, Context context) {
        this.lancamentos = lancamentos;
        this.context = context;
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Lancamento lancamento = lancamentos.get(i);
        appViewHolder.valor.setText(StringUtils.getPrecoFormatado(lancamento.getValor()));//Valor do lancamento
        appViewHolder.descricao.setText(lancamento.getDescricao());//Descricao do lancamento
        appViewHolder.data.setText(sdf.format(lancamento.getData()));//Data do lancamento

        setButtonEvents(appViewHolder, lancamento);

    }

    private void setButtonEvents(AppViewHolder appViewHolder, final Lancamento lancamento) {
        CardView cardView = appViewHolder.vCard;

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LancamentoActivity.class);
                intent.putExtra("id", lancamento.getId_lancamento());//
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                ((Activity) context).finish();
            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*
                new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.excluir_title)
                        .content(R.string.excluir_content)
                        .positiveText(R.string.excluir)
                        .negativeText(R.string.nao)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                try {
                                    lancamentoDao.open();
                                    lancamentoDao.delete(lancamentoDao.getById(row.getId()));
                                    lancamentoDao.close();

                                    //Data data = (Data) spinner_periodo.getSelectedItem();
                                    //filtrar(data.getDate());//Atualiza lista novamente

                                    //Excluido com sucesso!

                                } catch (Exception e) {
                                    UtilsApp.showToast(MainActivity.this, e.getMessage());
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                            }
                        })
                        .show();
                */
                return true;
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

                //lancamentos = (ArrayList<Lancamento>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View appAdapterView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lancamento_row, viewGroup, false);
        return new AppViewHolder(appAdapterView);
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        protected TextView valor;
        protected TextView descricao;
        protected TextView data;
        protected CardView vCard;

        public AppViewHolder(View v) {
            super(v);
            valor = (TextView) v.findViewById(R.id.txtValor);
            descricao = (TextView) v.findViewById(R.id.txtDescricao);
            data = (TextView) v.findViewById(R.id.txt_data);
            vCard = (CardView) v.findViewById(R.id.app_card);
        }
    }

}
