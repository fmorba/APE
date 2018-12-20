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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import algoritmo_genetico.AlgoritmoGenetico;
import modelos.ModeloEvento;
import modelos.ModeloExamen;
import modelos.ModeloHorarios;
import modelos.ModeloMateria;
import modelos.ModeloPlanEstudio;
import servicios.GestorAlgoritmo;
import servicios.GestorEvento;
import servicios.GestorExamen;
import servicios.GestorMateria;
import servicios.GestorPlanificador;

public class iu_planificador extends AppCompatActivity {
    String idUsuario;
    Spinner opcionesExamenes;
    Button btnIniciarPlanificador, btnAceptarPlanificacion;
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
    int cantidadHorasSemanales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_planificador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPlanificador);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getString("idUsuario");

        opcionesExamenes = (Spinner) findViewById(R.id.opcionesExamenesPlanificador);
        horasEstimadas = (TextView) findViewById(R.id.txtHorasEstimadas);
        btnIniciarPlanificador = (Button) findViewById(R.id.botonIniciarPlanificador);
        btnAceptarPlanificacion = (Button) findViewById(R.id.botonAceptarPlaneación);
        listaPlanesEstudios = (ListView) findViewById(R.id.listaHorariosPlaneados);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(10);
        listado = new ArrayList<>();
        listadoEventosGenerados = new ArrayList<>();
        listadoExamenes = new ArrayList<>();
        gestorExamen = new GestorExamen();
        gestorEvento = new GestorEvento();
        gestorMateria = new GestorMateria();
        gestorPlanificador = new GestorPlanificador();

        CargarExamenesDisponibles();

        btnIniciarPlanificador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int horasSemanalesEstimadas = estimarHorasNecesarias();
                gestorAlgoritmo = new GestorAlgoritmo(horasSemanalesEstimadas, materia.getTipo());
                cantidadHorasSemanales = gestorAlgoritmo.obtenerOptimizacion();
                generarPlanesDeEstudio(cantidadHorasSemanales);
                CargarPlanesGenerados();
            }
        });

        btnAceptarPlanificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
            return true;
        }
        if (id == R.id.menu_planificador_eliminar) {
            return true;
        }
        if (id == R.id.menu_planificador_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ControlListView(ArrayList<String> array){
        if (array!=null) {
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listaPlanesEstudios.setAdapter(itemsAdapter);
        }
    }

    private void CargarExamenesDisponibles(){
        ArrayList<String> listado = new ArrayList<>();
        listadoExamenes = gestorExamen.obtenerListadoExamenesPendientes();
        if (listadoExamenes==null || listadoExamenes.isEmpty()){
            btnIniciarPlanificador.setEnabled(false);
            btnAceptarPlanificacion.setEnabled(false);
        }else {
            btnIniciarPlanificador.setEnabled(true);
            btnAceptarPlanificacion.setEnabled(true);
            for (ModeloExamen examen : listadoExamenes) {
                listado.add(examen.getFecha() + " - " + examen.getMateria());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listado);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            opcionesExamenes.setAdapter(adapter);
        }
    }

    private void CargarPlanesGenerados(){
        ArrayList<String> listado= new ArrayList<>();
        for (int i = 0; i <listadoEventosGenerados.size() ; i++) {
            listado.add(listadoFechasPlanesEstudio.get(i)+" - "+listadoEventosGenerados.get(i).getNombre()+" - "+listadoEventosGenerados.get(i).getHoraInicio()+" - "+listadoEventosGenerados.get(i).getHoraFin());
        }
        ControlListView(listado);
    }



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

            int dias=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000); //milisegundos en un dia;
            String fechaElegida = formato.format(fechaInicial);

            for (int i = dias; i > 0 ; i-=7) {
                int aux=0;
                for (int j = 0; j < horasSemanales ; j+=aux) {

                    horasLibres=gestorEvento.horasLibres(fechaElegida);

                    for (int k = 0; k < horasPorDia ; k++) {
                        ModeloEvento evento = new ModeloEvento("Hora de estudio para: "+materia.getNombre(),horasLibres.get(k).split(" - ")[0],horasLibres.get(k).split(" - ")[1],opcionesExamenes.getSelectedItem().toString(),true);
                        evento.setTipo("PlanDeEstudio");
                        listadoEventosGenerados.add(evento);
                        listadoFechasPlanesEstudio.add(fechaElegida.toString());
                        aux++;
                    }
                    c.add(Calendar.DATE,1);
                    fechaElegida=formato.format(new Date(c.getTimeInMillis()));

                }
            }
        } catch (ParseException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
