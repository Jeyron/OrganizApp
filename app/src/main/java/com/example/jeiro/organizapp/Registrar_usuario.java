package com.example.jeiro.organizapp;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeiro.organizapp.Datos.*;
import com.example.jeiro.organizapp.Modelo.*;

public class Registrar_usuario extends AppCompatActivity {
    Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void registrar_usuario(View v)
    {
        try {
            String password = ((EditText) findViewById(R.id.txt_contraseña_registro)).getText().toString();
            String password_confirmar = ((EditText) findViewById(R.id.txt_confirmar_contraseña_registro)).getText().toString();
            String nombre_completo = ((EditText) findViewById(R.id.txt_nombre_completo_registro)).getText().toString();
            String usuario = ((EditText) findViewById(R.id.txt_nombre_usuario_registro)).getText().toString();

            //*
            if (!password.equals(password_confirmar)) {
                Toast.makeText(this,getResources().getString(R.string.toast_error_contra_diferentes), Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.equals("") || password_confirmar.equals("") || nombre_completo.equals("") || usuario.equals("")) {
                Toast.makeText(this,getResources().getString(R.string.toast_espacio_vacio), Toast.LENGTH_SHORT).show();
                return;
            }
            Usuario temp = new Usuario(usuario, password, nombre_completo);
            datos_usuario datos_usuario = new datos_usuario();
            if (datos_usuario.insertar_usuario(temp, true, this)) {
                Toast.makeText(this,getResources().getString(R.string.toast_se_registro), Toast.LENGTH_SHORT).show();
                this.finish();
            }
            else
            {
                Toast.makeText(this,getResources().getString(R.string.toast_no_se_guardan), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this,getResources().getString(R.string.toast_error) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
