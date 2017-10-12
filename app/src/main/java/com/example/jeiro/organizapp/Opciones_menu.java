package com.example.jeiro.organizapp;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import java.io.File;

public class Opciones_menu extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

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
            Toast.makeText(this,getResources().getString(R.string.toast_carpeta_creada), Toast.LENGTH_SHORT).show();
        }
        MainActivity.root_usuario = carpetaContenedora.getAbsolutePath().toString();

        //Toast.makeText(this,"Ruta " + root_usuario, Toast.LENGTH_SHORT).show();
        //*/

        set_view_adapter();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void set_view_adapter()
    {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
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
