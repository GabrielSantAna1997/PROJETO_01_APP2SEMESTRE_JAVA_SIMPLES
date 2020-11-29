package com.example.projetofaculdade;

import android.content.ContentValues;
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
    private static final int DATABASE_VERSION = 10;
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
            TABLE_QUESTAO + " ("  + QUESTAO_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, " +
            QUESTAO_NOME + " TEXT, " +
            QUESTAO_CATEGORIA_ID + " INTEGER, " +
            "FOREIGN KEY(" + QUESTAO_CATEGORIA_ID + ") REFERENCES " + TABLE_CATEGORIA + " (" + CATEGORIA_ID + "))";

    // CREATE OPCAO TABLE statement
    private static final String CREATE_OPCAO_TABLE = "CREATE TABLE " +
            TABLE_OPCAO + " (" + OPCAO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORIA_TABLE);
        db.execSQL(CREATE_QUESTAO_TABLE);
        db.execSQL(CREATE_OPCAO_TABLE);
        db.execSQL(CREATE_QUESTAO_OPCAO_TABLE);


        long categoriaDesenvolvimentoid = this.insertCategoria("Desenvolvimento", db);
        long categoriaRedesId = this.insertCategoria("Redes", db);
        long categoriaSegurancaId = this.insertCategoria("Seguranca da Informação", db);

        this.criarQuestoes(categoriaDesenvolvimentoid, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTAO_OPCAO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTAO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPCAO);

        this.onCreate(db);
    }

    private void criarQuestoes(long categoriaId, SQLiteDatabase database) {
        List<Questao> questoes = new ArrayList<Questao>();
        List<QuestaoOpcao> opcoes = new ArrayList<QuestaoOpcao>();
        List<QuestaoOpcao> opcoes2 = new ArrayList<QuestaoOpcao>();

        opcoes.add(new QuestaoOpcao(1, 1, 1,"A) uma classe e tem o mesmo nome da classe.", false));
        opcoes.add(new QuestaoOpcao( 1, 1, 2, "B) um objeto e tem o mesmo nome do objeto.", false));
        opcoes.add(new QuestaoOpcao(1, 1, 3, "C) um objeto e tem o mesmo nome da classe a qual pertence.", true));
        opcoes.add(new QuestaoOpcao(1, 1, 4, "D) uma classe e tem o nome diferente do nome da classe.", false));
        questoes.add(new Questao(1, "Construtores Java são métodos especiais chamados pelo sistema no momento da criação de:", opcoes));


        opcoes2.add(new QuestaoOpcao(1, 1, 1, "A) generic.", false));
        opcoes2.add(new QuestaoOpcao(1, 1, 2, "B) void.", false));
        opcoes2.add(new QuestaoOpcao(1, 1, 3, "C) initial.", false));
        opcoes2.add(new QuestaoOpcao(1, 1, 4, "D) abstract.", true));
        questoes.add(new Questao(2, "Na linguagem Java, um método que é apenas declarado como membro de uma classe, mas não provê uma implementação, deve ser declarado como:", opcoes2));

        for (int index = 0; index < questoes.size(); index++) {
            ContentValues questaoValues = new ContentValues();
            Questao questaoAtual = questoes.get(index);

            long questaoId = this.insertQuestao(questaoAtual.nome, categoriaId, database);

            for (int opcaoIndex = 0; opcaoIndex < questaoAtual.opcoes.size(); opcaoIndex++) {
                QuestaoOpcao questaoOpcaoAtual = questaoAtual.opcoes.get(opcaoIndex);

                long opcaoId = this.insertOpcao(questaoOpcaoAtual.nome, database);
                this.insertQuestaoOpcao(questaoOpcaoAtual, questaoId, opcaoId, database);
            }
        }
    }


    private long insertCategoria(String nome, SQLiteDatabase database) {
        ContentValues values = new ContentValues();

        values.put(CATEGORIA_NOME, nome);
        long id = database.insert(TABLE_CATEGORIA, null, values);

        return id;
    }

    private long insertQuestao(String nome, long categoriaId, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(QUESTAO_NOME, nome);
        values.put(QUESTAO_CATEGORIA_ID, categoriaId);
        long id = database.insert(TABLE_QUESTAO, null, values);

        return id;
    }

    private long insertOpcao(String nome, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(OPCAO_NOME, nome);
        long id = database.insert(TABLE_OPCAO, null, values);

        return id;
    }

    private void insertQuestaoOpcao(QuestaoOpcao questaoOpcao, long questaoId, long opcaoId, SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put(QUESTAO_OPCAO_QUESTAO_ID, questaoId);
        values.put(QUESTAO_OPCAO_OPCAO_ID, opcaoId);
        values.put(QUESTAO_OPCAO_CORRETA, questaoOpcao.isCorreta ? 1 : 0);
        values.put(QUESTAO_OPCAO_NUMERO, questaoOpcao.numero);
        database.insert(TABLE_QUESTAO_OPCAO, null, values);
    }

    public List<Categoria> getCategorias() {
        String query  = "SELECT * FROM " + TABLE_CATEGORIA;
        SQLiteDatabase database  = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        List<Categoria> categorias = new ArrayList<Categoria>();

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(CATEGORIA_ID));
                String nome = cursor.getString(cursor.getColumnIndex(CATEGORIA_NOME));
                categorias.add(new Categoria(id, nome));
            } while (cursor.moveToNext());
        }

        return categorias;
    }

    public List<Questao> getQuestoes(long categoriaId) {
        String query = "SELECT * FROM " + TABLE_QUESTAO +
                " WHERE " + QUESTAO_CATEGORIA_ID + " = " + categoriaId;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        List<Questao> questoes = new ArrayList<Questao>();

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(QUESTAO_ID));
                String nome = cursor.getString(cursor.getColumnIndex(QUESTAO_NOME));
                List<QuestaoOpcao> questoesOpcoes = this.getQuestaoOpcoes(id);

                Questao questao = new Questao(id, nome, questoesOpcoes);
                questoes.add(questao);
            } while (cursor.moveToNext());
        }

        return questoes;
    }

    public Opcao getOpcao(long id) {
        String query = "SELECT * FROM " + TABLE_OPCAO +
                " WHERE " + OPCAO_ID + " = " + id;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        String nome = cursor.getString(cursor.getColumnIndex(OPCAO_NOME));
        Opcao opcao = new Opcao(id, nome);

        return opcao;
    }

    public List<QuestaoOpcao> getQuestaoOpcoes(long questaoId) {
        String query = "SELECT * FROM " + TABLE_QUESTAO_OPCAO +
                " WHERE " +  QUESTAO_OPCAO_QUESTAO_ID + " = " + questaoId;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        List<QuestaoOpcao> questaoOpcoes = new ArrayList<QuestaoOpcao>();

        if (cursor.moveToFirst()) {
            do {
                long opcaoId = cursor.getLong(cursor.getColumnIndex(QUESTAO_OPCAO_OPCAO_ID));
                boolean isCorreta = cursor.getInt(cursor.getColumnIndex(QUESTAO_OPCAO_CORRETA)) == 1 ? true : false;
                int numero = cursor.getInt(cursor.getColumnIndex(QUESTAO_OPCAO_NUMERO));
                String nome = this.getOpcao(opcaoId).nome;

                QuestaoOpcao questaoOpcao = new QuestaoOpcao(questaoId, opcaoId, numero, nome, isCorreta);
                questaoOpcoes.add(questaoOpcao);
            } while (cursor.moveToNext());
        }

        return questaoOpcoes;
    }

    public Categoria getCategoriaByNome(String nome) {
        String selectQuery  = "SELECT * FROM " + TABLE_CATEGORIA +
                " WHERE " + CATEGORIA_NOME + " = " + nome;
        SQLiteDatabase database  = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int id = cursor.getInt(cursor.getColumnIndex(CATEGORIA_ID));
        Categoria categoria = new Categoria(id, nome);


        return categoria;
    }
}
