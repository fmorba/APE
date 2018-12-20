package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class iu_archivos extends AppCompatActivity {
    ImageButton btnAgregarArchivo, btnObservarArchivo, btnEliminarArchivo, btnModificarArchivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_archivos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarArchivos);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();

        btnAgregarArchivo = (ImageButton) findViewById(R.id.boton_agregar_archivo);
        btnObservarArchivo = (ImageButton) findViewById(R.id.boton_ver_archivo);
        btnModificarArchivo = (ImageButton) findViewById(R.id.boton_modificar_archivo);
        btnEliminarArchivo = (ImageButton) findViewById(R.id.boton_eliminar_archivo);

        final Intent intentAgregar = new Intent(this,iu_agregar_archivo.class);
        final Intent intentModificar = new Intent(this,iu_modificar_archivo.class);

        btnAgregarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAgregar);
            }
        });

        btnObservarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnModificarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentModificar);
            }
        });

        btnEliminarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_gestion_basica, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el menU de la barra de tareas de la aplicación.
        int id = item.getItemId();

        if (id == R.id.menu_barra_agregar) {
            final Intent intentAgregar = new Intent(this,iu_agregar_archivo.class);
            startActivity(intentAgregar);
            return true;
        }
        if (id == R.id.menu_barra_observar) {
            return true;
        }
        if (id == R.id.menu_barra_modificar) {
            final Intent intentModificar = new Intent(this,iu_modificar_archivo.class);
            startActivity(intentModificar);
            return true;
        }
        if (id == R.id.menu_barra_eliminar) {
            return true;
        }
        if (id == R.id.menu_barra_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
