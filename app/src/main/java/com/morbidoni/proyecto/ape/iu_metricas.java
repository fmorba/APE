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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import modelos.ModeloArchivo;
import modelos.ModeloExamen;
import modelos.ModeloHorarios;
import modelos.ModeloMateria;
import modelos.ModeloPlanEstudio;
import modelos.ModeloPlanificacion;
import servicios.GestorArchivos;
import servicios.GestorExamen;
import servicios.GestorMateria;
import servicios.GestorPlanificador;

/**
 * Esta clase controla la interfaz donde se presentan al usuario, distintas métricas dependiendo de
 * los datos almacenados en la agenda, mediante la utilización de los distintos gestores de datos.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_metricas extends AppCompatActivity {
    Button btnPromedio, btnMaterias, btnExamen;
    GestorExamen gestorExamen;
    GestorMateria gestorMateria;
    GestorPlanificador gestorPlanificador;
    GestorArchivos gestorArchivos;
    ArrayList<ModeloExamen> listadoExamenes;
    ProgressBar progressBar;
    TextView txtNotaAlta, txtnNotaBaja, txtMMateria, txtPMateria, txtMTMateria, txtPTMateria, txtExamenesCursados, txtHorasEstudio, txtHorasCursado, txtPromedioGeneral, txtExamenesAnuales, txtCantidadArchivos;
    float mejorNota=0, promedioHorasSemanales=0, peorNota=999, promedioGeneral=0;
    String mejorMateria="", peorMateria="", mejorTipoMateria="", peorTipoMateria="";
    int cantidadExamenesCursados=0, cantidadHorasCursado=0, cantidadExamenesEsteAño=0, cantidadArchivosRegistrados=0;

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
        progressBar = (ProgressBar) findViewById(R.id.progressBarMetricas);
        progressBar.setMax(10);
        progressBar.setVisibility(View.VISIBLE);
        txtNotaAlta = (TextView) findViewById(R.id.metri_NotaAlta);
        txtnNotaBaja = (TextView) findViewById(R.id.metri_NotaBaja);
        txtMMateria = (TextView) findViewById(R.id.metri_mejorMateria);
        txtPMateria = (TextView) findViewById(R.id.metri_PeorMateria);
        txtMTMateria = (TextView) findViewById(R.id.metri_MejorTipo);
        txtPTMateria = (TextView) findViewById(R.id.metri_PeorTipo);
        txtExamenesCursados = (TextView) findViewById(R.id.metri_ExamenesCursados);
        txtExamenesAnuales = (TextView) findViewById(R.id.metri_ExamenesAnuales);
        txtHorasEstudio = (TextView) findViewById(R.id.metri_HorasEstudio);
        txtHorasCursado = (TextView) findViewById(R.id.metri_HorasCursado);
        txtCantidadArchivos = (TextView) findViewById(R.id.metri_cantidadArchivos);
        txtPromedioGeneral = (TextView) findViewById(R.id.metri_PromedioGeneral);

        final Intent intentPromedio = new Intent(this,iu_promedio_especifico.class);
        final Intent intentMaterias = new Intent(this, iu_metricas_materias.class);
        final Intent intentExamen = new Intent(this,iu_metricas_examenes.class);

        gestorExamen= new GestorExamen();
        gestorMateria = new GestorMateria();
        gestorPlanificador = new GestorPlanificador();
        gestorArchivos = new GestorArchivos();

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

        Thread linea = new Thread (new Runnable() {
            @Override
            public void run() {
                generarMetricas();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        actualizarPantalla();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        linea.start();

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
            intentAyuda.putExtra("ayuda", "metricas");
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que genera métricas en base a los datos aportados por les gestores.
     */
    private void generarMetricas(){
        listadoExamenes = gestorExamen.obtenerListadoExamenes();
        if(listadoExamenes!=null) {
            ArrayList<ModeloMateria> listadoMaterias = gestorMateria.obtenerListadoMaterias();
            ArrayList<ModeloPlanificacion> listadoPlanificaciones = gestorPlanificador.obtenerListadoPlanificaciones();
            ArrayList<ModeloArchivo> listadoArchivos = gestorArchivos.obtenerListadoArchivos();

            float auxNota, auxProme = 0, promedioNuevo, promedioPeor = 999, promedioMejor = 0, sumaNotas = 0;
            int cantidadExamenes = 0, horasTotalesPlanificaciones = 0, planesRevisados = 0;

            for (ModeloExamen examen : listadoExamenes) {
                if (examen.getResultado().equals("") == false) {
                    cantidadExamenesCursados++;
                    auxNota = Float.valueOf(examen.getResultado());
                    auxProme += auxNota;

                    if (auxNota > mejorNota) {
                        mejorNota = auxNota;
                    }
                    if (auxNota < peorNota) {
                        peorNota = auxNota;
                    }

                    Calendar calendar = Calendar.getInstance();
                    String añoActual = calendar.get(Calendar.YEAR) + "";
                    String añosExamen = examen.getFecha().split("-")[0];

                    if (añosExamen.equals(añoActual)) {
                        cantidadExamenesEsteAño++;
                    }
                }
            }
            if (peorNota == 999) peorNota = 0;
            promedioGeneral = auxProme / cantidadExamenesCursados;
            if (listadoMaterias!=null){
                for (ModeloMateria materia : listadoMaterias) {
                    ArrayList<ModeloExamen> examenAuxiliar = gestorExamen.obtenerListadoExamenesPorMateria(materia.getNombre());
                    if (examenAuxiliar!=null) {
                        for (ModeloExamen examen : examenAuxiliar) {
                            sumaNotas += Float.valueOf(examen.getResultado());
                            cantidadExamenes++;
                        }
                        promedioNuevo = sumaNotas / cantidadExamenes;
                        sumaNotas = 0;
                        cantidadExamenes = 0;
                        if (promedioNuevo < promedioPeor) {
                            promedioPeor = promedioNuevo;
                            peorMateria = materia.getNombre();
                            peorTipoMateria = materia.getTipo();
                        }
                        if (promedioNuevo > promedioMejor) {
                            promedioMejor = promedioNuevo;
                            mejorMateria = materia.getNombre();
                            mejorTipoMateria = materia.getTipo();
                        }
                        if (materia.getEstado().equals("Cursando")) {
                            ArrayList<ModeloHorarios> horarios = materia.getHorarios();
                            for (ModeloHorarios hora : horarios) {
                                cantidadHorasCursado += (Integer.valueOf(hora.getHoraFin()) - Integer.valueOf(hora.getHoraInicio()));
                            }
                        }
                    }
                }
            }
            if (listadoPlanificaciones!=null) {
                for (ModeloPlanificacion plan : listadoPlanificaciones) {
                    if (plan.getHoras() != 0) {
                        horasTotalesPlanificaciones += plan.getHoras();
                        planesRevisados++;
                    }
                }
                if (planesRevisados != 0) {
                    promedioHorasSemanales = horasTotalesPlanificaciones / planesRevisados;
                }
                if (listadoArchivos.isEmpty() == false) {
                    cantidadArchivosRegistrados = listadoArchivos.size();
                }
            }
        }else{
            peorNota=0;
        }
    }

    /**
     * Una vez generadas las métricas, este método actualiza los TextView en la interfaz gráfica,
     * para que el usuario pueda observar los resultados obtenidos.
     */
    private void actualizarPantalla(){
        txtNotaAlta.setText(getResources().getString(R.string.nota_mas_alta,mejorNota));
        txtnNotaBaja.setText(getResources().getString(R.string.nota_mas_baja,peorNota));
        txtMMateria.setText(getResources().getString(R.string.mejor_materia,mejorMateria));
        txtPMateria.setText(getResources().getString(R.string.peor_materia,peorMateria));
        txtMTMateria.setText(getResources().getString(R.string.mejor_tipo_materia,mejorTipoMateria));
        txtPTMateria.setText(getResources().getString(R.string.peor_tipo_materia,peorTipoMateria));
        txtExamenesCursados.setText(getResources().getString(R.string.examenes_cursados,cantidadExamenesCursados));
        txtHorasEstudio.setText(getResources().getString(R.string.horas_estudio,promedioHorasSemanales));
        txtHorasCursado.setText(getResources().getString(R.string.horas_cursado,cantidadHorasCursado));
        txtPromedioGeneral.setText(getResources().getString(R.string.promedio_general,promedioGeneral));
        txtCantidadArchivos.setText(getResources().getString(R.string.archivos_registrados,cantidadArchivosRegistrados));
        txtExamenesAnuales.setText(getResources().getString(R.string.examenes_anual,cantidadExamenesEsteAño));
    }

}
