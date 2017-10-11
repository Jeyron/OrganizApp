package com.example.jeiro.organizapp;

import android.content.Context;
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

import com.example.jeiro.organizapp.Datos.datos_contenido;
import com.example.jeiro.organizapp.Modelo.Contenido;

import java.util.ArrayList;

public class Fragment_capturas extends Fragment {

    private static Fragment_capturas instance = null;

    public Fragment_capturas() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        datos_contenido datos = new datos_contenido();
        ArrayList<Contenido> lista_datos = datos.obtener_contenidos(getActivity());
        View v;
        if(lista_datos.size() == 0)
        {
            v = inflater.inflate(R.layout.fragment_vacio, container, false);

            TextView textView = (TextView) v.findViewById(R.id.label);
            textView.setText("No hay contenido");

        }
        else
        {
            v = inflater.inflate(R.layout.fragment_fragment_capturas, container, false);

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

    public static Fragment_capturas newInstance(String text){

        Fragment_capturas fragment = new Fragment_capturas();
        Bundle args = new Bundle();
        args.putString("msg", text);
        //seccion_actual = sectionNumber;
        fragment.setArguments(args);
        return fragment;

    }}
