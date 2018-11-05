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
import android.widget.Button;

public class iu_metricas extends AppCompatActivity {
    Button btnPromedio, btnMaterias, btnExamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_metricas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMetricas);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();

        btnPromedio = (Button) findViewById(R.id.boton_promedio);
        btnMaterias = (Button) findViewById(R.id.boton_datos_materia);
        btnExamen = (Button) findViewById(R.id.boton_datos_examen);

        final Intent intentPromedio = new Intent(this,iu_promedio_especifico.class);
        final Intent intentMaterias = new Intent(this, iu_metricas_materias.class);
        final Intent intentExamen = new Intent(this,iu_metricas_examenes.class);

        btnPromedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentPromedio);
            }
        });

        btnMaterias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentMaterias);
            }
        });

        btnExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentExamen);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_metricas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el meno de la barra de tareas de la aplicación.
        int id = item.getItemId();

        if (id == R.id.menu_metri_promedio) {
            final Intent intentPromedio = new Intent(this,iu_promedio_especifico.class);
            startActivity(intentPromedio);
            return true;
        }
        if (id == R.id.menu_metri_datos_materia) {
            final Intent intentMaterias = new Intent(this, iu_metricas_materias.class);
            startActivity(intentMaterias);
            return true;
        }
        if (id == R.id.menu_metri_datos_examen) {
            final Intent intentExamen = new Intent(this,iu_metricas_examenes.class);
            startActivity(intentExamen);
            return true;
        }
        if (id == R.id.menu_metri_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
