package com.example.jeiro.organizapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.add_video);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), capturar_video.class);
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

                            if(datos.obtener_album(getActivity(), album) != null)
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
                                    cargar_grid_view();
                                    set_adapter();
                                    dialog.dismiss();
                                }
                                else
                                {
                                    Function.delete_album(path, album.getNombre());
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
        getActivity().setTitle(MainActivity.usuario_activo.getNombre());

        //Toast.makeText(getActivity(),"todo parece bien", Toast.LENGTH_SHORT).show();
        cargar_grid_view();
        set_adapter();
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

    ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();

    public void cargar_grid_view()
    {
        albumList.clear();
        //Toast.makeText(getActivity(),"Entra", Toast.LENGTH_SHORT).show();
        //sleep();
        datos_album d_album              = new datos_album();
        datos_contenido d_contenido      = new datos_contenido();
        Album album = new Album("", MainActivity.padre,MainActivity.usuario_activo.getUsuario());
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
            ArrayList<Contenido> content = d_contenido.obtener_contenido_por_album(getActivity(),temp);
            if(content.size() != 0)
                path = MainActivity.root_usuario + d_album.obtener_album_path(getActivity(),new Album(content.get(0).getPadre(),"","")) + File.separator + content.get(0).getNombre();
            else
                path = "";                                         // 2
            // File acr = new File(Opciones_menu.root_usuario + path, temp.getNombre());

            countPhoto = Integer.toString(content.size());     // 3
            tipo = Function.ALBUM;                                            // 4
            tipo_contenido = "";                                   // 5

            albumList.add(Function.mappingInbox(name, path, countPhoto, tipo, tipo_contenido));
        }

        // contenido
        for(int i = 0; i < l_contenido.size(); i++)
        {
            Contenido temp = l_contenido.get(i);
            name = temp.getNombre();
            path = MainActivity.root_usuario + d_album.obtener_album_path(getActivity(),new Album(temp.getPadre(),"",MainActivity.usuario_activo.getUsuario()))+ File.separator + temp.getNombre();
            tipo = Function.CONTENIDO;
            countPhoto = "";
            tipo_contenido = temp.getTipo();
            albumList.add(Function.mappingInbox(name, path, countPhoto, tipo, tipo_contenido));
        }
        //*/
    }

    private void set_adapter()
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
                if(view_id == Function.ID_ALBUM)
                {
                    AlbumViewHolder a = (AlbumViewHolder)view.getTag();
                    a.gallery_title.getText();
                    MainActivity.padre = a.gallery_title.getText().toString();
                    cargar_grid_view();
                    set_adapter();
                    getActivity().setTitle(MainActivity.padre);
                }
                else if(view_id == Function.ID_IMAGE)
                {
                    Intent intent = new Intent(getActivity(), GalleryPreview.class);
                    intent.putExtra("path", albumList.get(+position).get(Function.KEY_PATH));
                    startActivity(intent);
                }
                //Toast.makeText(getActivity(),"view_id " + view.getId() + " - " + id , Toast.LENGTH_SHORT).show();
            }
        });
        galleryGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id)
            {
                int view_id = view.getId();
                if(view_id == 3)
                {
                    AlbumViewHolder a = (AlbumViewHolder)view.getTag();
                    MainActivity.string_temporal = a.gallery_title.getText().toString();
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_editar_eliminar);
                    TextView txt = (TextView) dialog.findViewById(R.id.dialog_title);
                    txt.setText("Álbum opciones");

                    Button btn1 = (Button)dialog.findViewById(R.id.btnEditar);
                    btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                        EditText text = (EditText) dialog.findViewById(R.id.txtName);
                        try {
                            //*
                            String inputText = text.getText().toString();
                            if (inputText.equals("")) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_nuevo_nombre_vacio),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(getActivity(), getResources().getString(R.string.toast_name_dialog)  + inputText,
                                    Toast.LENGTH_SHORT).show();

                            datos_album datos = new datos_album();
                            Album album_anterior = new Album(MainActivity.padre, MainActivity.string_temporal, MainActivity.usuario_activo.getUsuario());
                            Album album_nuevo = datos.obtener_album(getActivity(), new Album(MainActivity.padre, MainActivity.string_temporal, MainActivity.usuario_activo.getUsuario()));
                            album_nuevo.setNombre(inputText);

                            if (datos.obtener_album(getActivity(), album_nuevo) != null) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_nombre_uso),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //*
                            String path = MainActivity.root_usuario + File.separator + datos.obtener_album_path(getActivity(), album_anterior);
                            if (Function.rename_album(path, album_anterior.getNombre(), album_nuevo.getNombre())) {
                                if (datos.rename_album(album_nuevo,album_anterior,getActivity()))
                                {
                                    MainActivity.string_temporal = null;
                                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_se_creo_album), Toast.LENGTH_SHORT).show();
                                    cargar_grid_view();
                                    set_adapter();
                                    dialog.dismiss();
                                }
                                else {
                                    Function.delete_album(path, album_anterior.getNombre());
                                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_se_creo_album), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(getContext(), getResources().getString(R.string.toast_ya_existe_album), Toast.LENGTH_SHORT).show();
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

                    Button btn2 = (Button)dialog.findViewById(R.id.btnEliminar);
                    btn2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            try
                            {
                                datos_album datos = new datos_album();
                                Album temp = datos.obtener_album(getActivity(),new Album(MainActivity.padre,MainActivity.string_temporal,MainActivity.usuario_activo.getUsuario()));
                                String path = MainActivity.root_usuario + File.separator + datos.obtener_album_path(getActivity(), temp);
                                if(Function.delete_album(path,temp.getNombre()))
                                {
                                    if (datos.eliminar_album(temp,getActivity()))
                                    {
                                        MainActivity.string_temporal = null;
                                        Toast.makeText(getActivity(),getResources().getString(R.string.toast_se_elimino_album), Toast.LENGTH_SHORT).show();
                                        cargar_grid_view();
                                        set_adapter();
                                        dialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(getActivity(),getResources().getString(R.string.toast_no_posible_eliminar_album), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getContext(),getResources().getString(R.string.toast_no_existe), Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_error) + e.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
                else
                    {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_editar_eliminar_mover_compartir);
                    dialog.show();

                    final Button btn_reset = (Button) dialog.findViewById(R.id.button);
                    btn_reset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });

                    final Button btn_ingresos = (Button) dialog.findViewById(R.id.button2);
                    btn_ingresos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    final Button btn_incluir_categorias = (Button) dialog.findViewById(R.id.button3);
                    btn_incluir_categorias.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });

                    final Button btn_compartir_multimedia = (Button) dialog.findViewById(R.id.button4);
                    btn_compartir_multimedia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //  AQUI VA EL PATH DE LA IMAGEN ESTO ES UNA IMAGEN QUEMADA XQ ESTOY PRBANDO
                            Uri imagen= Uri.parse("android.resource:// drawable /" + Integer.toString(R.drawable.album));
                            //
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            String shareBody = "Dato principal, es el titulo";
                            String shareSub = "Asunto, detalles";
                            intent.putExtra(Intent.EXTRA_STREAM,imagen);
                            intent.putExtra(Intent.EXTRA_TEXT, "Comentarios");
                            startActivity(Intent.createChooser(intent, "Compartir Datos"));
                        }
                    });
                }
                return true;
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

                    convertView.setId(Function.ID_IMAGE);

                    holder.galleryImage = (ImageView) convertView.findViewById(R.id.galleryImage);
                    holder.galleryImage.setId(position);
                }
                else
                {
                    convertView = LayoutInflater.from(activity).inflate(
                            R.layout.video_layout, parent, false);

                    convertView.setId(Function.ID_VIDEO);

                    holder.galleryVideo = (VideoView) convertView.findViewById(R.id.galleryImage);
                    holder.galleryVideo.setId(position);
                }
            }
            else
            {
                convertView = LayoutInflater.from(activity).inflate(
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
        } else {
            holder = (AlbumViewHolder) convertView.getTag();
        }
        try {

            if(tipo.equals(Function.ALBUM))
            {
                holder.gallery_count.setText("Archivos " + song.get(Function.KEY_COUNT));
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
