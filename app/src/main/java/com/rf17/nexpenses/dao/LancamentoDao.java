package com.rf17.nexpenses.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rf17.nexpenses.dao.db.DataBaseHandler;
import com.rf17.nexpenses.model.Data_filtro;
import com.rf17.nexpenses.model.Lancamento;
import com.rf17.nexpenses.utils.DataUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LancamentoDao {

    private SQLiteDatabase database;
    private DataBaseHandler dbHelper;

    public LancamentoDao(Context context) {
        dbHelper = new DataBaseHandler(context);
    }

    public void open() throws SQLException { database = dbHelper.getWritableDatabase(); }
    public void close() { dbHelper.close(); }

    @SuppressLint("SimpleDateFormat")
    public List<Lancamento> ListAll(Date date) throws ParseException {

        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");

        List<Lancamento> lancamentos = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        Date firstDate = cal.getTime();

        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE)); // changed calendar to cal
        Date lastDate = cal.getTime();

        String data1 = sdf.format(firstDate);
        String data2 = sdf.format(lastDate);

        String selectQuery = "SELECT * FROM lancamento WHERE data >= '"+data1+"' AND data <= '"+data2+"' ORDER BY data desc, id_lancamento desc ;" ;
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Lancamento lancamento = cursorToEntity(cursor);
            lancamentos.add(lancamento);
            cursor.moveToNext();
        }
        cursor.close();
        return lancamentos;
    }

    public Lancamento getById(long id_lancamento) throws Exception {
        Lancamento lancamento = null;
        String selectQuery = "SELECT * FROM lancamento WHERE id_lancamento=" + id_lancamento;
        Cursor cursor = database.rawQuery(selectQuery, null);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                lancamento = cursorToEntity(cursor);
            }
        }catch (Exception e){
            throw new Exception("Erro ao buscar lançamento pelo identificador! ("+e.getMessage()+")");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return lancamento;
    }

    public long saveOrUpdate(Lancamento lancamento) {
        lancamento.setData_atual(new Date());
        if(lancamento.getId_lancamento() == null) {
            return database.insert("lancamento", null, lancamentoToContentValues(lancamento));
        }else{
            return database.update("lancamento", lancamentoToContentValues(lancamento),"id_lancamento="+lancamento.getId_lancamento(), null);
        }
    }

    public void delete(Lancamento lancamento) {
        database.delete("lancamento", "id_lancamento="+lancamento.getId_lancamento(), null);
    }

    private Lancamento cursorToEntity(Cursor cursor) throws ParseException {
        Lancamento lancamento = new Lancamento();
        lancamento.setId_lancamento(cursor.getInt(0));
        lancamento.setTipo(cursor.getString(1));
        lancamento.setValor(cursor.getDouble(2));
        lancamento.setData(cursor.getString(3) == null ? null : DataUtils.getDateBd(cursor.getString(3)));
        lancamento.setDescricao(cursor.getString(4));
        return lancamento;
    }

    private ContentValues lancamentoToContentValues(Lancamento lancamento) {
        ContentValues values = new ContentValues();
        values.put("id_lancamento", lancamento.getId_lancamento());
        values.put("tipo", lancamento.getTipo());
        values.put("valor", lancamento.getValor());
        if(lancamento.getData() != null){values.put("data", DataUtils.setDateBd(lancamento.getData()));}
        values.put("descricao", lancamento.getDescricao());
        return values;
    }

    public List<Data_filtro> ListMonths() throws Exception {
        try {
            String selectQuery = " select"
                    + " strftime('%m', data) as int_month, "
                    + " case strftime('%m', data) "
                    + " when '01' then 'Janeiro' "
                    + " when '02' then 'Fevereiro' "
                    + " when '03' then 'Março' "
                    + " when '04' then 'Abril' "
                    + " when '05' then 'Maio' "
                    + " when '06' then 'Junho' "
                    + " when '07' then 'Julho' "
                    + " when '08' then 'Agosto' "
                    + " when '09' then 'Setembro' "
                    + " when '10' then 'Outubro' "
                    + " when '11' then 'Novembro' "
                    + " when '12' then 'Dezembro' "
                    + " else '' end as month_name, "
                    + " strftime('%Y', data) as year "
                    + " from lancamento GROUP BY int_month, year ORDER BY data desc; ";
            Cursor cursor = database.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            List<Data_filtro> meses = new ArrayList<>();

            while (!cursor.isAfterLast()) {

                int mes = Integer.parseInt(cursor.getString(0));
                String descricao_mes = cursor.getString(1);
                int ano = cursor.getInt(2);

                Calendar calendar = Calendar.getInstance();
                calendar.set(ano, mes-1, 1);

                Data_filtro data = new Data_filtro();
                data.setDescricao(descricao_mes+" / "+ano);
                data.setDate(calendar.getTime());

                meses.add(data);
                cursor.moveToNext();
            }
            cursor.close();

            return meses;

        } catch (Exception e) {
            throw new Exception("Erro ao listar meses!");
        }
    }

}
