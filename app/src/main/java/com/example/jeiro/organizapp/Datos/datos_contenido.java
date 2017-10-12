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

public class datos_contenido
{
    public datos_contenido() {}

    public boolean insertar_contenido(Contenido contenido, boolean insertar, Context context)
    {
        base_de_datos helper = new base_de_datos(context);
        try
        {
            ContentValues values = new ContentValues();
            values.put(tablas.tabla_contenido.COLUMN_NAME_PADRE,  contenido.getPadre());
            values.put(tablas.tabla_contenido.COLUMN_NAME_TIPO,   contenido.getTipo());
            values.put(tablas.tabla_contenido.COLUMN_NAME_NOMBRE, contenido.getNombre());
            values.put(tablas.tabla_contenido.COLUMN_NAME_USUARIO, contenido.getUsuario());
            if (insertar) // insertar
            {
                SQLiteDatabase db = helper.getWritableDatabase();
                db.insert(tablas.tabla_contenido.TABLE_NAME, null, values);
                db.close();
            }
            else // modificar
            {
                SQLiteDatabase db = helper.getWritableDatabase();
                db.update(tablas.tabla_contenido.TABLE_NAME, values, "_id=" + contenido.getId(), null);
                db.close();
            }

        }
        catch (Exception exc)
        {
            return false;
        }
        return true;
    }

    public ArrayList<Contenido> obtener_contenidos (Context context)
    {
        ArrayList<Contenido> datos = new ArrayList();
        base_de_datos helper = new base_de_datos(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {
            Cursor c = db.query
                    (
                            tablas.tabla_contenido.TABLE_NAME, // The table to query
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
                    Contenido a = new Contenido(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
                    datos.add(a);
                } while (c.moveToNext());
        }
        catch (Exception exc)
        {
            db.close();
            return new ArrayList<Contenido>();
        }
        db.close();
        return datos;
    }

    public ArrayList<Contenido> obtener_contenido_por_album (Context context, Album album)
    {
        ArrayList<Contenido> datos = obtener_contenidos(context);
        ArrayList<Contenido> resultado = new ArrayList<>();
        for(int i = 0; i < datos.size(); i++)
        {
            Contenido temp = datos.get(i);
            if(temp.getPadre().equals(album.getNombre()) && temp.getUsuario().equals(album.getUsuario()))
                resultado.add(temp);
        }
        return resultado;
    }

    public boolean eliminar_contenido (Contenido contenido, Context context)
    {
        base_de_datos helper = new base_de_datos(context);
        try
        {
            SQLiteDatabase db = helper.getWritableDatabase();
            String whereClause = "_id=?";
            String[] whereArgs = new String[] { String.valueOf(contenido.getId()) };
            db.delete(tablas.tabla_contenido.TABLE_NAME, whereClause, whereArgs);
        }
        catch (Exception exc)
        {
            return false;
        }
        return true;
    }
}
