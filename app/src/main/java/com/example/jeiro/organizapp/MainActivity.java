package com.example.jeiro.organizapp;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.jeiro.organizapp.Modelo.*;
import com.example.jeiro.organizapp.Datos.*;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    public static String  root;
    public static Usuario usuario_activo;
    public static String root_usuario;
    public static String padre = "";
    public static String string_temporal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();  // se elimina la barra del activity para que se vea mejor la interfaz
        actionBar.hide();

        ContextWrapper context = new ContextWrapper(getApplicationContext());
        File carpetaContenedora = context.getDir( "OrganizApp", Context.MODE_PRIVATE);
        if (!carpetaContenedora.exists())
        {
            carpetaContenedora.mkdirs();
            Toast.makeText(this,"Carpeta base creada", Toast.LENGTH_SHORT).show();
        }
        root = carpetaContenedora.getAbsolutePath().toString();
    }

    /**
     *
     * @param v
     */
    public void iniciar_sesion(View v)
    {
        String usuario = ((EditText)findViewById(R.id.txt_nombre_usuario)).getText().toString();
        String password = ((EditText)findViewById(R.id.txt_contraseña)).getText().toString();

        if (usuario.equals("") || password.equals(""))
        {
            Toast.makeText(this,"Error, algún espacio vacío", Toast.LENGTH_SHORT).show();
            return;
        }
        Usuario temp = new Usuario();
        temp.setUsuario(usuario);

        datos_usuario datos = new datos_usuario();
        temp = datos.obtener_usuarios(this, temp);

        if (temp != null)
        {
            usuario_activo = temp;
            if(!usuario_activo.getPassword().equals(password))
            {
                Snackbar.make(v, "Error, contraseña incorrecta", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                return;
            }
            //Toast.makeText(this,"Bienvenido " + usuario_activo.getNombre().toLowerCase(), Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(MainActivity.this, Opciones_menu.class);
            startActivity(intent);
        }
        else
        {
            Snackbar.make(v, "Error, No se ha encontrado el usuario", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    /**
     *
     * @param v
     */
    public void registrar_usuario (View v)
    {
        Intent intent= new Intent(MainActivity.this, Registrar_usuario.class);
        startActivity(intent);
    }
}

