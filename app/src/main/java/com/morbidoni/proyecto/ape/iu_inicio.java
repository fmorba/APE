package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import modelos.ModeloEvento;
import servicios.GestorEvento;
import servicios.GestorMateria;

/**
 * Es la clase que controla la prime interfaz con la que el usuario interactuar y que actúa como
 * entrada a la aplicación. Permite acceder a las distintas funcionalidades, y muestra un listado
 * con las actividades de día de hoy, así como un recordatorio de aquellas actividades marcadas
 * como importantes, dentro de la semana.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_inicio extends AppCompatActivity {
    ImageButton btnAgenda, btnMateria, btnExamen, btnPlanificador, btnMetrica, btnArchivo;
    ListView listRecordatorios, listActividadesHoy;
    GestorEvento gestorEvento;
    GestorMateria gestorMateria;
    String idUsuario;
    ProgressBar progressBar;
    ArrayList<ModeloEvento> listadoEventosHoy = new ArrayList<>();
    /**
     * Esta parte del programa detecta cual boton fue presionado y abre la actividad
     * correspondiente. No realiza tareas aparte del iniciado de otras ventanas.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarInicio);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getString(iu_login.EXTRA_MESSAGE);

        gestorEvento = new GestorEvento();
        gestorMateria = new GestorMateria();

        final Intent intentAgenda = new Intent(this, iu_agenda.class);
        final Intent intentMateria = new Intent(this, iu_materias.class);
        final Intent intentExamen = new Intent(this, iu_examenes.class);
        final Intent intentPlanificador = new Intent(this, iu_entrada_planificador.class);
        final Intent intentMetricas = new Intent(this, iu_metricas.class);
        final Intent intentArchivo = new Intent(this, iu_archivos.class);

        btnAgenda = (ImageButton) findViewById(R.id.boton_agenda);
        btnMateria = (ImageButton) findViewById(R.id.boton_materia);
        btnExamen = (ImageButton) findViewById(R.id.boton_examenes);
        btnPlanificador = (ImageButton) findViewById(R.id.boton_planificador);
        btnMetrica = (ImageButton) findViewById(R.id.boton_metricas);
        btnArchivo = (ImageButton) findViewById(R.id.boton_archivos);
        progressBar = (ProgressBar) findViewById(R.id.progressBarInicio);
        progressBar.setMax(10);
        listRecordatorios = (ListView) findViewById(R.id.lista_recordatorios);
        listActividadesHoy = (ListView) findViewById(R.id.lista_eventos_hoy);

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
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        Thread linea = new Thread (new Runnable() {
            @Override
            public void run() {
                final String hoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EventosDelDia(hoy);
                        Recordatorios(hoy);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        linea.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el menu de la barra de tareas de la aplicación.
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
            final Intent intentPlanificador = new Intent(this, iu_entrada_planificador.class);
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
            Intent intentConfiguracion= new Intent(this,iu_configuracion.class);
            startActivity(intentConfiguracion);
            return true;
        }
        if (id == R.id.menu_inicio_consejos) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            intentAyuda.putExtra("ayuda", "consejos");
            startActivity(intentAyuda);
            return true;
        }
        if (id == R.id.menu_inicio_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            intentAyuda.putExtra("ayuda", "inicio");
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que se encarga de encontrar aquellos eventos marcados para recordar en la próxima
     * semana, mediante el gestor de eventos, y mostrar los resultados encontrados en el ListView
     * correspondiente.
     *
     * @param hoy String conteniendo la fecha actual.
     */
    public void Recordatorios(String hoy){
        ArrayList<String> array = new ArrayList<>();
        array = gestorEvento.obtenerRecordatorios(hoy);
        if (array!=null) {
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listRecordatorios.setAdapter(itemsAdapter);
        }else {
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listRecordatorios.setAdapter(itemsAdapter);
        }
    }

    /**
     * Método que muestra al usuario los eventos registrados para el día de hoy, así como las
     * materias a cursar, mediante la utilización de los distintos gestores de datos. Avisa si hay
     * registrado exámenes o planes de estudio para el día de hoy.
     *
     * @param hoy String conteniendo la fecha actual.
     */
    public void EventosDelDia(String hoy){
        boolean hayExamen = false;
        boolean hayPlanDeEstudio =false;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        listadoEventosHoy=gestorEvento.obtenerEventosSegunFechas(hoy);
        if (listadoEventosHoy!=null) {
            ArrayList<String> array1 = new ArrayList<>();
            ArrayList<String> array2 = new ArrayList<>();
            array1=gestorMateria.obtenerListadoMateriasHorariosPorDia(sdf.format(new Date()));
            if (array1!=null){
                array2.addAll(array1);
            }
            for (ModeloEvento modelo:listadoEventosHoy) {
                array2.add(modelo.getNombre()+" - "+modelo.getHoraInicio()+" - "+modelo.getHoraFin());
                if (modelo.getTipo().equals(getResources().getString(R.string.evento_tipo_examen))){
                    hayExamen=true;
                }
                if (modelo.getTipo().equals(getResources().getString(R.string.evento_tipo_plan))){
                    hayPlanDeEstudio=true;
                }
            }
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array2);
            itemsAdapter.notifyDataSetChanged();
            listActividadesHoy.setAdapter(itemsAdapter);
        }else {
            ArrayList<String> array = new ArrayList<>();
            array = gestorMateria.obtenerListadoMateriasHorariosPorDia(sdf.format(new Date()));
            if (array == null) {
                listActividadesHoy.setAdapter(null);
            } else{
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listActividadesHoy.setAdapter(itemsAdapter);
        }
        }
        if (hayExamen){
            Toast.makeText(this, getResources().getString(R.string.mensaje_examenes_hoy_nota), Toast.LENGTH_SHORT).show();
        }
        if (hayPlanDeEstudio){
            Toast.makeText(this, getResources().getString(R.string.mensaje_plan_de_estudio_hoy), Toast.LENGTH_SHORT).show();
        }
    }
}