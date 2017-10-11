package com.example.jeiro.organizapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.jeiro.organizapp.Datos.*;
import com.example.jeiro.organizapp.Modelo.*;

import java.io.File;

public class Opciones_menu extends AppCompatActivity implements InputNameDialogFragment.InputNameDialogListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private static int seccion_actual;
    public static String root_usuario;
    public static String padre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Toast.makeText(this,"Ruta " + MainActivity.root , Toast.LENGTH_SHORT).show();
        setTitle(MainActivity.usuario_activo.getNombre());

        //*
        File carpetaContenedora = new File(MainActivity.root, MainActivity.usuario_activo.getUsuario());
        if (Function.crear_album(MainActivity.root, MainActivity.usuario_activo.getUsuario()))
        {
            Toast.makeText(this,"Carpeta base creada", Toast.LENGTH_SHORT).show();
        }
        root_usuario = carpetaContenedora.getAbsolutePath().toString();

        Toast.makeText(this,"Ruta " + root_usuario, Toast.LENGTH_SHORT).show();
        //*/

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position)
            {
                seccion_actual = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seccion_actual == 1)
                {
                    btnShowDialog(view);
                    Snackbar.make(view, "Éxito, se ha creado un álbum", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else
                    Snackbar.make(view, "Error, no se puede crear un álbum", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    public void btnShowDialog(View view) {
        showInputNameDialog();
    }

    private void showInputNameDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        InputNameDialogFragment inputNameDialog = new InputNameDialogFragment();
        inputNameDialog.setCancelable(false);
        inputNameDialog.setDialogTitle("Nuevo álbum");
        inputNameDialog.show(fragmentManager, "Input Dialog");
    }

    @Override
    public void onFinishInputDialog(String inputText) {
        try {

            //*
            if (inputText.equals(""))
            {
                Toast.makeText(this, "Error, nombre del nuevo álbum vacío",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            datos_album datos = new datos_album();
            Album album = new Album(padre,inputText,MainActivity.usuario_activo.getUsuario());
            Toast.makeText(this, "Input Name to dialog: " + inputText,
                    Toast.LENGTH_SHORT).show();
            String path = root_usuario + datos.obtener_album_path(this, album);
            //*
            if(Function.crear_album(path, album.getNombre()))
            {
                if (datos.insertar_album(album, true, this))
                    Toast.makeText(this, "Éxito, se ha creado un nuevo álbum", Toast.LENGTH_SHORT).show();
                else
                {
                    Function.delete_album(path, album.getNombre());
                    Toast.makeText(this, "Error, no ha sido posible crear el álbum", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "Error, el álbum ya existe", Toast.LENGTH_SHORT).show();
            }
            //*/
        }
        catch (Exception e)
        {
            Toast.makeText(this,"Error, " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opciones_menu, menu);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Fragment_capturas.newInstance("Galería");
                case 1:
                    return Fragment_albumes.newInstance("Álbumes");
                default:
                    return Fragment_albumes.newInstance("Álbumes");
            }
        }

        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Fotos";
                case 1:
                    return "Albumes";
            }
            return null;
        }
    }
}
