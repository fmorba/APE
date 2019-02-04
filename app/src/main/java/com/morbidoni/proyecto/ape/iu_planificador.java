package com.morbidoni.proyecto.ape;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import modelos.ModeloEvento;
import modelos.ModeloExamen;
import modelos.ModeloHorarios;
import modelos.ModeloMateria;
import modelos.ModeloPlanEstudio;
import modelos.ModeloPlanificacion;
import servicios.GestorAlgoritmo;
import servicios.GestorEvento;
import servicios.GestorExamen;
import servicios.GestorMateria;
import servicios.GestorPlanificador;

/**
 * Esta clase controla la interfaz dedicada al planificador de horarios de estudio, y a su vez
 * realiza las tareas relevantes para el correcto funcionamiento del mismo.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_planificador extends AppCompatActivity {
    String idUsuario;
    Spinner opcionesExamenes;
    Button btnIniciarPlanificador, btnAgregarPlan, btnAceptarPlanificacion;
    TextView horasEstimadas;
    ListView listaPlanesEstudios;
    ProgressBar progressBar;
    ArrayList<String> listado;
    ArrayList<ModeloExamen> listadoExamenes;
    ArrayList<ModeloHorarios> listadoHorarios;
    ArrayList<ModeloEvento> listadoEventosGenerados;
    ArrayList<String> listadoFechasPlanesEstudio;
    GestorExamen gestorExamen;
    GestorEvento gestorEvento;
    GestorMateria gestorMateria;
    GestorAlgoritmo gestorAlgoritmo;
    GestorPlanificador gestorPlanificador;
    ModeloMateria materia;
    int cantidadHorasSemanales, posicion;
    final static int CODIGO_DE_RESPUESTA_MODIFICAR=1;
    final static int CODIGO_DE_RESPUESTA_AGREGAR=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_planificador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPlanificador);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        opcionesExamenes = (Spinner) findViewById(R.id.opcionesExamenesPlanificador);
        horasEstimadas = (TextView) findViewById(R.id.txtHorasEstimadas);
        btnIniciarPlanificador = (Button) findViewById(R.id.botonIniciarPlanificador);
        btnAceptarPlanificacion = (Button) findViewById(R.id.botonAceptarPlaneación);
        btnAgregarPlan = (Button) findViewById(R.id.botonAgregarMasPlanes);
        listaPlanesEstudios = (ListView) findViewById(R.id.listaHorariosPlaneados);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(10);
        listado = new ArrayList<>();
        listadoEventosGenerados = new ArrayList<>();
        listadoFechasPlanesEstudio = new ArrayList<>();
        listadoExamenes = new ArrayList<>();
        gestorExamen = new GestorExamen();
        gestorEvento = new GestorEvento();
        gestorMateria = new GestorMateria();
        gestorPlanificador = new GestorPlanificador();

        cargarExamenesDisponibles();

        btnIniciarPlanificador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarPlanificador();
            }
        });

        btnAceptarPlanificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarPlanificacion();
            }
        });

        listaPlanesEstudios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                posicion=i;
                final AlertDialog.Builder builder = new AlertDialog.Builder(iu_planificador.this);
                builder.setMessage(R.string.mensaje_seleccion_plan_estudio)
                        .setPositiveButton(R.string.modificar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intentModificar = new Intent(iu_planificador.this,iu_modificar_planes_de_estudio.class);
                                intentModificar.putExtra(iu_modificar_planes_de_estudio.PUBLIC_STATIC_DATE_IDENTIFIER,listadoFechasPlanesEstudio.get(i));
                                intentModificar.putExtra(iu_modificar_planes_de_estudio.PUBLIC_STATIC_HOURF_IDENTIFIER,listadoEventosGenerados.get(i).getHoraInicio());
                                intentModificar.putExtra(iu_modificar_planes_de_estudio.PUBLIC_STATIC_HOURL_IDENTIFIER,listadoEventosGenerados.get(i).getHoraFin());
                                startActivityForResult(intentModificar,CODIGO_DE_RESPUESTA_MODIFICAR);
                                cargarPlanesGenerados();
                            }
                        })
                        .setNegativeButton(R.string.eliminar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listadoEventosGenerados.remove(i);
                                listadoFechasPlanesEstudio.remove(i);
                                cargarPlanesGenerados();
                            }
                        });
                builder.setCancelable(true);
                builder.show();
            }
        });

        btnAgregarPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAgregar = new Intent(iu_planificador.this,iu_agregar_planes_de_estudio.class);
                startActivityForResult(intentAgregar,CODIGO_DE_RESPUESTA_AGREGAR);
                cargarPlanesGenerados();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CODIGO_DE_RESPUESTA_MODIFICAR: {
                if (resultCode == Activity.RESULT_OK) {
                    String fecha = data.getStringExtra(iu_modificar_planes_de_estudio.PUBLIC_STATIC_DATE_IDENTIFIER);
                    String horaInicio = data.getStringExtra(iu_modificar_planes_de_estudio.PUBLIC_STATIC_HOURF_IDENTIFIER);
                    String horaFin = data.getStringExtra(iu_modificar_planes_de_estudio.PUBLIC_STATIC_HOURL_IDENTIFIER);
                    ModeloEvento evento = new ModeloEvento("Hora de estudio para: "+materia.getNombre(),horaInicio,horaFin,opcionesExamenes.getSelectedItem().toString(),true);
                    evento.setTipo("PlanDeEstudio");
                    listadoEventosGenerados.set(posicion,evento);
                    listadoFechasPlanesEstudio.set(posicion,fecha);
                }
                cargarPlanesGenerados();
                break;
            }
            case CODIGO_DE_RESPUESTA_AGREGAR: {
                if (resultCode == Activity.RESULT_OK) {
                    String fecha = data.getStringExtra(iu_agregar_planes_de_estudio.PUBLIC_STATIC_DATE_IDENTIFIER);
                    String horaInicio = data.getStringExtra(iu_agregar_planes_de_estudio.PUBLIC_STATIC_HOURF_IDENTIFIER);
                    String horaFin = data.getStringExtra(iu_agregar_planes_de_estudio.PUBLIC_STATIC_HOURL_IDENTIFIER);
                    ModeloEvento evento = new ModeloEvento("Hora de estudio para: "+materia.getNombre(),horaInicio,horaFin,opcionesExamenes.getSelectedItem().toString(),true);
                    evento.setTipo("PlanDeEstudio");
                    listadoEventosGenerados.add(evento);
                    listadoFechasPlanesEstudio.add(fecha);
                }
                cargarPlanesGenerados();
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_planificador, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el menu de la barra de tareas de la aplicación.
        int id = item.getItemId();

        if (id == R.id.menu_planificador_inicio) {
            progressBar.setVisibility(View.VISIBLE);
            Thread linea = new Thread (new Runnable() {
                @Override
                public void run() {
                    int horasSemanalesEstimadas = estimarHorasNecesarias();
                    gestorAlgoritmo = new GestorAlgoritmo(horasSemanalesEstimadas, materia.getTipo());
                    cantidadHorasSemanales = gestorAlgoritmo.obtenerOptimizacion();
                    generarPlanesDeEstudio(cantidadHorasSemanales);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cargarPlanesGenerados();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
            linea.start();
            return true;
        }
        if (id == R.id.menu_planificador_consejos) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            intentAyuda.putExtra("ayuda", "consejos");
            startActivity(intentAyuda);
            return true;
        }
        if (id == R.id.menu_planificador_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            intentAyuda.putExtra("ayuda", "planificador");
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            listaPlanesEstudios.setAdapter(itemsAdapter);
        }
    }

    /**
     * Método que carga el listado de exámenes disponibles en el Spinner de la interfaz, para que
     * el usuario pueda seleccionar uno del cual generar una planificación, de no haber exámenes no
     * se habilita la posibilidad de generar dichas planificaciones.
     */
    private void cargarExamenesDisponibles(){
        ArrayList<String> listado = new ArrayList<>();
        listadoExamenes = gestorExamen.obtenerListadoExamenesPendientes();
        if (listadoExamenes==null || listadoExamenes.isEmpty()){
            btnIniciarPlanificador.setEnabled(false);
            btnAceptarPlanificacion.setEnabled(false);
            btnAgregarPlan.setEnabled(false);
        }else {
            btnIniciarPlanificador.setEnabled(true);
            btnAceptarPlanificacion.setEnabled(true);
            btnAgregarPlan.setEnabled(true);
            for (ModeloExamen examen : listadoExamenes) {
                listado.add(examen.getFecha() + " - " + examen.getMateria());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listado);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            opcionesExamenes.setAdapter(adapter);
        }
    }

    /**
     * Método que obtiene la lista de planes de estudio registrados y genera un array de strings
     * para su presentación en la interfaz.
     */
    private void cargarPlanesGenerados(){
        ArrayList<String> listado= new ArrayList<>();
        for (int i = 0; i <listadoEventosGenerados.size() ; i++) {
            listado.add(listadoFechasPlanesEstudio.get(i)+" - "+listadoEventosGenerados.get(i).getNombre()+" - "+listadoEventosGenerados.get(i).getHoraInicio()+" - "+listadoEventosGenerados.get(i).getHoraFin());
        }
        controlListView(listado);
    }

    /**
     * Este método genera la cantidad de horas semanales estimadas, en base a la dificultad de la
     * materia del examen, y la cantidad de horas semanales que se cursan, en base a un estudio
     * realizado. El resultado sirve para alimentar al algoritmo genético.
     *
     * @return Cantidad de horas semanales estimadas.
     */
    private int estimarHorasNecesarias(){
        int horas=0, dificultadValor=1, horasSemanalesEstimadas=0;
        String dificultad;

        materia = gestorMateria.obtenerDatosMateria(listadoExamenes.get(opcionesExamenes.getSelectedItemPosition()).getIdMateria());
        dificultad=materia.getDificultad();
        listadoHorarios = materia.getHorarios();
        for (int i = 0; i < listadoHorarios.size(); i++) {
            int horaInicio = Integer.valueOf(listadoHorarios.get(i).getHoraInicio().split(":")[0]);
            int horaFin = Integer.valueOf(listadoHorarios.get(i).getHoraFin().split(":")[0]);
            horas += (horaFin-horaInicio);
        }
        switch(dificultad){
            case "Baja":
                dificultadValor=1;
                break;
            case "Media":
                dificultadValor=2;
                break;
            case "Alta":
                dificultadValor=3;
                break;
            default:
                break;
        }
        horasSemanalesEstimadas=horas*dificultadValor;
        return horasSemanalesEstimadas;
    }

    /**
     * Método que genera los planes individuales de la planificación, en base al tiempo libre en la
     * agenda y a la cantidad de horas estimadas por el algoritmo genético.
     *
     * @param horasSemanales Cantidad de horas por semana dedicadas al estudio.
     */
    private void generarPlanesDeEstudio(int horasSemanales){
        String fechaLimite = listadoExamenes.get(opcionesExamenes.getSelectedItemPosition()).getFecha();
        String hoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        int horasPorDia;
        ArrayList<String> horasLibres = new ArrayList<>();

        if (horasSemanales/7>1){
            if (horasSemanales/7>2){
                horasPorDia=horasSemanales/7;
            }else {
                horasPorDia=2;
            }
        }else {
            horasPorDia=1;
        }

        try {
            Date fechaInicial = formato.parse(hoy);
            Date fechaFinal = formato.parse(fechaLimite);
            Calendar c = Calendar.getInstance();
            c.setTime(fechaInicial);

            int dias=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000); //milisegundos en un dia, no se considera el dia del examen;
            dias=dias-1;
            String fechaElegida = formato.format(fechaInicial);

            for (int i = dias; i > 0 ; i-=7) {
                int horasUtilizadas=0;
                int diaUsados=0;
                for (int j = 0; j < horasSemanales ; j+=horasUtilizadas) {

                    horasLibres=gestorEvento.horasLibres(fechaElegida);

                    for (int k = 0; k < horasPorDia ; k++) {
                        ModeloEvento evento = new ModeloEvento("Hora de estudio para: "+materia.getNombre(),horasLibres.get(k).split(" - ")[0],horasLibres.get(k).split(" - ")[1],opcionesExamenes.getSelectedItem().toString(),true);
                        evento.setTipo("planDeEstudio");
                        listadoEventosGenerados.add(evento);
                        listadoFechasPlanesEstudio.add(fechaElegida.toString());
                        horasUtilizadas++;
                    }
                    c.add(Calendar.DATE,1);
                    fechaElegida=formato.format(new Date(c.getTimeInMillis()));
                    diaUsados++;
                }
                c.add(Calendar.DATE,7-diaUsados);
                fechaElegida=formato.format(new Date(c.getTimeInMillis()));
            }
        } catch (ParseException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método que realiza el registro de la planificación y sus planes individuales, una vez
     * aceptados por el usuario.
     */
    private void registrarPlanificacion(){
        if (listadoEventosGenerados.isEmpty()){
            Toast.makeText(iu_planificador.this, getResources().getString(R.string.error_eventos_no_generados), Toast.LENGTH_SHORT).show();
        }else{
            String hoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            ModeloPlanificacion planificacion = new ModeloPlanificacion(listadoExamenes.get(opcionesExamenes.getSelectedItemPosition()).getIdExamen(),hoy);
            ArrayList<ModeloPlanEstudio> planes = new ArrayList<>();
            String tipoMateria = gestorMateria.obtenerDatosMateria(listadoExamenes.get(opcionesExamenes.getSelectedItemPosition()).getIdMateria()).getTipo();
            String idPlanificacion=gestorPlanificador.registarPlanificacion(planificacion, tipoMateria);
            for (int i = 0; i <listadoEventosGenerados.size() ; i++) {
                String respuesta =gestorEvento.agregarEvento(listadoFechasPlanesEstudio.get(i),listadoEventosGenerados.get(i));
                String idEvento = respuesta.split(" - ")[1];
                planes.add(new ModeloPlanEstudio(listadoFechasPlanesEstudio.get(i),idEvento));
            }
            gestorPlanificador.agregarPlanes(idPlanificacion,tipoMateria,planes);
            Toast.makeText(iu_planificador.this, getResources().getString(R.string.completado), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iu_planificador.this.finish();
                }
            }, 1000);
        }
    }

    /**
     * Método que inicia el planificador, haciendo uso de los datos obtenidos, los gestores de
     * datos y del algoritmo genético.
     */
    private void iniciarPlanificador(){
        progressBar.setVisibility(View.VISIBLE);
        Thread linea = new Thread (new Runnable() {
            @Override
            public void run() {
                int horasSemanalesEstimadas = estimarHorasNecesarias();
                gestorAlgoritmo = new GestorAlgoritmo(horasSemanalesEstimadas, materia.getTipo());
                cantidadHorasSemanales = gestorAlgoritmo.obtenerOptimizacion();
                generarPlanesDeEstudio(cantidadHorasSemanales);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cargarPlanesGenerados();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        linea.start();
    }
}
