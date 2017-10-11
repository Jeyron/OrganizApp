package com.example.jeiro.organizapp.Modelo;

import java.util.ArrayList;

/**
 * Created by rcrodriguez on 8/10/2017.
 */

public class Album
{
    private int    Id;
    private String  Padre;
    private String Nombre;
    private String Usuario;
    private ArrayList<Album> Albums;
    private ArrayList<Contenido> Contenido;

    public Album() {}

    public Album(String padre, String nombre, String usuario) {
        Padre = padre;
        Nombre = nombre;
        Usuario = usuario;
    }

    public Album(int id, String padre, String nombre, String usuario)
    {

        Id = id;
        Padre = padre;
        Nombre = nombre;
        Usuario = usuario;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getPadre() {
        return Padre;
    }

    public void setPadre(String padre) {
        Padre = padre;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public ArrayList<Album> getAlbums() { return Albums; }

    public void setAlbums(ArrayList<Album> albums) { this.Albums = albums; }

    public ArrayList<Contenido> getContenido() { return Contenido; }

    public void setContenido(ArrayList<Contenido> contenido) { this.Contenido = contenido; }
}
