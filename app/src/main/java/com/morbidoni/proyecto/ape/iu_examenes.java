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

public class iu_examenes extends AppCompatActivity {
    ImageButton btnAgregarExamen, btnObservarExamen, btnModificarExamen, btnEliminarExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_examenes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarExamenes);
        setSupportActionBar(toolbar);

        final Intent intent= getIntent();

        btnAgregarExamen = (ImageButton) findViewById(R.id.boton_agregar_examen);
        btnObservarExamen = (ImageButton) findViewById(R.id.boton_ver_examen);
        btnModificarExamen = (ImageButton) findViewById(R.id.boton_modificar_examen);
        btnEliminarExamen = (ImageButton) findViewById(R.id.boton_eliminar_examen);

        final Intent intentAgregarExamen = new Intent(this, iu_agregar_examen.class);
        final Intent intentModificarExamen = new Intent(this, iu_modificar_examen.class);
        final Intent intentObservarExamen = new Intent(this,iu_datos_examenes.class);

        btnAgregarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAgregarExamen);
            }
        });

        btnObservarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentObservarExamen);
            }
        });

        btnModificarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentModificarExamen);
            }
        });

        btnEliminarExamen.setOnClickListener(new View.OnClickListener() {
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
            final Intent intentAgregarExamen = new Intent(this, iu_agregar_examen.class);
            startActivity(intentAgregarExamen);
            return true;
        }
        if (id == R.id.menu_barra_observar) {
            final Intent intentObservarExamen = new Intent(this,iu_datos_examenes.class);
            startActivity(intentObservarExamen);
            return true;
        }
        if (id == R.id.menu_barra_modificar) {
            final Intent intentModificarExamen = new Intent(this, iu_modificar_examen.class);
            startActivity(intentModificarExamen);
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
