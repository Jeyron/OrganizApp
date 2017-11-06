package com.example.jeiro.organizapp;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Environment;
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
    static final int REQUEST_PERMISSION_KEY = 1;
    public static String  root;
    public static Usuario usuario_activo;
    public static String root_usuario;
    public static String padre = "";
    public static boolean usado_por_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();  // se elimina la barra del activity para que se vea mejor la interfaz
        actionBar.hide();

        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET};
        if(!Function.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_KEY);
        }

        ContextWrapper context = new ContextWrapper(getApplicationContext());
        File carpetaContenedora = new File(getExternalFilesDir(null), "OrganizApp");//context.getDir( "OrganizApp", Context.MODE_PRIVATE);

        if (!carpetaContenedora.exists())
        {
            carpetaContenedora.mkdirs();
            Toast.makeText(this,getResources().getString(R.string.toast_carpeta_creada), Toast.LENGTH_SHORT).show();
        }
        root = carpetaContenedora.getAbsolutePath().toString();

        datos_usuario datos = new datos_usuario();

        if(datos.obtener_usuarios(this).size() == 0)
            ((Button)findViewById(R.id.btn_registrarse)).setEnabled(false);
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
            Toast.makeText(this,getResources().getString(R.string.toast_espacio_vacio), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, getResources().getString(R.string.toast_contraseña_incorrecta), Toast.LENGTH_SHORT).show();
                return;
            }
            //Toast.makeText(this,"Bienvenido " + usuario_activo.getNombre().toLowerCase(), Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(MainActivity.this, Opciones_menu.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, getResources().getString(R.string.toast_no_encontrado), Toast.LENGTH_SHORT).show();
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

