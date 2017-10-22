package com.example.jeiro.organizapp.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.jeiro.organizapp.BD.*;
import com.example.jeiro.organizapp.MainActivity;
import com.example.jeiro.organizapp.Modelo.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by rcrodriguez on 8/10/2017.
 */

public class datos_album
{
    public datos_album() {}

    /**
     * Agrega a base de datos todos los albumes
     * @param album
     * @param insertar
     * @param context
     * @return
     */
    public boolean insertar_album(Album album, boolean insertar, Context context)
    {
        base_de_datos helper = new base_de_datos(context);
        try
        {
            ContentValues values = new ContentValues();
            values.put(tablas.tabla_album.COLUMN_NAME_PADRE,   album.getPadre());
            values.put(tablas.tabla_album.COLUMN_NAME_NOMBRE,  album.getNombre());
            values.put(tablas.tabla_album.COLUMN_NAME_USUARIO, album.getUsuario());
            if (insertar) // insertar
            {
                SQLiteDatabase db = helper.getWritableDatabase();
                db.insert(tablas.tabla_album.TABLE_NAME, null, values);
                db.close();
            }
            else // modificar
            {
                SQLiteDatabase db = helper.getWritableDatabase();
                db.update(tablas.tabla_album.TABLE_NAME, values, "_id=" + album.getId(), null);
                db.close();
            }

        }
        catch (Exception exc)
        {
            return false;
        }
        return true;
    }

    /**
     * Busca todos los albumes creados en el sistema
     * @param context
     * @return
     */
    public ArrayList<Album> obtener_albums (Context context)
    {
        ArrayList datos = new ArrayList<Album>();
        base_de_datos helper = new base_de_datos(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        try
        {
            Cursor c = db.query
                    (
                            tablas.tabla_album.TABLE_NAME, // The table to query
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
                    Album a = new Album(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
                    datos.add(a);
                } while (c.moveToNext());
        }
        catch (Exception exc)
        {
            db.close();
            return new ArrayList<Album>();
        }
        db.close();
        return datos;
    }

    /**
     * busca un album especifico en el sistema
     * @param context
     * @param album
     * @return
     */
    public Album obtener_album (Context context, String album)
    {
        ArrayList<Album> datos = obtener_albums(context);
        if(album.equals(""))
            return new Album("","", MainActivity.usuario_activo.getUsuario());
        for (int i = 0; i < datos.size();i++)
        {
            Album temp = datos.get(i);
            if(temp.getNombre().toLowerCase().equals(album.toLowerCase()))
                return temp;
        }
        return null;
    }

    /**
     * Busca todos los albums que se hayen dentro otro album
     * @param context
     * @param album
     * @return
     */
    public ArrayList<Album> obtener_albums_por_album(Context context, Album album)
    {
        ArrayList<Album> datos = obtener_albums(context);
        ArrayList<Album> resultado = new ArrayList<>();
        for (int i = 0; i < datos.size();i++)
        {
            Album temp = datos.get(i);
            if(temp.getPadre().equals(album.getNombre()) && temp.getUsuario().equals(album.getUsuario()))
                resultado.add(temp);
        }
        return resultado;
    }

    /**
     * Busca la ruta en la que se encuetra hubicado el album
     * @param context
     * @param album
     * @return
     */
    public String obtener_album_path (Context context, Album album) {
        ArrayList<Album> datos = obtener_albums(context);
        /*
        for (int i = 0; i < datos.size();i++)
        {
            Album temp = datos.get(i);
            if(temp.getNombre().equals(album.getNombre()))
                return temp;
        }
        //*/
        String path = (album.getPadre().equals("")) ? "" : File.separator + album.getPadre();
        if (datos.size() != 0)
        {
            int i = 0;
            while (!album.getPadre().equals("") && datos.size() > i) {

                if (datos.get(i).getNombre().equals(album.getPadre())) {
                    album = datos.get(i);
                    path = (datos.get(i).getPadre().equals(""))?path:File.separator + datos.get(i).getPadre() + path;

                    i = 0;
                }
                else
                    i++;
            }
        }
        return path;
    }

    /**
     * Elimina un album del sistema
     * @param album
     * @param context
     * @return
     */
    public boolean eliminar_album (Album album, Context context)
    {
        ArrayList<Album> datos = obtener_albums_por_album(context,album);
        datos_contenido d_contenido = new datos_contenido();
        for(int i = 0; i < datos.size();i++)
        {
            ArrayList<Contenido> temp = d_contenido.obtener_contenido_por_album(context,datos.get(i));
            for(int  j = 0; j < temp.size();j++)
            {
                d_contenido.eliminar_contenido(temp.get(j),context);
            }
            eliminar_album(datos.get(i),context);
        }
        ArrayList<Contenido> temp = d_contenido.obtener_contenido_por_album(context,album);
        for(int  j = 0; j < temp.size();j++)
        {
            d_contenido.eliminar_contenido(temp.get(j),context);
        }
        base_de_datos helper = new base_de_datos(context);
        try
        {
            SQLiteDatabase db = helper.getWritableDatabase();
            String whereClause = "_id=?";
            String[] whereArgs = new String[] { String.valueOf(album.getId()) };
            db.delete(tablas.tabla_album.TABLE_NAME, whereClause, whereArgs);
        }
        catch (Exception exc)
        {
            return false;
        }
        return true;
    }

    /**
     * cambia el nombre de un album en el sistema
     * @param nuevo
     * @param anterior
     * @param context
     * @return
     */
    public boolean rename_album (Album nuevo, Album anterior, Context context) {
        try
        {
            ArrayList<Album> datos = obtener_albums_por_album(context, anterior);
            datos_contenido d_contenido = new datos_contenido();
            for (int i = 0; i < datos.size(); i++) {
                Album var = datos.get(i);
                var.setPadre(nuevo.getNombre());
                insertar_album(var, false, context);
            }

            ArrayList<Contenido> content = d_contenido.obtener_contenido_por_album(context,anterior);
            for (int j = 0; j < content.size(); j++) {
                Contenido var = content.get(j);
                var.setPadre(nuevo.getNombre());
                d_contenido.insertar_contenido(var, false, context);
            }
            insertar_album(nuevo,false,context);

        }
        catch (Exception e)
        {
            return false;
        }
        return true;

    }
}
