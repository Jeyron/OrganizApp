package com.example.jeiro.organizapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button iniciar_sesion, registrar;
    EditText txt_contraseña,txt_nombre_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();  // se elimina la barra del activity para que se vea mejor la interfaz
        actionBar.hide();

        iniciar_sesion =(Button)findViewById(R.id.btn_iniciar_sesion);
        iniciar_sesion.setOnClickListener(new View.OnClickListener() {
        //String nombre=txt_nombre_usuario.getText().toString();
        //String contraseña=txt_contraseña.getText().toString();
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, Opciones_menu.class);
                //hacer los put extra nombre y contraseña
                startActivity(intent);
                /*if((TextUtils.isEmpty(nombre)) && (TextUtils.isEmpty(contraseña))){
                        Toast.makeText(MainActivity.this,"¡Atencion! Debe ingresar todos los datos requeridos.",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(txt_nombre_usuario.length()==0){
                        txt_nombre_usuario.setError("Ingrese el nombre de usuario por favor.");
                    }
                    else{
                        if(txt_contraseña.length()==0){
                            txt_contraseña.setError("Ingrese la contraseña del usuario por favor.");
                        }
                        else{
                            if((validar_usuario(txt_nombre_usuario.getText().toString())) && (validar_contraseña(txt_nombre_usuario.getText().toString(),txt_contraseña.getText().toString()))){
                                Intent intent= new Intent(MainActivity.this, Opciones_menu.class);
                                //hacer los put extra nombre y contraseña
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(MainActivity.this,"¡Atencion, datos incorrectos! Por favor verifique que los datos" +
                                        "sean validos",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }*/

            }
        });

        registrar = (Button)findViewById(R.id.btn_registrarse);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, Registrar_usuario.class);
                //hacer los put extra nombre y contraseña
                startActivity(intent);
            }
        });
    }


    /**
     *b3e7f9
     * @param nombre
     * @return
     */
    public boolean validar_usuario(String nombre){
        return true;
    }

    /**
     *
     * @param nombre
     * @param contraseña
     * @return
     */
    public boolean validar_contraseña(String nombre, String contraseña){
        return true;
    }
}
