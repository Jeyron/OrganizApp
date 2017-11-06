package com.example.jeiro.organizapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.jeiro.organizapp.Datos.datos_album;
import com.example.jeiro.organizapp.Datos.datos_contenido;
import com.example.jeiro.organizapp.Modelo.Album;
import com.example.jeiro.organizapp.Modelo.Contenido;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Importa archivos desde medios externos
 */
public class importar extends AppCompatActivity {
    private static int IMAGE_PICKER_SELECT = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_gallery_preview_photo);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //*
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});
        startActivityForResult(intent, IMAGE_PICKER_SELECT);
        //*/
    }

    @Override
    public void onBackPressed()
    {
        finish();
        Intent intent= new Intent(this, Opciones_menu.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_SELECT && resultCode == RESULT_OK && null != data) {
            Uri a = data.getData();
            String selection = null;
            String p = null;
            String nuevo_path = null;
            String[] selectionArgs = null;
            final String docId = DocumentsContract.getDocumentId(a);
            final String[] split = docId.split(":");
            final String type = split[0];
            if ("image".equals(type)) {
                a = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                a = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                a = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            selection = "_id=?";
            selectionArgs = new String[]{ split[1] };
            String[] projection = { MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(a, projection, selection, selectionArgs, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                p = cursor.getString(column_index);

            }
            try {
                //*
                File file = new File(p);
                file.getName();
                datos_album datos = new datos_album();
                datos_contenido contenido = new datos_contenido();
                String nuevo_padre = MainActivity.padre;
                Album album_nuevo = datos.obtener_album(this, nuevo_padre);
                album_nuevo.setPadre(nuevo_padre);
                //*
                String path = p;
                nuevo_path = MainActivity.root_usuario + datos.obtener_album_path(this, album_nuevo);
                Contenido content;
                java.util.Date date= new java.util.Date();
                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(date.getTime());

                if (data.getData().toString().contains("image"))
                    content = new Contenido( MainActivity.padre,Function.PHOTO_TYPE, timeStamp + file.getName().substring(file.getName().length()-4,file.getName().length()),MainActivity.usuario_activo.getUsuario());
                else
                    if (data.getData().toString().contains("video"))
                        content = new Contenido( MainActivity.padre,Function.VIDEO_TYPE, timeStamp + file.getName().substring(file.getName().length()-4,file.getName().length()),MainActivity.usuario_activo.getUsuario());
                    else
                        content = new Contenido();
                if (Function.importar_contenido(path, nuevo_path, timeStamp + file.getName().substring(file.getName().length()-4,file.getName().length()))) {
                    if (contenido.insertar_contenido(content, true, this))
                    {
                        Toast.makeText(this, getResources().getString(R.string.toast_se_creo_album), Toast.LENGTH_SHORT).show();;
                    }
                    else
                    {
                        Toast.makeText(this, getResources().getString(R.string.toast_no_se_creo_album), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else
                {
                    Toast.makeText(this, getResources().getString(R.string.toast_ya_existe_album), Toast.LENGTH_SHORT).show();
                    return;
                }
                //*/
            } catch (Exception e) {
                Toast.makeText(this, getResources().getString(R.string.toast_error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (data.getData().toString().contains("image")) {

            } else  if (data.getData().toString().contains("video")) {

            }
            Toast.makeText(this, p, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, nuevo_path, Toast.LENGTH_SHORT).show();
        }
        finish();
        Intent intent= new Intent(this, Opciones_menu.class);
        startActivity(intent);
    }
}