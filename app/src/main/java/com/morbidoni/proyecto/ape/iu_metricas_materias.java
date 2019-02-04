package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import modelos.ModeloHorarios;
import modelos.ModeloMateria;
import servicios.GestorMateria;

/**
 * Clase que presenta la usuario un conjunto de métricas relacionadas con las materias registradas,
 * en su correspondiente interfaz.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_metricas_materias extends AppCompatActivity {
    GestorMateria gestorMateria;
    TextView txtMateriasCursadas, txtMateriasAprobadas, txtMateriasActuales;
    ListView listaMaterias;
    ArrayList<String> listado = new ArrayList<>();
    ArrayList<ModeloMateria> arrayMaterias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_metricas_materias);

        Intent intent = getIntent();

        txtMateriasActuales = (TextView) findViewById(R.id.txtCantidadMateriasCursadasActual);
        txtMateriasAprobadas = (TextView) findViewById(R.id.txtCantidadMateriasAprobadas);
        txtMateriasCursadas = (TextView) findViewById(R.id.txtCantidadMateriasCursadas);
        listaMaterias = (ListView) findViewById(R.id.listado_metricas_materias);
        gestorMateria = new GestorMateria();

        cargarListadoMaterias();
        generarMetricas();

    }

    /**
     * Método que obtiene la lista de materias registradas y genera un array de strings para su
     * presentación en la interfaz.
     */
    private void cargarListadoMaterias(){
        listado.clear();
        arrayMaterias=gestorMateria.obtenerListadoMaterias();
        if (arrayMaterias!=null) {
            for (int i = 0; i < arrayMaterias.size(); i++) {
                int horas=0;
                ArrayList<ModeloHorarios> horarios = gestorMateria.obtenerHorariosPorMateria(arrayMaterias.get(i).getIdMateria());
                for (int j = 0; j < horarios.size(); j++) {
                    int horaInicio = Integer.valueOf(horarios.get(j).getHoraInicio().split(":")[0]);
                    int horaFin = Integer.valueOf(horarios.get(j).getHoraFin().split(":")[0]);
                    horas += (horaFin-horaInicio);
                }
                String cantidadHoras = getResources().getString(R.string.cantidad_horas_materia,horas);

                listado.add(arrayMaterias.get(i).getNombre() + " - "+arrayMaterias.get(i).getDificultad() +" - " +cantidadHoras+" - " + arrayMaterias.get(i).getEstado());
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
            listaMaterias.setAdapter(itemsAdapter);
        }
    }

    /**
     * Método que genera las métricas que luego se colocaran en la interfaz gráficas, mediante la
     * utilización de los TextView correspondientes.
     */
    private void generarMetricas(){
        int materiasCursadas=0, materiasAprobadas=0, materiasActuales=0;
        materiasCursadas=arrayMaterias.size();
        for (ModeloMateria materia:arrayMaterias) {
            if (materia.getEstado().equals("Cursando")){
                materiasActuales++;
            }
            if (materia.getEstado().equals("Aprobada")){
                materiasAprobadas++;
            }
        }
        txtMateriasCursadas.setText(getResources().getString(R.string.materias_cursadas,materiasCursadas));
        txtMateriasAprobadas.setText(getResources().getString(R.string.materias_aprobadas,materiasAprobadas));
        txtMateriasActuales.setText(getResources().getString(R.string.materias_cursadas_actualmente,materiasActuales));

    }
}
