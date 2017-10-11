package com.example.jeiro.organizapp.BD;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rcrodriguez on 7/9/2017.
 */

public class base_de_datos extends SQLiteOpenHelper
{
    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMBER_TYPE = " INTEGER";
    private static final String FLOAT_TYPE = " REAL";

    private static final String COMMA_SEP = ",";

    private static final String CREAR_TABLA_USUARIO =
            "CREATE TABLE " +
                    tablas.tabla_usuario.TABLE_NAME +
                    " (" +
                    tablas.tabla_usuario._ID + " INTEGER PRIMARY KEY," +
                    tablas.tabla_usuario.COLUMN_NAME_USUARIO  + TEXT_TYPE + COMMA_SEP +
                    tablas.tabla_usuario.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    tablas.tabla_usuario.COLUMN_NAME_NOMBRE   + TEXT_TYPE +
                    " )";

    private static final String DELETE_TABLA_USUARIO =
            "DROP TABLE IF EXISTS " + tablas.tabla_usuario.TABLE_NAME;

    private static final String CREAR_TABLA_ALBUM =
            "CREATE TABLE " +
                    tablas.tabla_album.TABLE_NAME +
                    " (" +
                    tablas.tabla_album._ID + " INTEGER PRIMARY KEY," +
                    tablas.tabla_album.COLUMN_NAME_PADRE   + TEXT_TYPE + COMMA_SEP +
                    tablas.tabla_album.COLUMN_NAME_NOMBRE  + TEXT_TYPE + COMMA_SEP +
                    tablas.tabla_album.COLUMN_NAME_USUARIO + TEXT_TYPE +
                    " )";

    private static final String DELETE_TABLA_ALBUM =
            "DROP TABLE IF EXISTS " + tablas.tabla_album.TABLE_NAME;

    private static final String CREAR_TABLA_CONTENIDO =
            "CREATE TABLE " +
                    tablas.tabla_contenido.TABLE_NAME +
                    " (" +
                    tablas.tabla_contenido._ID + " INTEGER PRIMARY KEY," +
                    tablas.tabla_contenido.COLUMN_NAME_PADRE + TEXT_TYPE +  COMMA_SEP +
                    tablas.tabla_contenido.COLUMN_NAME_TIPO   + TEXT_TYPE + COMMA_SEP +
                    tablas.tabla_contenido.COLUMN_NAME_NOMBRE + TEXT_TYPE + COMMA_SEP +
                    tablas.tabla_usuario.COLUMN_NAME_USUARIO +
                    " )";

    private static final String DELETE_TABLA_CONTENIDO =
            "DROP TABLE IF EXISTS " + tablas.tabla_contenido.TABLE_NAME;

    private static final String INSERTAR_USUARIO =
            "INSERT INTO " +
                    tablas.tabla_usuario.TABLE_NAME +
                    "(" +
                        tablas.tabla_usuario.COLUMN_NAME_USUARIO  + COMMA_SEP +
                        tablas.tabla_usuario.COLUMN_NAME_PASSWORD + COMMA_SEP +
                        tablas.tabla_usuario.COLUMN_NAME_NOMBRE   +

                    ")" +
                    "VALUES" +
                        "(" +
                            "\"admin\"," +
                            "\"admin\", " +
                            "\"Administrador del sistema\"" +
                        ");";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "OrganizApp.db";

    public base_de_datos(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREAR_TABLA_USUARIO);
        db.execSQL(CREAR_TABLA_ALBUM);
        db.execSQL(CREAR_TABLA_CONTENIDO);
        db.execSQL(INSERTAR_USUARIO);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DELETE_TABLA_USUARIO);
        db.execSQL(DELETE_TABLA_ALBUM);
        db.execSQL(DELETE_TABLA_CONTENIDO);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}