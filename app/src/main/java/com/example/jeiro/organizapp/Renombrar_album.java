package com.example.jeiro.organizapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeiro.organizapp.Datos.datos_album;
import com.example.jeiro.organizapp.Modelo.Album;

/**
 * Created by rcrodriguez on 11/10/2017.
 */

public class Renombrar_album extends DialogFragment {
    EditText txtname;
    Button btnDone;
    static String DialogboxTitle;

    public interface InputNameDialogListener {
        void onFinishInputDialog(String inputText);
    }

    //---empty constructor required
    public Renombrar_album() {

    }

    //---set the title of the dialog window
    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(
                R.layout.dialog_obtener, container);

        //---get the EditText and Button views
        txtname = (EditText) view.findViewById(R.id.txtName);
        //btnDone = (Button) view.findViewById(R.id.btnDone);
        TextView t = (TextView) view.findViewById(R.id.lblTitulo);
        t.setText(getResources().getString(R.string.btn_Renombrar_album));

        //---event handler for the button
        btnDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {
                    //*
                    String inputText = txtname.getText().toString();
                    if (inputText.equals("")) {
                        Toast.makeText(getActivity(), "Error, el nuevo nombre está vacío",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getActivity(), "Input Name to dialog: " + inputText,
                            Toast.LENGTH_SHORT).show();

                    datos_album datos = new datos_album();
                    Album album = new Album(MainActivity.padre, inputText, MainActivity.usuario_activo.getUsuario());

                    if (datos.obtener_album(getActivity(), album) != null) {
                        Toast.makeText(getActivity(), "Error, nuevo nombre en uso",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //*
                    String path = MainActivity.root_usuario + datos.obtener_album_path(getActivity(), album);
                    if (Function.rename_album(path, MainActivity.string_temporal, album.getNombre())) {
                        if (datos.insertar_album(album, true, getActivity()))
                        {
                            album = new Album(MainActivity.padre, MainActivity.string_temporal, MainActivity.usuario_activo.getUsuario());
                            datos.rename_album(album,getActivity());
                            MainActivity.string_temporal = null;
                            Toast.makeText(getActivity(), "Éxito, se ha creado un nuevo álbum", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Function.delete_album(path, album.getNombre());
                            Toast.makeText(getActivity(), "Error, no ha sido posible crear el álbum", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error, el álbum ya existe", Toast.LENGTH_SHORT).show();
                    }
                    //*/
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        //---show the keyboard automatically
        txtname.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //---set the title for the dialog
        getDialog().setTitle(DialogboxTitle);

        return view;
    }
}