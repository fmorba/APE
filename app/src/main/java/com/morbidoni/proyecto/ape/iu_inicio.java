package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;

public class iu_inicio extends AppCompatActivity {
    ImageButton btnAgenda, btnMateria, btnExamen, btnPlanificador, btnMetrica, btnArchivo;

    //Esta parte del programa detecta cual boton fue presionado y abre la actividad correspondiente.
    //No realiza tareas aparte del iniciado de otras ventanas.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarInicio);
        setSupportActionBar(toolbar);

        final Intent intentAgenda = new Intent(this, iu_agenda.class);
        final Intent intentMateria = new Intent(this, iu_materias.class);
        final Intent intentExamen = new Intent(this,iu_examenes.class);
        final Intent intentPlanificador = new Intent(this, iu_planificador.class);
        final Intent intentMetricas = new Intent(this,iu_metricas.class);
        final Intent intentArchivo = new Intent(this, iu_archivos.class);

        btnAgenda = (ImageButton) findViewById(R.id.boton_agenda);
        btnMateria = (ImageButton) findViewById(R.id.boton_materia);
        btnExamen = (ImageButton) findViewById(R.id.boton_examenes);
        btnPlanificador = (ImageButton) findViewById(R.id.boton_planificador);
        btnMetrica = (ImageButton) findViewById(R.id.boton_metricas);
        btnArchivo = (ImageButton) findViewById(R.id.boton_archivos);


        btnAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAgenda);
            }
        });

        btnMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentMateria);
            }
        });

        btnExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentExamen);
            }
        });

        btnPlanificador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentPlanificador);
            }
        });

        btnMetrica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentMetricas);
            }
        });

        btnArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentArchivo);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el meno de la barra de tareas de la aplicación.
        int id = item.getItemId();

        if (id == R.id.menu_inicio_agenda) {
            final Intent intentAgenda = new Intent(this, iu_agenda.class);
            startActivity(intentAgenda);
            return true;
        }
        if (id == R.id.menu_inicio_materias) {
            final Intent intentMateria = new Intent(this, iu_materias.class);
            startActivity(intentMateria);
            return true;
        }
        if (id == R.id.menu_inicio_examen) {
            final Intent intentExamen = new Intent(this,iu_examenes.class);
            startActivity(intentExamen);
            return true;
        }
        if (id == R.id.menu_inicio_planificador) {
            final Intent intentPlanificador = new Intent(this, iu_planificador.class);
            startActivity(intentPlanificador);
            return true;
        }
        if (id == R.id.menu_inicio_metricas) {
            final Intent intentMetricas = new Intent(this,iu_metricas.class);
            startActivity(intentMetricas);
            return true;
        }
        if (id == R.id.menu_inicio_archivos) {
            final Intent intentArchivo = new Intent(this, iu_archivos.class);
            startActivity(intentArchivo);
            return true;
        }
        if (id == R.id.menu_inicio_configuracion) {
            final Intent intentConfiguracion= new Intent(this,iu_configuracion.class);
            startActivity(intentConfiguracion);
            return true;
        }
        if (id == R.id.menu_inicio_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
