package com.example.jeiro.organizapp.Modelo;

/**
 * Created by rcrodriguez on 8/10/2017.
 */

public class Contenido
{
    private int Id;
    private Album Padre;
    private String Tipo;
    private String Nombre;

    public Contenido() {}

    public Contenido(Album padre, String tipo, String nombre) {
        Padre = padre;
        Tipo = tipo;
        Nombre = nombre;
    }

    public Contenido(int id, String padre, String tipo, String nombre)
    {
        Padre = new Album();
        Id = id;
        Padre.setNombre(padre);
        Tipo = tipo;
        Nombre = nombre;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Album getPadre() {
        return Padre;
    }

    public void setPadre(Album padre) {
        Padre = padre;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
