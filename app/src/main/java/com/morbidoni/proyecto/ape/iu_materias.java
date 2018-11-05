package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class iu_materias extends AppCompatActivity {
    ImageButton btnAgregarMateria, btnVerMateria, btnEliminarMateria, btnModificarMateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_materias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMaterias);
        setSupportActionBar(toolbar);

        final Intent intent= getIntent();

        btnAgregarMateria = (ImageButton) findViewById(R.id.boton_agregar_materia);
        btnVerMateria = (ImageButton) findViewById(R.id.boton_ver_materia);
        btnModificarMateria = (ImageButton) findViewById(R.id.boton_modificar_materia);
        btnEliminarMateria = (ImageButton) findViewById(R.id.boton_eliminar_materia);

        final Intent intentAgregarMateria = new Intent(this,iu_agregar_materia.class);
        final Intent intentModificarMateria = new Intent(this,iu_modificar_materia.class);
        final Intent intentDatosMateria = new Intent(this,iu_datos_materia.class);

        btnAgregarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAgregarMateria);
            }
        });

        btnVerMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentDatosMateria);
            }
        });

        btnModificarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentModificarMateria);
            }
        });

        btnEliminarMateria.setOnClickListener(new View.OnClickListener() {
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
            final Intent intentAgregarMateria = new Intent(this,iu_agregar_materia.class);
            startActivity(intentAgregarMateria);
            return true;
        }
        if (id == R.id.menu_barra_observar) {
            final Intent intentDatosMateria = new Intent(this,iu_datos_materia.class);
            startActivity(intentDatosMateria);
            return true;
        }
        if (id == R.id.menu_barra_modificar) {
            final Intent intentModificarMateria = new Intent(this,iu_modificar_materia.class);
            startActivity(intentModificarMateria);
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
