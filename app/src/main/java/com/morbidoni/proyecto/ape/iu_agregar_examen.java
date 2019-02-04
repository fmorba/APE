package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import modelos.ModeloEvento;
import modelos.ModeloExamen;
import modelos.ModeloMateria;
import servicios.GestorExamen;
import servicios.GestorMateria;

/**
 * Clase que maneja la interfaz para agregar exámenes a la agenda de la aplicación, se encarga de
 * verificar datos y llamar a los métodos correspondientes de los gestores de datos.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_agregar_examen extends AppCompatActivity {
    String temas, idUsuario;
    TimePicker examenHoraInicio, examenHoraFin;
    DatePicker examenFecha;
    TextView listadoTemasExamen;
    EditText agregarTemasExamen;
    ImageButton btnAgregarTemas;
    Spinner opcionesMaterias;
    Button btnAgregarExamen;
    ArrayList<String> listadoTemas = new ArrayList<>();
    ArrayList<ModeloMateria> materias = new ArrayList<>();
    GestorExamen gestorExamen;
    GestorMateria gestorMateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_agregar_examen);

        Intent intent = getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getString("idUsuario");

        examenFecha = (DatePicker) findViewById(R.id.dtFechaExamen);
        examenHoraFin = (TimePicker) findViewById(R.id.agregarExamenHoraFin);
        examenHoraInicio = (TimePicker) findViewById(R.id.agregarExamenHoraInicio);
        listadoTemasExamen = (TextView) findViewById(R.id.listadoTemasAgregados);
        agregarTemasExamen = (EditText) findViewById(R.id.editAgregarTemasExamen);
        btnAgregarTemas = (ImageButton) findViewById(R.id.botonAgregarTema);
        btnAgregarExamen = (Button) findViewById(R.id.btnAgregarExamen);
        opcionesMaterias = (Spinner) findViewById(R.id.opcionesMaterias);
        gestorExamen = new GestorExamen();
        gestorMateria = new GestorMateria();

        cargarMateriasDisponibles();

        btnAgregarTemas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarTemas();
            }
        });

        btnAgregarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarExamen();
            }
        });
    }

    /**
     * Método que verifica que la hora ingresada sea correcta dentro de los límites del día.
     *
     * @return true: horario valido – false: horario inválido.
     */
    private boolean validarHorarios(){
        boolean respuesta = true;
        try{
            if (examenHoraInicio.getHour()>examenHoraFin.getHour() || (examenHoraInicio.getHour()==examenHoraFin.getHour() && examenHoraInicio.getMinute()>=examenHoraFin.getMinute()) ){
                throw new InstantiationException("Horarios asignados invalidos.");
            }
        }
        catch (InstantiationException e){
            respuesta=false;
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return respuesta;
    }

    /**
     * Este método carga en la interfaz del usuario, la lista de materias disponibles, para que
     * pueda asignarle una al examen que quiere registrar.
     */
    private void cargarMateriasDisponibles(){
        ArrayList<String> listado = new ArrayList<>();
        ArrayList<ModeloMateria> listaAuxiliar = new ArrayList<>();
        materias = gestorMateria.obtenerListadoMaterias();
        if (materias==null){
            btnAgregarExamen.setEnabled(false);
        }else {
            for (ModeloMateria materia : materias) {
                if (materia.getEstado().equals("Cursando")) {
                    listado.add(materia.getNombre());
                }else{
                    listaAuxiliar.add(materia);
                }
            }
            materias.removeAll(listaAuxiliar);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listado);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            opcionesMaterias.setAdapter(adapter);
        }
    }

    /**
     * Método que convierte los datos ingresados a un Modelo de examen, para luego llamar al Gestor
     * de Exámenes, y realizar el registro del mismo.
     */
    private void agregarExamen(){
        boolean validacion= validarHorarios();
        String nombre, fecha, horaInicio, horaFin, temas;

        if (validacion){
            nombre = "Examen de "+opcionesMaterias.getSelectedItem().toString();
            int año = examenFecha.getYear();
            int mes = examenFecha.getMonth()+1;
            int dia = examenFecha.getDayOfMonth();
            if (mes<10){fecha=año+"-"+"0"+mes+"-"+dia;}
            else {fecha=año+"-"+mes+"-"+dia;}
            horaInicio=examenHoraInicio.getHour()+":"+examenHoraInicio.getMinute();
            horaFin=examenHoraFin.getHour()+":"+examenHoraFin.getMinute();
            temas=listadoTemas.toString();
            String idMateria = materias.get(opcionesMaterias.getSelectedItemPosition()).getIdMateria();
            ModeloExamen examen = new ModeloExamen(fecha,temas,opcionesMaterias.getSelectedItem().toString(),idMateria);
            examen.setResultado("");
            ModeloEvento evento = new ModeloEvento(nombre, horaInicio, horaFin, nombre, true);
            gestorExamen.agregarExamen(examen, evento);
            Toast.makeText(iu_agregar_examen.this, "Completo", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iu_agregar_examen.this.finish();
            }
        }, 1000);
    }

    /**
     * Método que registra los temas ingresados por el usuario mediante la interfaz, y actualiza la
     * misma para que dicho usuario pueda ver que temas ya ha ingresado.
     */
    private void agregarTemas(){
        if (agregarTemasExamen.getText().toString().trim().isEmpty()){
            Toast.makeText(iu_agregar_examen.this, getResources().getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show();
        }else{
            String temaIngresado = agregarTemasExamen.getText().toString();
            listadoTemas.add(temaIngresado);
            if (temas==null){
                temas=temaIngresado;
            }else {
                temas = temas + "\n" + temaIngresado;
            }
            listadoTemasExamen.setText(temas);
        }
    }
}
