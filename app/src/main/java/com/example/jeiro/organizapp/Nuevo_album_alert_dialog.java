package com.example.jeiro.organizapp;

/**
 * Created by rcrodriguez on 10/10/2017.
 */

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeiro.organizapp.Datos.datos_album;
import com.example.jeiro.organizapp.Modelo.Album;

public class Nuevo_album_alert_dialog extends DialogFragment {
    EditText txtname;
    Button btnDone;
    static String DialogboxTitle;

    public interface InputNameDialogListener {
        void onFinishInputDialog(String inputText);
    }

    //---empty constructor required
    public Nuevo_album_alert_dialog() {

    }
    //---set the title of the dialog window
    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){

        View view = inflater.inflate(
                R.layout.obtener_nombre, container);

        //---get the EditText and Button views
        txtname = (EditText) view.findViewById(R.id.txtName);
        btnDone = (Button) view.findViewById(R.id.btnDone);

        //---event handler for the button
        btnDone.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {

                try {
                    //*
                    String inputText = txtname.getText().toString();
                    if (inputText.equals(""))
                    {
                        Toast.makeText(getActivity(), "Error, nombre del nuevo álbum vacío",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getActivity(), "Input Name to dialog: " + inputText,
                            Toast.LENGTH_SHORT).show();

                    datos_album datos = new datos_album();
                    Album album = new Album(Opciones_menu.padre,inputText,MainActivity.usuario_activo.getUsuario());

                    //*

                    String path = Opciones_menu.root_usuario + datos.obtener_album_path(getActivity(), album);
                    if(Function.crear_album(path, album.getNombre()))
                    {
                        if (datos.insertar_album(album, true, getActivity()))
                            Toast.makeText(getActivity(), "Éxito, se ha creado un nuevo álbum", Toast.LENGTH_SHORT).show();
                        else
                        {
                            Function.delete_album(path, album.getNombre());
                            Toast.makeText(getActivity(), "Error, no ha sido posible crear el álbum", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Error, el álbum ya existe", Toast.LENGTH_SHORT).show();
                    }
                    //*/
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(),"Error, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        //---show the keyboard automatically
        txtname.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //---set the title for the dialog
        getDialog().setTitle(DialogboxTitle);

        return view;
    }


}