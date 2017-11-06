package com.example.jeiro.organizapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jeiro.organizapp.Datos.*;
import com.example.jeiro.organizapp.Modelo.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/***
 * Clase que muestra y ordena los albumes y archivos del programa
 *
 */
public class Fragment_albumes extends Fragment
{

    GridView galleryGridView;
    LoadAlbumImages loadAlbumTask;
    public Fragment_albumes() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v;

        v = inflater.inflate(R.layout.fragment_fragment_albumes, container, false);

        galleryGridView = (GridView) v.findViewById(R.id.AlbumGridView);

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
        //*/

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.add_video);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), capturar_video.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        FloatingActionButton fab3 = (FloatingActionButton) v.findViewById(R.id.add_external);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), importar.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) v.findViewById(R.id.add_photo);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), capturar_foto.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        FloatingActionButton fab2 = (FloatingActionButton) v.findViewById(R.id.add_album);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_obtener);

                TextView text = (TextView) dialog.findViewById(R.id.lblTitulo);
                text.setText(getResources().getString(R.string.lbl_nuevo_album));

                Button btn1 = (Button)dialog.findViewById(R.id.btnObtener);
                btn1.setText(getResources().getString(R.string.btn_agregar));
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        EditText text = (EditText) dialog.findViewById(R.id.txtName);
                        try {
                            //*
                            String inputText = text.getText().toString();
                            if (inputText.equals(""))
                            {
                                Toast.makeText(getActivity(),getResources().getString(R.string.toast_nombre_album_vacio),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(getActivity(),getResources().getString(R.string.toast_name_dialog) + inputText,
                                    Toast.LENGTH_SHORT).show();

                            datos_album datos = new datos_album();
                            Album album = new Album(MainActivity.padre,inputText,MainActivity.usuario_activo.getUsuario());

                            if(datos.obtener_album(getActivity(), album.getNombre()) != null)
                            {
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_ya_existe_album),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //*

                            String path = MainActivity.root_usuario + File.separator + datos.obtener_album_path(getActivity(), album);
                            if(Function.crear_album(path, album.getNombre()))
                            {
                                if (datos.insertar_album(album, true, getActivity()))
                                {
                                    Toast.makeText(getActivity(),getResources().getString(R.string.toast_se_creo_album), Toast.LENGTH_SHORT).show();
                                    loadAlbumTask = new LoadAlbumImages();
                                    loadAlbumTask.execute();
                                    /*
                                    cargar_grid_view();
                                    set_adapter();
                                    //*/
                                    dialog.dismiss();
                                }
                                else
                                {
                                    Function.borrar_directorio(path, album.getNombre());
                                    Toast.makeText(getActivity(),getResources().getString(R.string.toast_no_se_creo_album), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_ya_existe_album),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //*/
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getActivity(),getResources().getString(R.string.toast_error) + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        //MainActivity.padre = "";
        if(MainActivity.padre.equals(""))
            getActivity().setTitle(MainActivity.usuario_activo.getNombre());
        else
            getActivity().setTitle(MainActivity.padre);

        //Toast.makeText(getActivity(),"todo parece bien", Toast.LENGTH_SHORT).show();
        loadAlbumTask = new LoadAlbumImages();
        loadAlbumTask.execute();
        /*
        cargar_grid_view();
        set_adapter();
        //*/
        return v;
    }

    /**
     * Crea una nueva instacia del fragment
     * @param text
     * @return
     */
    public static Fragment_albumes newInstance(String text){

        Fragment_albumes fragment = new Fragment_albumes();
        Bundle args = new Bundle();
        args.putString("msg", text);
        //seccion_actual = sectionNumber;
        fragment.setArguments(args);
        return fragment;

    }

    ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> pathList = new ArrayList<>();
    ArrayList<String> typeList = new ArrayList<>();
    int albums;

    /**
     * Permite refrescar la interfaz y obtener las rutas del contenido
     */


    class LoadAlbumImages extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            albumList.clear();
            pathList.clear();
            typeList.clear();
            albums = 0;
        }

        protected String doInBackground(String... args)
    /*
    public void cargar_grid_view()
    //*/
        {
            //albumList.clear();
            //Toast.makeText(getActivity(),"Entra", Toast.LENGTH_SHORT).show();
            //sleep();
            datos_album d_album = new datos_album();
            datos_contenido d_contenido = new datos_contenido();
            Album album = new Album("", MainActivity.padre, MainActivity.usuario_activo.getUsuario());
            ArrayList<Album> l_albums = d_album.obtener_albums_por_album(getActivity(), album);
            ArrayList<Contenido> l_contenido = d_contenido.obtener_contenido_por_album(getActivity(), album);

            //*
            String path = null; // Imagen path
            String name = null;
            String countPhoto = null;
            String tipo = null;
            String tipo_contenido = null;

            albums = l_albums.size();
            //albums
            for (int i = 0; i < l_albums.size(); i++) {
                Album temp = l_albums.get(i);
                name = temp.getNombre();                         // 1
                ArrayList<Contenido> content = d_contenido.obtener_contenido_por_album(getActivity(), temp);
                if (content.size() != 0)
                    path = MainActivity.root_usuario + d_album.obtener_album_path(getActivity(), new Album(content.get(0).getPadre(), "", "")) + File.separator + content.get(0).getNombre();
                else
                    path = "";                                         // 2
                // File acr = new File(Opciones_menu.root_usuario + path, temp.getNombre());

                countPhoto = Integer.toString(content.size());     // 3
                tipo = Function.ALBUM;                                            // 4
                tipo_contenido = "";                                   // 5

                albumList.add(Function.mappingInbox(name, path, countPhoto, tipo, tipo_contenido));
            }

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

        /**
         * Configura el layout y los componentes que lo integran
         */

        /*
        private void set_adapter() {
        //*/
        @Override
        protected void onPostExecute(String xml)
        {
            //Toast.makeText(getActivity(),"Albums " + albumList.size(), Toast.LENGTH_SHORT).show();
            //galleryGridView.setAdapter(null);
            AlbumAdapter adapter = new AlbumAdapter(getActivity(), albumList);
            galleryGridView.setAdapter(adapter);
            //*
            galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {

                    int view_id = view.getId();
                    if (view_id == Function.ID_ALBUM) {
                        AlbumViewHolder a = (AlbumViewHolder) view.getTag();
                        a.gallery_title.getText();
                        MainActivity.padre = a.gallery_title.getText().toString();
                        loadAlbumTask = new LoadAlbumImages();
                        loadAlbumTask.execute();
                        /*
                        cargar_grid_view();
                        set_adapter();
                        //*/
                        getActivity().setTitle(MainActivity.padre);
                    }
                    else
                    {
                        Intent intent = new Intent(getActivity(), GalleryPreview.class);
                        intent.putStringArrayListExtra("pathList", pathList);
                        intent.putStringArrayListExtra("typeList", typeList);
                        intent.putExtra("posicion", position - albums);
                        startActivity(intent);
                    }
                }
            });
            galleryGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               final int position, long id) {
                    int view_id = view.getId();

                    /***
                     * Dialogo de los albumes
                     ***/


                    if (view_id == Function.ID_ALBUM) {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_editar_eliminar);
                        TextView txt = (TextView) dialog.findViewById(R.id.dialog_title);
                        txt.setText("Álbum opciones");

                        Button btn1 = (Button) dialog.findViewById(R.id.btnEditar);
                        btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText text = (EditText) dialog.findViewById(R.id.txtName);
                                try {
                                    //*
                                    String inputText = text.getText().toString();
                                    if (inputText.equals("")) {
                                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_nuevo_nombre_vacio),
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_name_dialog) + inputText,
                                            Toast.LENGTH_SHORT).show();

                                    datos_album datos = new datos_album();
                                    Album album_anterior = new Album(MainActivity.padre, albumList.get(+position).get(Function.KEY_ALBUM), MainActivity.usuario_activo.getUsuario());

                                    if (datos.obtener_album(getActivity(), inputText) != null) {
                                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_nombre_uso),
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Album album_nuevo = datos.obtener_album(getActivity(), albumList.get(+position).get(Function.KEY_ALBUM));
                                    album_nuevo.setNombre(inputText);
                                    //*
                                    String path = MainActivity.root_usuario + File.separator + datos.obtener_album_path(getActivity(), album_anterior);
                                    if (Function.rename_album(path, album_anterior.getNombre(), album_nuevo.getNombre())) {
                                        if (datos.rename_album(album_nuevo, album_anterior, getActivity())) {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_se_creo_album), Toast.LENGTH_SHORT).show();

                                            /*
                                            cargar_grid_view();
                                            set_adapter();
                                            //*/
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_se_creo_album), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_ya_existe_album), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    //*/
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                dialog.dismiss();
                            }
                        });

                        Button btn2 = (Button) dialog.findViewById(R.id.btnEliminar);
                        btn2.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        try {
                                                            datos_album datos = new datos_album();
                                                            Album temp = datos.obtener_album(getActivity(), albumList.get(+position).get(Function.KEY_ALBUM));
                                                            String path = MainActivity.root_usuario + File.separator + datos.obtener_album_path(getActivity(), temp);
                                                            if (Function.borrar_directorio(path, temp.getNombre())) {
                                                                if (datos.eliminar_album(temp, getActivity())) {
                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_se_elimino_album), Toast.LENGTH_SHORT).show();
                                                                    MainActivity.usado_por_fragment = true;
                                                                    getActivity().onBackPressed();
                                        /*
                                        cargar_grid_view();
                                        set_adapter();
                                        //*/
                                                                    dialog.dismiss();
                                                                } else {
                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_posible_eliminar_album), Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                            } else {
                                                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_existe), Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (Exception e) {
                                                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                        dialog.dismiss();
                                                    }
                                                }
                        );

                        dialog.show();
                    }


                    /***
                     * Para configurar el dialog de los archivos
                     ***/


                    else {
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_editar_eliminar_mover_compartir);
                        Spinner spn = (Spinner) dialog.findViewById(R.id.spn_albumes);
                        TextView txt = (TextView) dialog.findViewById(R.id.dialog_title);
                        datos_album datos = new datos_album();
                        ArrayList<Album> list = datos.obtener_albums(getActivity());
                        String albums[] = new String[list.size() + 1];
                        albums[0] = "ROOT";
                        for (int i = 1; i < list.size() + 1; i++) {
                            albums[i] = list.get(i - 1).getNombre().toUpperCase();
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_layout, albums);
                        spn.setAdapter(adapter);


                        final Button btn1 = (Button) dialog.findViewById(R.id.btn_eliminar);
                        btn1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    datos_contenido datos = new datos_contenido();
                                    datos_album datos_album = new datos_album();
                                    Contenido temp = datos.obtener_contenido(getActivity(), albumList.get(+position).get(Function.KEY_ALBUM));
                                    String path = MainActivity.root_usuario + datos_album.obtener_album_path(getActivity(), new Album(temp.getPadre(), "", MainActivity.usuario_activo.getUsuario()));
                                    if (Function.borrar_directorio(path, temp.getNombre())) {
                                        if (datos.eliminar_contenido(temp, getActivity())) {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_se_elimino_album), Toast.LENGTH_SHORT).show();

                                            dialog.dismiss();
                                            MainActivity.padre = temp.getPadre();
                                            MainActivity.usado_por_fragment = true;
                                            getActivity().onBackPressed();
                                        /*
                                        Intent intent= new Intent(getActivity(), Opciones_menu.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                        //*/

                                        } else {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_posible_eliminar_album), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_existe), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });


                        final Button btn2 = (Button) dialog.findViewById(R.id.btn_mover);
                        btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Spinner spn = (Spinner) dialog.findViewById(R.id.spn_albumes);
                                try {
                                    //*
                                    String inputText = spn.getSelectedItem().toString();

                                    datos_album datos = new datos_album();
                                    datos_contenido contenido = new datos_contenido();
                                    Album album_anterior = new Album(MainActivity.padre, MainActivity.padre, MainActivity.usuario_activo.getUsuario());
                                    String nuevo_padre = (inputText.equals("ROOT")) ? "" : inputText.toLowerCase();
                                    Album album_nuevo = datos.obtener_album(getActivity(), nuevo_padre);
                                    album_nuevo.setPadre(nuevo_padre);
                                    //*
                                    String path = MainActivity.root_usuario + File.separator + datos.obtener_album_path(getActivity(), album_anterior);
                                    String nuevo_path = MainActivity.root_usuario + File.separator + datos.obtener_album_path(getActivity(), album_nuevo);
                                    Contenido content = contenido.obtener_contenido(getActivity(), albumList.get(+position).get(Function.KEY_ALBUM));
                                    content.setPadre(album_nuevo.getNombre());
                                    if (Function.mover_contenido(path, nuevo_path, albumList.get(+position).get(Function.KEY_ALBUM))) {
                                        if (contenido.insertar_contenido(content, false, getActivity())) {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_movio_arhivo), Toast.LENGTH_SHORT).show();
                                            MainActivity.usado_por_fragment = true;
                                            getActivity().onBackPressed();
                                        /*
                                        cargar_grid_view();
                                        set_adapter();
                                        //*/
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_se_puede_mover), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_mover_ya_exite), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    //*/
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                dialog.dismiss();
                            }
                        });


                        final Button btn_compartir_multimedia = (Button) dialog.findViewById(R.id.btn_compartir);
                        btn_compartir_multimedia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //  AQUI VA EL PATH DE LA IMAGEN ESTO ES UNA IMAGEN QUEMADA XQ ESTOY PRBANDO
                                Uri imagen = Uri.parse("file://" + albumList.get(+position).get(Function.KEY_PATH));
                                //
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("image/*");
                                String shareBody = "Dato principal, es el titulo";
                                String shareSub = "Asunto, detalles";
                                intent.putExtra(Intent.EXTRA_STREAM, imagen);
                                intent.putExtra(Intent.EXTRA_TEXT, albumList.get(+position).get(Function.KEY_TIPO_CONTENIDO));
                                startActivity(Intent.createChooser(intent, "Compartir Datos"));
                            }
                        });
                        dialog.show();
                    }
                    return true;
                }
            });
            //*/
        }
    }
}


/**
 * Adaptador utilizado para mostrar los contenidos de la mejor manera posible
 */

class AlbumAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap< String, String >> data;

    public AlbumAdapter(Activity a, ArrayList < HashMap < String, String >> d) {
        activity = a;
        data = d;
    }
    @Override
    public int getCount() {
        return data.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AlbumViewHolder holder = null;
        HashMap < String, String > song = new HashMap < String, String > ();
        song = data.get(position);

        String nombre = song.get(Function.KEY_ALBUM);
        String ruta = song.get(Function.KEY_PATH);
        String photos = song.get(Function.KEY_COUNT);
        String tipo = song.get(Function.KEY_TIPO);
        String tipo_contenido = song.get(Function.KEY_TIPO_CONTENIDO);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            holder = new AlbumViewHolder();
        }
        else
        {
            holder = (AlbumViewHolder) convertView.getTag();
        }

        if(tipo.equals(Function.CONTENIDO))
        {
            if(tipo_contenido.equals(Function.PHOTO_TYPE))
            {
                convertView = inflater.inflate(
                        R.layout.imagen_layout, parent, false);

                convertView.setId(Function.ID_IMAGE);

                holder.galleryImage = (ImageView) convertView.findViewById(R.id.galleryImage);
                holder.galleryImage.setId(position);
            }
            else
            {
                convertView = inflater.inflate(
                        R.layout.video_layout, parent, false);

                convertView.setId(Function.ID_VIDEO);

                holder.galleryVideo = (ImageView) convertView.findViewById(R.id.galleryImage);
                holder.galleryVideo.setId(position);
            }
        }
        else
        {
            convertView = inflater.inflate(
                    R.layout.album_layout, parent, false);
            convertView.setId(Function.ID_ALBUM);
            holder.galleryImage = (ImageView) convertView.findViewById(R.id.galleryImage);
            holder.gallery_count = (TextView) convertView.findViewById(R.id.gallery_count);
            holder.gallery_title = (TextView) convertView.findViewById(R.id.gallery_title);
            holder.galleryImage.setId(position);
            holder.gallery_title.setId(position);
            holder.gallery_count.setId(position);
        }

        convertView.setTag(holder);
        try {

            if(tipo.equals(Function.ALBUM))
            {
                holder.gallery_count.setText(activity.getResources().getString(R.string.txt_archivo) + " " + song.get(Function.KEY_COUNT));
                holder.gallery_title.setText(song.get(Function.KEY_ALBUM));
            }

            String path = song.get(Function.KEY_PATH);

            if(path.equals(""))
                Glide.with(activity)
                        .load(R.drawable.album) // Uri of the picture
                        .into(holder.galleryImage);
            else
            {
                if(tipo_contenido.equals(Function.VIDEO_TYPE))
                {
                    Glide.with(activity)
                            .asBitmap()
                            .load(Uri.fromFile(new File(path)))
                            .into(holder.galleryVideo);
                }
                else
                    Glide.with(activity)
                        .load(new File(path)) // Uri of the picture
                        .into(holder.galleryImage);
            }

        } catch (Exception e) {}
        return convertView;
    }
}


/**
 * Funciona como entidad
 */

class AlbumViewHolder {
    ImageView galleryImage, galleryVideo;
    TextView gallery_count, gallery_title;
}
