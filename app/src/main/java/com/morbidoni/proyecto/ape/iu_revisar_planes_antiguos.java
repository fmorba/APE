package com.morbidoni.proyecto.ape;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import modelos.ModeloExamen;
import modelos.ModeloMateria;
import modelos.ModeloPlanificacion;
import servicios.GestorExamen;
import servicios.GestorMateria;
import servicios.GestorPlanificador;

/**
 * Clase que controla la interfaz de usuario, dedicada al gestionamiento de los registro de las
 * planificaciones terminadas.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_revisar_planes_antiguos extends AppCompatActivity {
    Button btnEliminar, btnModificarHoras;
    Spinner spMaterias;
    ListView listaPlanificaficacionesFinalizadas;
    GestorPlanificador gestorPlanificador;
    GestorMateria gestorMateria;
    GestorExamen gestorExamen;
    ArrayList<ModeloPlanificacion> listadoPlanificaciones;
    ArrayList<ModeloMateria> listadoMaterias;
    ArrayList<String> listado;
    String itemSeleccionado;
    int indicador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_revisar_planes_antiguos);

        btnEliminar = (Button) findViewById(R.id.btnEliminiarPlanificacion);
        btnModificarHoras = (Button) findViewById(R.id.btnModificarPlanificacion);
        spMaterias = (Spinner) findViewById(R.id.spListadoMaterias);
        listaPlanificaficacionesFinalizadas = (ListView) findViewById(R.id.listaPlanificacionesAntiguas);
        gestorPlanificador = new GestorPlanificador();
        gestorMateria = new GestorMateria();
        gestorExamen = new GestorExamen();
        listadoPlanificaciones = new ArrayList<>();
        listadoMaterias=new ArrayList<>();
        listado = new ArrayList<>();

        cargarMateriasDisponibles();

        btnModificarHoras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresarHoras();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarPlanificacion();
            }
        });

        spMaterias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tipo = listadoMaterias.get(i).getTipo();
                String nombre = listadoMaterias.get(i).getNombre();
                cargarPlanificacionesFinalizadas(tipo,nombre);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listaPlanificaficacionesFinalizadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionado=listadoPlanificaciones.get(i).getIdPlanificacion();
                indicador=i;
            }
        });


    }

    /**
     * Método que obtiene la lista de materias registradas, de forma que el usuario pueda elegir
     * la planificación según una materia en particular.
     */
    private void cargarMateriasDisponibles(){
        ArrayList<String> listado = new ArrayList<>();
        ArrayList<ModeloMateria> listadoAuxiliar = new ArrayList<>();
        listadoMaterias = gestorMateria.obtenerListadoMaterias();
        if (listadoMaterias==null){
            btnEliminar.setEnabled(false);
            btnModificarHoras.setEnabled(false);
        }else {
            for (ModeloMateria materia : listadoMaterias) {
                if (materia.getEstado().equals("Cursando")) {
                    listado.add(materia.getNombre());
                }
                else{
                    listadoAuxiliar.add(materia);
                }
            }
            listadoMaterias.removeAll(listadoAuxiliar);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listado);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMaterias.setAdapter(adapter);
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
            listaPlanificaficacionesFinalizadas.setAdapter(itemsAdapter);
        }
    }

    /**
     * Método que obtiene la lista planificaciones registradas hasta el momento y genera un array
     * de strings para su presentación en la interfaz.
     */
    private void cargarPlanificacionesFinalizadas(String tipo, String nombre){
        listadoPlanificaciones = gestorPlanificador.obtenerListadoPlanificacionesPorTipo(tipo,nombre);
        ArrayList<ModeloPlanificacion> listadoAuxiliar = new ArrayList<>();
        if (listadoPlanificaciones==null){
            btnEliminar.setEnabled(false);
            btnModificarHoras.setEnabled(false);
        }else {
            if (listado!=null) {
                listado.clear();
            }
            btnEliminar.setEnabled(true);
            btnModificarHoras.setEnabled(true);
            for (ModeloPlanificacion modelo : listadoPlanificaciones) {
                if (modelo.getResultado()!=0) {
                    ModeloExamen examen = gestorExamen.obtenerDatosExamenPorId(modelo.getIdExamen());
                    listado.add(examen.getFecha() + " - " + examen.getMateria() +" - Horas utilizadas:"+modelo.getHoras()+" - Resultado:"+ modelo.getResultado());
                }else {
                    listadoAuxiliar.add(modelo);
                }
            }
            listadoPlanificaciones.removeAll(listadoAuxiliar);
            controlListView(listado);
        }
    }

    /**
     * Método que permite eliminar el registro de una planificación, luego de una confirmación, si
     * así lo deseara el usuario. Para ello hace uso de los gestores de datos.
     */
    private void eliminarPlanificacion(){
        if(itemSeleccionado!=null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_revisar_planes_antiguos.this);
            builder.setMessage(R.string.mensaje_eliminar)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            gestorPlanificador.eliminarPlanificacion(listadoPlanificaciones.get(indicador));
                            listadoPlanificaciones.remove(indicador);
                            listado.remove(indicador);
                            controlListView(listado);
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.show();
        }else{
            Toast.makeText(iu_revisar_planes_antiguos.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método que permite actualizar los registros en cuanto a la cantidad de horas semanales que
     * el usuario utilizo durante la planificación seleccionada, para estudiar sobre el examen
     * vinculado a dicha planificación.
     */
    public void ingresarHoras(){
        if (itemSeleccionado != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_revisar_planes_antiguos.this);
            builder.setTitle(getResources().getString(R.string.resultado));
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);
            builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int valor = Integer.valueOf(input.getText().toString());
                    if (input.getText().toString().trim().isEmpty()==false && valor>=0) {
                        ModeloPlanificacion modelo = listadoPlanificaciones.get(indicador);
                        modelo.setHoras(valor);
                        gestorPlanificador.actualizarPlanificacion(modelo);
                        Toast.makeText(iu_revisar_planes_antiguos.this, R.string.mensaje_modificacion_exitosa, Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(iu_revisar_planes_antiguos.this, R.string.error_campos_vacios, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setCancelable(true);
            builder.show();
        } else {
            Toast.makeText(iu_revisar_planes_antiguos.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }


}
