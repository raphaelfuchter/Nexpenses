package com.rf17.nexpenses.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rf17.nexpenses.activities.LancamentoActivity;
import com.rf17.nexpenses.R;
import com.rf17.nexpenses.activities.MainActivity;
import com.rf17.nexpenses.dao.LancamentoDao;
import com.rf17.nexpenses.model.Lancamento;
import com.rf17.nexpenses.services.LancamentoService;
import com.rf17.nexpenses.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LancamentoAdapter extends RecyclerView.Adapter<LancamentoAdapter.AppViewHolder> {

    private List<Lancamento> lancamentos;
    private Context context;

    public LancamentoAdapter(List<Lancamento> lancamentos, Context context) {
        this.lancamentos = lancamentos;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return lancamentos.size();
    }

    @Override
    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Lancamento lancamento = lancamentos.get(i);

        if(lancamento.getTipo().equals("R")){
            appViewHolder.valor.setTextColor(context.getResources().getColor(R.color.green));
        }else{
            appViewHolder.valor.setTextColor(context.getResources().getColor(R.color.red));
        }

        appViewHolder.valor.setText(StringUtils.getPrecoFormatado(lancamento.getValor()));//Valor do lancamento
        appViewHolder.descricao.setText(lancamento.getDescricao());//Descricao do lancamento
        appViewHolder.data.setText(sdf.format(lancamento.getData()));//Data do lancamento

        setButtonEvents(appViewHolder, lancamento);

    }

    private void setButtonEvents(final AppViewHolder appViewHolder, final Lancamento lancamento) {
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

                new MaterialDialog.Builder(context)
                        .title(R.string.excluir_title)
                        .content(R.string.excluir_content)
                        .positiveText(R.string.excluir)
                        .negativeText(R.string.nao)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                try {
                                    LancamentoDao lancamentoDao = new LancamentoDao(context);
                                    lancamentoDao.open();
                                    lancamentoDao.delete(lancamento);
                                    lancamentoDao.close();

                                    lancamentoDao.open();
                                    lancamentos = lancamentoDao.ListAll(new Date());
                                    lancamentoDao.close();

                                    LancamentoAdapter lancamentoAdapter = new LancamentoAdapter(lancamentos, context);
                                    MainActivity.recyclerView.setAdapter(lancamentoAdapter);

                                    LancamentoService.calculaDefineSaldo(lancamentos);

                                    //Excluido com sucesso! TODO CRIAR SNACKBAR

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //UtilsApp.showToast(MainActivity.this, e.getMessage());
                                }
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) { }
                        })
                        .show();

                return true;
            }
        });
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View appAdapterView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_lancamento, viewGroup, false);
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
