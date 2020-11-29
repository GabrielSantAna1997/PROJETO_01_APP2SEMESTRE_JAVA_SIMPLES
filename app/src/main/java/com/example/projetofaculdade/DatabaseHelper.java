package com.example.projetofaculdade;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import androidx.annotation.Nullable;

import java.nio.DoubleBuffer;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "categoria.db";

    // Tables names
    private static final String TABLE_CATEGORIA = "tb_categoria";
    private static final String TABLE_QUESTAO = "tb_questao";
    private static final String TABLE_OPCAO = "tb_opcao";
    private static final String TABLE_QUESTAO_OPCAO = "questao_opcao";

    // Table CATEGORIA columns names
    private static final String CATEGORIA_ID = "id";
    private static final String CATEGORIA_NOME = "nome";

    // Table QUESTAO columns names
    private static final String QUESTAO_ID = "id";
    private static final String QUESTAO_NOME = "nome";
    private static final String QUESTAO_CATEGORIA_ID = "CATEGORIA_ID";

    // Table OPCAO columns names
    private static final String OPCAO_ID = "id";
    private static final String OPCAO_NOME = "nome";

    // Table QUESTAO_OPCAO columns names
    private static final String QUESTAO_OPCAO_QUESTAO_ID = "questao_id";
    private static final String QUESTAO_OPCAO_OPCAO_ID = "opcao_id";
    private static final String QUESTAO_OPCAO_NUMERO = "numero";
    private static final String QUESTAO_OPCAO_CORRETA = "ic_correta";

    // Create CATEGORIA table statement
    private static final String CREATE_CATEGORIA_TABLE = "CREATE TABLE " +
            TABLE_CATEGORIA + " (" + CATEGORIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CATEGORIA_NOME + " TEXT" + ")";

    // CREATE QUESTAO TABLE statement
    private static final String CREATE_QUESTAO_TABLE = "CREATE TABLE " +
            TABLE_QUESTAO + " ("  + QUESTAO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            QUESTAO_NOME + " TEXT, " +
            QUESTAO_CATEGORIA_ID + " INTEGER, " +
            "FOREIGN KEY(" + QUESTAO_CATEGORIA_ID + ") REFERENCES " + TABLE_CATEGORIA + " (" + CATEGORIA_ID + "))";

    // CREATE OPCAO TABLE statement
    private static final String CREATE_OPCAO_TABLE = "CREATE TABLE " +
            TABLE_OPCAO + " (" + OPCAO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            OPCAO_NOME + " TEXT" + ")";

    // CREATE QUESTAO_OPCAO TABLE statement
    private static final String CREATE_QUESTAO_OPCAO_TABLE = "CREATE TABLE " +
            TABLE_QUESTAO_OPCAO + " (" + QUESTAO_OPCAO_QUESTAO_ID + " INTEGER, " +
            QUESTAO_OPCAO_OPCAO_ID + " INTEGER, " +
            QUESTAO_OPCAO_NUMERO + " INTEGER, " +
            QUESTAO_OPCAO_CORRETA +  " INTEGER, " +
            "FOREIGN KEY (" + QUESTAO_OPCAO_QUESTAO_ID  + ") REFERENCES " +
            TABLE_QUESTAO + " (" + QUESTAO_ID + "), " +
             "FOREIGN KEY (" +  QUESTAO_OPCAO_OPCAO_ID + ") REFERENCES " +
            TABLE_OPCAO + " (" + OPCAO_ID + "))";

    private static final String INSERT_CATEGORIA_DESENVOLVIMENTO = "INSERT INTO " + TABLE_CATEGORIA +
            " (" + CATEGORIA_NOME +  ") " +
            "VALUES (Desenvolvimento)";
    private static final String INSERT_CATEGORIA_REDES = "INSERT INTO " + TABLE_CATEGORIA +
            " (" + CATEGORIA_NOME +  ") " +
            "VALUES (Redes)";
    private static final String INSERT_CATEGORIA_SEGURANCA_INFORMACAO = "INSERT INTO " + TABLE_CATEGORIA +
            " (" + CATEGORIA_NOME +  ") " +
            "VALUES (Seguranca_da_Informacao)";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORIA_TABLE);
        db.execSQL(CREATE_QUESTAO_TABLE);
        db.execSQL(CREATE_OPCAO_TABLE);
        db.execSQL(CREATE_QUESTAO_OPCAO_TABLE);
        db.execSQL(INSERT_CATEGORIA_DESENVOLVIMENTO);
        db.execSQL(INSERT_CATEGORIA_REDES);
        db.execSQL(INSERT_CATEGORIA_SEGURANCA_INFORMACAO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTAO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPCAO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTAO_OPCAO);
        this.onCreate(db) ;
    }

    public List<Categoria> getCategorias() {
        String selectQuery  = "SELECT * FROM " + TABLE_CATEGORIA;
        SQLiteDatabase database  = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        List<Categoria> categorias = new ArrayList<Categoria>();

        if (cursor.moveToFirst()) {
            do {
                int id =    cursor.getInt(cursor.getColumnIndex(CATEGORIA_ID));
                String nome = cursor.getString(cursor.getColumnIndex(CATEGORIA_NOME));
                categorias.add(new Categoria(id, nome));
            } while (cursor.moveToNext());
        }

        return categorias;
    }

    public Categoria getCategoriaById(int id) {
        String selectQuery  = "SELECT * FROM " + TABLE_CATEGORIA +
                " WHERE " + CATEGORIA_ID + " = " + id;
        SQLiteDatabase database  = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        String nome = cursor.getString(cursor.getColumnIndex(CATEGORIA_NOME));
        Categoria categoria = new Categoria(id, nome);


        return categoria;
    }
}
