package com.example.jeiro.organizapp.BD;

import android.provider.BaseColumns;

/**
 * Created by rcrodriguez on 7/9/2017.
 */

public final class tablas
{
    private tablas() {}
    /* Tablas de la base de datos */
    public static class tabla_usuario implements BaseColumns
    {
        public static final String TABLE_NAME           = "Usuario";        //
        public static final String COLUMN_NAME_USUARIO  = "Usuario";        // 1
        public static final String COLUMN_NAME_PASSWORD = "Password";       // 2
        public static final String COLUMN_NAME_NOMBRE   = "Nombre";         // 3

    }

    public static class tabla_album implements BaseColumns
    {
        public static final String TABLE_NAME          = "Album";      //
        public static final String COLUMN_NAME_PADRE   = "Padre";      // 1
        public static final String COLUMN_NAME_NOMBRE  = "Nombre";     // 2
        public static final String COLUMN_NAME_USUARIO = "Usuario";    // 3
    }

    public static class tabla_contenido implements BaseColumns
    {
        public static final String TABLE_NAME          = "Contenido";    //
        public static final String COLUMN_NAME_PADRE   = "Padre";        // 1
        public static final String COLUMN_NAME_TIPO    = "Tipo";         // 2
        public static final String COLUMN_NAME_NOMBRE  = "Nombre";       // 3
        public static final String COLUMN_NAME_USUARIO = "Usuario";      // 4
    }
}
