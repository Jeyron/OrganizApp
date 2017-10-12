package com.example.jeiro.organizapp;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.jeiro.organizapp.Datos.*;
import com.example.jeiro.organizapp.Modelo.*;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class capturar_video extends AppCompatActivity {

    static final int REQUEST_PHOTO_CAPTURE = 1;

    File mediaFile;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capturar_video);

        //Permisos para poder escribir en ruta Externa
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        CapturadorImagen();

    }

    private void CapturadorImagen() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        generarRutaArchivo();
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_PHOTO_CAPTURE);
        }
    }

    public void generarRutaArchivo(){
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(date.getTime());
        datos_album datos = new datos_album();
        name = "VID_" + timeStamp + ".mp4";
        mediaFile = new File(MainActivity.root_usuario + File.separator + datos.obtener_album_path(this,new Album(MainActivity.padre, MainActivity.string_temporal,MainActivity.usuario_activo.getUsuario())) + File.separator + name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if(MediaStore.EXTRA_OUTPUT.isEmpty()) {
                mediaFile.createNewFile();
                datos_contenido content = new datos_contenido();
                Contenido imagen = new Contenido(MainActivity.padre, Function.VIDEO_TYPE, name, MainActivity.usuario_activo.getUsuario());
                content.insertar_contenido(imagen, true, this);
                Toast.makeText(this, "Éxito, la foto se guardó: " + mediaFile.getPath(), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e)
        {
            Toast.makeText(this, "Error, no ha sido posible agregar la foto", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }finally {
            Intent intent= new Intent(this, Opciones_menu.class);
            startActivity(intent);
            finish();
        }
    }
}