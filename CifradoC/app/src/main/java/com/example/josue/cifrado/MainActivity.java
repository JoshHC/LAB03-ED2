package com.example.josue.cifrado;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Ruta del Archivo
    String Ruta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            final CifradoZigZag ExtraerDatos = new CifradoZigZag();
            //Se Extraen los datos Leidos de la Estructura para fijarlos en la Actividad
            List<String> Lista = ExtraerDatos.EnviarNombres();

            final String[] ListaNombres = new String[Lista.size()];
            int contador = 0;
            for(String i: Lista)
            {
                ListaNombres[contador]= i;
                contador++;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Selecciona la Ruta para Guardar tu Cifrado");

            int checkedItem = 1; // cow
            builder.setSingleChoiceItems(ListaNombres, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // user checked an item
                }
            });

            builder.setItems(ListaNombres,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int item)
                        {
                            Toast.makeText(getApplicationContext(),"Has Elegido Guardar tu Cifrado en: " + ListaNombres[item], Toast.LENGTH_SHORT).show();
                            //Se envian los datos a los Fragments
                            ExtraerDatos.RecibirRuta(ListaNombres[item]);
                            Ruta = ListaNombres[item];
                        }
                    });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.setNegativeButton("Cancel", null);


            AlertDialog dialog = builder.create();
            dialog.show();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_camera) {
            TextView Instrucciones = findViewById(R.id.textinstrucciones);
            Instrucciones.setEnabled(false);
            Instrucciones.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction().replace(R.id.contenedor, new ProcesoCifradoZigZag()).commit();
        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new CifradoEspiral()).commit();
        } else if (id == R.id.nav_slideshow) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new CifradoCesar()).commit();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
