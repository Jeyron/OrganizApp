package com.example.jeiro.organizapp.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.jeiro.organizapp.BD.*;
import com.example.jeiro.organizapp.Modelo.*;

import java.util.ArrayList;

/**
 * Created by rcrodriguez on 8/10/2017.
 */

public class datos_usuario
{
    public datos_usuario() {}

    public boolean insertar_usuario(Usuario usuario, boolean insertar, Context context)
    {
        base_de_datos helper = new base_de_datos(context);
        try
        {
            ContentValues values = new ContentValues();
            values.put(tablas.tabla_usuario.COLUMN_NAME_USUARIO,  usuario.getUsuario());
            values.put(tablas.tabla_usuario.COLUMN_NAME_PASSWORD,   usuario.getPassword());
            values.put(tablas.tabla_usuario.COLUMN_NAME_NOMBRE,   usuario.getNombre());
            if (insertar) // insertar
            {
                SQLiteDatabase db = helper.getWritableDatabase();
                db.insert(tablas.tabla_usuario.TABLE_NAME, null, values);
                db.close();
            }
            else // modificar
            {
                SQLiteDatabase db = helper.getWritableDatabase();
                db.update(tablas.tabla_usuario.TABLE_NAME, values, "_id=" + usuario.getId(), null);
                db.close();
            }

        }
        catch (Exception exc)
        {
            return false;
        }
        return true;
    }

    public ArrayList<Usuario> obtener_usuarios (Context context)
    {
        ArrayList<Usuario> datos = new ArrayList();
        base_de_datos helper = new base_de_datos(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {
            Cursor c = db.query
                    (
                            tablas.tabla_usuario.TABLE_NAME, // The table to query
                            null, // The columns to return
                            null, // The columns for the WHERE clause
                            null, // The values for the WHERE clause
                            null, // don't group the rows
                            null, // don't filter by row groups
                            null // The sort order
                    );
            if (c.moveToFirst())
                do
                {
                    Usuario a = new Usuario(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
                    datos.add(a);
                } while (c.moveToNext());
        }
        catch (Exception exc)
        {
            db.close();
            return new ArrayList<Usuario>();
        }
        db.close();
        return datos;
    }

    public Usuario obtener_usuarios (Context context, Usuario usuario)
    {
        ArrayList<Usuario> datos = new ArrayList();
        base_de_datos helper = new base_de_datos(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {
            Cursor c = db.query
                    (
                            tablas.tabla_usuario.TABLE_NAME, // The table to query
                            null, // The columns to return
                            null, // The columns for the WHERE clause
                            null, // The values for the WHERE clause
                            null, // don't group the rows
                            null, // don't filter by row groups
                            null // The sort order
                    );
            if (c.moveToFirst())
                do
                {
                    Usuario a = new Usuario(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
                    datos.add(a);
                } while (c.moveToNext());
        }
        catch (Exception exc)
        {
            db.close();
            return new Usuario();
        }
        db.close();
        for (int i = 0; i < datos.size();i++)
        {
            Usuario temp = datos.get(i);
            if(temp.getUsuario().equals(usuario.getUsuario()))
                return temp;
        }
        return null;
    }

    public boolean eliminar_usuario (Usuario usuario, Context context)
    {
        base_de_datos helper = new base_de_datos(context);
        try
        {
            SQLiteDatabase db = helper.getWritableDatabase();
            String whereClause = "_id=?";
            String[] whereArgs = new String[] { String.valueOf(usuario.getId()) };
            db.delete(tablas.tabla_usuario.TABLE_NAME, whereClause, whereArgs);
        }
        catch (Exception exc)
        {
            return false;
        }
        return true;
    }
}
