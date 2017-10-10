package com.example.jeiro.organizapp.Modelo;

/**
 * Created by rcrodriguez on 8/10/2017.
 */

public class Usuario
{
    private int Id;
    private String Usuario;
    private String Password;
    private String Nombre;

    public Usuario() {}

    public Usuario(String usuario, String password, String nombre) {
        Usuario = usuario;
        Password = password;
        Nombre = nombre;
    }

    public Usuario(int id, String usuario, String password, String nombre) {
        Id = id;
        Usuario = usuario;
        Password = password;
        Nombre = nombre;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getNombre() { return Nombre; }

    public void setNombre(String nombre) { Nombre = nombre; }
}
