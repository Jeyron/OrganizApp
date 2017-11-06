package com.example.jeiro.organizapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeiro.organizapp.Datos.datos_album;
import com.example.jeiro.organizapp.Datos.datos_contenido;
import com.example.jeiro.organizapp.Modelo.Album;
import com.example.jeiro.organizapp.Modelo.Contenido;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_capturas extends Fragment {

    private static Fragment_capturas instance = null;
    GridView galleryGridView;
    LoadImages loadTask;
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
            textView.setText(getResources().getString(R.string.txt_vacio));

        }
        else
        {
            v = inflater.inflate(R.layout.fragment_fragment_capturas, container, false);

            galleryGridView = (GridView) v.findViewById(R.id.GalleryGridView);
            int iDisplayWidth = getResources().getDisplayMetrics().widthPixels;
            Resources resources = getActivity().getApplicationContext().getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            float dp = iDisplayWidth / (metrics.densityDpi / 160f);

            if(dp < 360)
            {
                dp = (dp - 17) / 2;
                float px = Function.convertDpToPixel(dp, getActivity().getApplicationContext());
                galleryGridView.setColumnWidth(Math.round(px));
            }
            loadTask = new LoadImages();
            loadTask.execute();
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

    }

    ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> pathList = new ArrayList<>();
    ArrayList<String> typeList = new ArrayList<>();
    class LoadImages extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            albumList.clear();
            pathList.clear();
            typeList.clear();
        }

        protected String doInBackground(String... args)
    /*
    public void cargar_grid_view()
    //*/ {
            datos_album d_album = new datos_album();
            datos_contenido d_contenido = new datos_contenido();
            ArrayList<Contenido> l_contenido = d_contenido.obtener_contenidos(getActivity());

            //*
            String path = null; // Imagen path
            String name = null;
            String countPhoto = null;
            String tipo = null;
            String tipo_contenido = null;

            // contenido
            for (int i = 0; i < l_contenido.size(); i++) {
                Contenido temp = l_contenido.get(i);
                name = temp.getNombre();
                path = MainActivity.root_usuario + d_album.obtener_album_path(getActivity(), new Album(temp.getPadre(), "", MainActivity.usuario_activo.getUsuario())) + File.separator + temp.getNombre();
                tipo = Function.CONTENIDO;
                countPhoto = "";
                tipo_contenido = temp.getTipo();
                albumList.add(Function.mappingInbox(name, path, countPhoto, tipo, tipo_contenido));
                pathList.add(path);
                typeList.add(tipo_contenido);
            }
            //*/
            return "";
        }

        /*
            private void set_adapter() {
            //*/
        @Override
        protected void onPostExecute(String xml) {
            //Toast.makeText(getActivity(),"Albums " + albumList.size(), Toast.LENGTH_SHORT).show();
            //galleryGridView.setAdapter(null);
            AlbumAdapter adapter = new AlbumAdapter(getActivity(), albumList);
            galleryGridView.setAdapter(adapter);
            //*
            galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {

                    int view_id = view.getId();
                    Intent intent = new Intent(getActivity(), GalleryPreview.class);
                    intent.putStringArrayListExtra("pathList", pathList);
                    intent.putStringArrayListExtra("typeList", typeList);
                    intent.putExtra("posicion", position);
                    startActivity(intent);
                    Toast.makeText(getActivity(), albumList.get(+position).get(Function.KEY_PATH), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
