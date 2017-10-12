package com.example.jeiro.organizapp.Modelo;

/**
 * Created by rcrodriguez on 8/10/2017.
 */

public class Contenido
{
    private int Id;
    private String Padre;
    private String Tipo;
    private String Nombre;
    private String Usuario;


    public Contenido() {}

    public Contenido(String padre, String tipo, String nombre, String usuario)
    {
        Padre = padre;
        Tipo = tipo;
        Nombre = nombre;
        Usuario = usuario;
    }

    public Contenido(int id, String padre, String tipo, String nombre, String usuario)
    {
        Id = id;
        Padre = padre;
        Tipo = tipo;
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

    public String getUsuario() { return Usuario; }

    public void setUsuario(String usuario) { Usuario = usuario; }
}
