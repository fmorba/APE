package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import modelos.ModeloExamen;
import servicios.GestorExamen;

/**
 * Clase que presenta la usuario un conjunto de métricas relacionadas con los exámenes registrados,
 * en su correspondiente interfaz.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_metricas_examenes extends AppCompatActivity {
    GestorExamen gestorExamen;
    ListView listaExamenes;
    TextView txtTotalExamenes, txtExamenesAprobados, txtExamenesDesaprobados, txtExamenesEsteAño;
    ArrayList<String>listado=new ArrayList<>();
    ArrayList<ModeloExamen> arrayExamenes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_metricas_examenes);

        Intent intent = getIntent();

        listaExamenes = (ListView) findViewById(R.id.listado_metricas_examenes);
        txtTotalExamenes = (TextView) findViewById(R.id.txtCantidadTotalExamenes);
        txtExamenesAprobados = (TextView) findViewById(R.id.metri_ExamenesAprobados);
        txtExamenesDesaprobados = (TextView) findViewById(R.id.metri_ExamenesDesaprobados);
        txtExamenesEsteAño = (TextView) findViewById(R.id.metri_cantidadExamenesAnuales);
        gestorExamen = new GestorExamen();

        cargarListadoExamenes();
        generarMetricas();
    }

    /**
     * Método que obtiene la lista de exámenes registrados y genera un array de strings para su
     * presentación en la interfaz.
     */
    private void cargarListadoExamenes(){
        listado.clear();
        arrayExamenes = gestorExamen.obtenerListadoExamenes();
        if (arrayExamenes!=null) {
            for (int i = 0; i < arrayExamenes.size(); i++) {

                listado.add(arrayExamenes.get(i).getFecha()+" - "+arrayExamenes.get(i).getMateria()+" - Resultado:"+ arrayExamenes.get(i).getResultado());
            }
            controlListView(listado);
        }
    }

    /**
     * Esta clase controla la lista de datos que se muestra en la ventana del usuario, y realiza
     * las actualizaciones pertinentes de la misma.
     *
     * @param array Array de Strings que necesitan ser ingresados en el ListView.
     */
    private void controlListView(ArrayList<String> array){
        if (array!=null) {
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listaExamenes.setAdapter(itemsAdapter);
        }
    }

    /**
     * Método que genera las métricas que luego se colocaran en la interfaz gráficas, mediante la
     * utilización de los TextView correspondientes.
     */
    private void generarMetricas(){
        int examenesCursados=0, examenesAprobados=0, examenesDesaprobados=0,examenesAnuales=0;
        examenesCursados=arrayExamenes.size();

        for (ModeloExamen examen:arrayExamenes) {
            Calendar calendar = Calendar.getInstance();
            String añoActual = calendar.get(Calendar.YEAR)+"";
            String añosExamen = examen.getFecha().split("-")[0];

            if (añosExamen.equals(añoActual)){
                examenesAnuales++;
            }
            if (examen.getResultado().equals("")==false) {
                float aux = Float.valueOf(examen.getResultado());
                if (aux >= 6) {
                    examenesAprobados++;
                } else {
                    examenesDesaprobados++;
                }
            }
        }

        txtTotalExamenes.setText(getResources().getString(R.string.examenes_cursados,examenesCursados));
        txtExamenesAprobados.setText(getResources().getString(R.string.examenes_aprobados,examenesAprobados));
        txtExamenesDesaprobados.setText(getResources().getString(R.string.examenes_desaprobados,examenesDesaprobados));;
        txtExamenesEsteAño.setText(getResources().getString(R.string.examenes_anual,examenesAnuales));
    }
}
