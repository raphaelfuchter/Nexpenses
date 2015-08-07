package com.rf17.nexpenses.dao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rf17.nexpenses.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "nexpenses_db";
    Context contextActivity = null;

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        contextActivity = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //LANÇAMENTO
        String lancamento = " CREATE TABLE lancamento( " +
                " id_lancamento INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " tipo TEXT NOT NULL, " +
                " valor NUMERIC NOT NULL, " +
                " data TEXT NOT NULL, " +
                " descricao TEXT); ";
        db.execSQL(lancamento);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Copia um arquivo de banco de dados (caminho da variavel db_import) para o banco de dados da aplicacao, ou seja: faz uma restauracao de outros dados
     *
     * ATENÇÃO: Usar com cuidado, pois irá sobrescrever todos os dados locais!
     *
     */
    public boolean importDatabase(String db_local, String db_import) throws IOException {
        close();// Fecha o a conexao com o bd
        File newDb = new File(db_import);//Arquivo que ira ser importado
        File oldDb = new File(db_local);//Arquivo do bd local
        if (newDb.exists()) {//Se achar arquivo para ser restaurado
            FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));//Faz a transferencia
            getWritableDatabase().close();// Acessa a db criada, para o DataBaseHandler armazenar e marcar como criada.
            return true;//Retorna para dizer que foi importado com sucesso
        }
        return false;//Retorna para dizer que deu erro
    }

}
