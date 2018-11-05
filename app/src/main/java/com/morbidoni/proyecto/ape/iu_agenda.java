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

public class iu_agenda extends AppCompatActivity {
    ImageButton btnAgregarEvento, btnModificarEvento, btnEliminarEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_agenda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAgenda);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();

        btnAgregarEvento = (ImageButton) findViewById(R.id.boton_agregar_evento);
        btnModificarEvento = (ImageButton) findViewById(R.id.boton_modificar_evento);
        btnEliminarEvento = (ImageButton) findViewById(R.id.boton_eliminar_evento);

        final Intent intentAgregar = new Intent(this,iu_agregar_eventos.class);
        final Intent intentModificar = new Intent(this, iu_modificar_eventos.class);

        btnAgregarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAgregar);
            }
        });

        btnModificarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentModificar);
            }
        });

        btnEliminarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_agenda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el meno de la barra de tareas de la aplicación.
        int id = item.getItemId();

        if (id == R.id.menu_agenda_agregar) {
            final Intent intentAgregar = new Intent(this,iu_agregar_eventos.class);
            startActivity(intentAgregar);
            return true;
        }
        if (id == R.id.menu_agenda_modificar) {
            final Intent intentModificar = new Intent(this, iu_modificar_eventos.class);
            startActivity(intentModificar);
            return true;
        }
        if (id == R.id.menu_agenda_eliminar) {
            return true;
        }
        if (id == R.id.menu_agenda_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
