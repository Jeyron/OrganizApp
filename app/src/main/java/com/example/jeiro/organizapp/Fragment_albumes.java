package com.example.jeiro.organizapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.jeiro.organizapp.Datos.*;
import com.example.jeiro.organizapp.Modelo.*;

import java.util.ArrayList;

public class Fragment_albumes extends Fragment {


    public Fragment_albumes() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        datos_album datos = new datos_album();
        ArrayList<Album> lista_datos = datos.obtener_albums(getActivity());
        View v;
        if(lista_datos.size() == 0)
        {
            v = inflater.inflate(R.layout.fragment_vacio, container, false);

            TextView textView = (TextView) v.findViewById(R.id.label);
            textView.setText("No hay álbumes");

        }
        else
        {
            v = inflater.inflate(R.layout.fragment_fragment_albumes, container, false);

            GridView galleryGridView = (GridView) getActivity().findViewById(R.id.galleryGridView);
            int iDisplayWidth = getResources().getDisplayMetrics().widthPixels ;
            Resources resources = getActivity().getApplicationContext().getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            float dp = iDisplayWidth / (metrics.densityDpi / 160f);

            if(dp < 360)
            {
                dp = (dp - 17) / 2;
                float px = Function.convertDpToPixel(dp, getActivity().getApplicationContext());
                galleryGridView.setColumnWidth(Math.round(px));
            }
        }
        return v;
    }

    public static Fragment_albumes newInstance(String text){

        Fragment_albumes fragment = new Fragment_albumes();
        Bundle args = new Bundle();
        args.putString("msg", text);
        //seccion_actual = sectionNumber;
        fragment.setArguments(args);
        return fragment;

    }
}
