package com.example.jeiro.organizapp;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.jeiro.organizapp.Datos.*;
import com.example.jeiro.organizapp.Modelo.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Fragment_albumes extends Fragment
{

    GridView galleryGridView;
    public Fragment_albumes() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        datos_album datos = new datos_album();
        ArrayList<Album> lista_datos = datos.obtener_albums_por_album(getActivity(),new Album( Opciones_menu.padre,"",MainActivity.usuario_activo.getUsuario()));
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

            galleryGridView = (GridView) v.findViewById(R.id.galleryGridView);
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

            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.add_album);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnShowDialog(view);
                    Snackbar.make(view, "Éxito, se ha creado un álbum", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    cargar_grid_view();
                    set_adapter();
                }
            });

            Opciones_menu.padre = "";

            Toast.makeText(getActivity(),"todo parece bien", Toast.LENGTH_SHORT).show();
            cargar_grid_view();
            set_adapter();
        }
        return v;
    }

    public void btnShowDialog(View view) {
        showInputNameDialog();
    }

    private void showInputNameDialog() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Nuevo_album_alert_dialog inputNameDialog = new Nuevo_album_alert_dialog();
        inputNameDialog.setCancelable(false);
        inputNameDialog.setDialogTitle("Nuevo álbum");
        inputNameDialog.show(fragmentManager, "Input Dialog");
    }

    private void sleep ()
    {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Fragment_albumes newInstance(String text){

        Fragment_albumes fragment = new Fragment_albumes();
        Bundle args = new Bundle();
        args.putString("msg", text);
        //seccion_actual = sectionNumber;
        fragment.setArguments(args);
        return fragment;

    }

    ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();

    public void cargar_grid_view()
    {
        albumList.clear();
        Toast.makeText(getActivity(),"Entra", Toast.LENGTH_SHORT).show();
        //sleep();
        datos_album d_album              = new datos_album();
        datos_contenido d_contenido      = new datos_contenido();
        Album album = new Album(Opciones_menu.padre,"",MainActivity.usuario_activo.getUsuario());
        ArrayList<Album> l_albums        = d_album.obtener_albums_por_album(getActivity(), album);
        ArrayList<Contenido> l_contenido = d_contenido.obtener_contenido_por_album(getActivity(), album);

        //*
        String path = null; // Imagen path
        String name = null;
        String countPhoto = null;
        String tipo = null;
        String tipo_contenido = null;

        //albums
        for(int i = 0; i < l_albums.size(); i++)
        {
            Album temp = l_albums.get(i);
            name = temp.getNombre();                         // 1
            if(l_contenido.size() != 0)
                path = Opciones_menu.root_usuario + File.separator + d_album.obtener_album_path(getActivity(),new Album(l_contenido.get(0).getPadre(),"","")) + l_contenido.get(0).getNombre();
            else
                path = "";                                         // 2
            // File acr = new File(Opciones_menu.root_usuario + path, temp.getNombre());
            countPhoto = Integer.toString(l_contenido.size());     // 3
            tipo = Function.ALBUM;                                            // 4
            tipo_contenido = "";                                   // 5

            albumList.add(Function.mappingInbox(name, path, countPhoto, tipo, tipo_contenido));
        }

        // contenido
        for(int i = 0; i < l_contenido.size(); i++)
        {
            Contenido temp = l_contenido.get(i);
            name = temp.getNombre();
            path = Opciones_menu.root_usuario + File.separator + d_album.obtener_album_path(getActivity(),new Album(temp.getPadre(),"","")) + temp.getNombre();
            tipo = Function.CONTENIDO;
            countPhoto = "";
            tipo_contenido = temp.getTipo();
            albumList.add(Function.mappingInbox(name, path, countPhoto, tipo, tipo_contenido));
        }
        //*/
    }

    private void set_adapter()
    {
        Toast.makeText(getActivity(),"Albums " + albumList.size(), Toast.LENGTH_SHORT).show();
        //galleryGridView.setAdapter(null);
        AlbumAdapter adapter = new AlbumAdapter(getActivity(), albumList);
        galleryGridView.setAdapter(adapter);
        //*
        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                /*
                Intent intent = new Intent(getActivity(), Fragment_albumes.class);
                intent.putExtra("name", albumList.get(+position).get(Function.KEY_ALBUM));
                startActivity(intent);
                //*/
                int view_id = view.getId();
                Toast.makeText(getActivity(),"view_id " + view_id + " - " + id , Toast.LENGTH_SHORT).show();
            }
        });
        //*/
    }
}





class AlbumAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap< String, String >> data;
    public AlbumAdapter(Activity a, ArrayList < HashMap < String, String >> d) {
        activity = a;
        data = d;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumViewHolder holder = null;
        HashMap < String, String > song = new HashMap < String, String > ();
        song = data.get(position);

        String nombre = song.get(Function.KEY_ALBUM);
        String ruta = song.get(Function.KEY_PATH);
        String photos = song.get(Function.KEY_COUNT);
        String tipo = song.get(Function.KEY_TIPO);
        String tipo_contenido = song.get(Function.KEY_TIPO_CONTENIDO);

        if (convertView == null) {
            holder = new AlbumViewHolder();

            if(tipo.equals(Function.CONTENIDO))
            {
                if(tipo_contenido.equals(Function.PHOTO_TYPE))
                {
                    convertView = LayoutInflater.from(activity).inflate(
                            R.layout.imagen_layout, parent, false);

                    holder.galleryImage = (ImageView) convertView.findViewById(R.id.galleryImage);
                    holder.gallery_title = (TextView) convertView.findViewById(R.id.gallery_title);
                    holder.galleryImage.setId(position);
                    holder.gallery_title.setId(position);
                }
                else
                {
                    convertView = LayoutInflater.from(activity).inflate(
                            R.layout.video_layout, parent, false);

                    holder.galleryVideo = (VideoView) convertView.findViewById(R.id.galleryImage);
                    holder.gallery_title = (TextView) convertView.findViewById(R.id.gallery_title);
                    holder.galleryVideo.setId(position);
                    holder.gallery_title.setId(position);
                }
            }
            else
            {
                convertView = LayoutInflater.from(activity).inflate(
                        R.layout.album_layout, parent, false);

                holder.galleryImage = (ImageView) convertView.findViewById(R.id.galleryImage);
                holder.gallery_count = (TextView) convertView.findViewById(R.id.gallery_count);
                holder.gallery_title = (TextView) convertView.findViewById(R.id.gallery_title);
                holder.galleryImage.setId(position);
                holder.gallery_title.setId(position);
                holder.gallery_count.setId(position);
            }

            convertView.setTag(holder);
        } else {
            holder = (AlbumViewHolder) convertView.getTag();
        }
        try {

            if(tipo.equals(Function.ALBUM))
                holder.gallery_count.setText(song.get(Function.KEY_COUNT));
            holder.gallery_title.setText(song.get(Function.KEY_ALBUM));

            String path = song.get(Function.KEY_PATH);

            if(path.equals(""))
                Glide.with(activity)
                        .load(R.drawable.folder) // Uri of the picture
                        .into(holder.galleryImage);
            else
            {
                if(tipo_contenido.equals(Function.VIDEO_TYPE))
                {
                    //* para video
                    String videoFile = path;
                    Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoFile,
                            MediaStore.Images.Thumbnails.MINI_KIND);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(thumbnail);
                    holder.galleryVideo.setBackgroundDrawable(bitmapDrawable);
                    //*/
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



class AlbumViewHolder {
    ImageView galleryImage;
    VideoView galleryVideo;
    TextView gallery_count, gallery_title;
}
