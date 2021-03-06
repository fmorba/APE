package com.morbidoni.proyecto.ape;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import servicios.GestorEvento;
import servicios.GestorExamen;
import servicios.GestorMateria;

/**
 * Clase que maneja la interfaz correspondiente a la modificación de los registros de un examen ya
 * registrado, principalmente para la corrección de errores en el registro inicial.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_modificar_examen extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_WRITE=0;
    String idExamen, fechaExamen, idUsuario, temas="";
    Spinner opcionesMaterias;
    DatePicker fechaModificada;
    TimePicker horaInicio, horaFin;
    ImageButton btnModificarTema;
    Button btnModificarExamen;
    CheckBox checkModificarTemas;
    EditText editTemasExamen;
    TextView txtTemasExamen;
    ModeloExamen examen;
    ModeloEvento evento;
    GestorExamen gestorExamen;
    GestorEvento gestorEvento;
    GestorMateria gestorMateria;
    ArrayList<ModeloMateria> materias = new ArrayList<>();
    ArrayList<String> listadoTemas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_modificar_examen);

        Intent intent = getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getString("idUsuario");
        idExamen = getuserID.getString("idExamen");
        fechaExamen = getuserID.getString("fecha");

        opcionesMaterias = (Spinner) findViewById(R.id.opcionesMateriasModificar);
        fechaModificada = (DatePicker) findViewById(R.id.editFechaExamenModificar);
        horaInicio = (TimePicker) findViewById(R.id.modificarExamenHoraInicial);
        horaFin = (TimePicker) findViewById(R.id.modificarExamenHoraFin);
        checkModificarTemas = (CheckBox) findViewById(R.id.checkModificarHorariosTemas);
        btnModificarTema = (ImageButton) findViewById(R.id.botonModificarTema);
        btnModificarExamen = (Button) findViewById(R.id.botonModificarExamen);
        txtTemasExamen = (TextView) findViewById(R.id.temasModificadosExamen);
        editTemasExamen = (EditText) findViewById(R.id.editModificarTemasExamen);

        pedirPermisosEscribir();
        gestorEvento = new GestorEvento(this);
        gestorMateria = new GestorMateria();
        gestorExamen = new GestorExamen();
        examen = gestorExamen.obtenerDatosExamenPorId(idExamen);
        evento = gestorEvento.obtenerDatosEventoPorId(examen.getIdEvento(),examen.getFecha());

        CompletarMateriasDisponibles();
        CompletarDatos();

        btnModificarTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTemasExamen.getText().toString().trim().isEmpty()){
                    Toast.makeText(iu_modificar_examen.this, getResources().getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show();
                }else{
                    String temaIngresado = editTemasExamen.getText().toString();
                    listadoTemas.add(temaIngresado);
                    if (temas==null){
                        temas=temaIngresado;
                    }else {
                        temas = temas + "\n" + temaIngresado;
                    }
                    txtTemasExamen.setText(temas);
                }
            }
        });

        btnModificarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarRegistroExamen();
            }
        });

        checkModificarTemas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                txtTemasExamen.setText("");
                btnModificarTema.setEnabled(true);
            }
        });

    }

    /**
     * Este método carga el listado de materias disponibles en el Spinner de la interfaz, de tal
     * forma de que el usuario sea capaz de modificar el tipo de materia correspondiente al examen
     * en caso de un error en el primer registro.
     */
    private void CompletarMateriasDisponibles(){
        ArrayList<String> listado = new ArrayList<>();
        materias= gestorMateria.obtenerListadoMaterias();
        for (ModeloMateria materia: materias) {
            listado.add(materia.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, listado);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        opcionesMaterias.setAdapter(adapter);
    }

    /**
     * Método que autocompleta los campos de la interfaz con la información ya registrada del
     * examen a modificar.
     */
    private void CompletarDatos(){
        String fecha = fechaExamen;
        int año = Integer.valueOf(fecha.split("-")[0]);
        int mes = Integer.valueOf(fecha.split("-")[1]);
        int dia = Integer.valueOf(fecha.split("-")[2]);
        fechaModificada.init(año,mes-1,dia,null);
        if (evento!=null) {
            String horaI = evento.getHoraInicio();
            String horaF = evento.getHoraFin();
            int hI = Integer.valueOf(horaI.split(":")[0]);
            int mI = Integer.valueOf(horaI.split(":")[1]);
            int hF = Integer.valueOf(horaF.split(":")[0]);
            int mF = Integer.valueOf(horaF.split(":")[1]);
            horaInicio.setHour(hI);
            horaInicio.setMinute(mI);
            horaFin.setHour(hF);
            horaFin.setMinute(mF);
        }
        txtTemasExamen.setText(examen.getTemas());
    }

    /**
     * Método que verifica la validez de los horarios ingresados, en relación a los límites
     * impuestos por el sistema.
     *
     * @return true: horario valido– false horario invalido
     */
    private boolean ValidarHorarios(){
        boolean respuesta = true;
        try{
            if (horaInicio.getHour()>horaFin.getHour() || (horaInicio.getHour()==horaFin.getHour() && horaInicio.getMinute()>=horaFin.getMinute()) ){
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
     * Método cuya tarea es registrar la nueva información sobre el examen modificado, mediante el
     * Gestor de Exámenes.
     */
    private void modificarRegistroExamen(){
        boolean validacion=ValidarHorarios();
        String fecha, horaIni, horaF, temas;

        if (validacion){

            String idMateria = examen.getIdMateria();
            int año = fechaModificada.getYear();
            int mes = fechaModificada.getMonth()+1;
            int dia = fechaModificada.getDayOfMonth();
            if (mes<10){fecha=año+"-"+"0"+mes+"-"+dia;}
            else {fecha=año+"-"+mes+"-"+dia;}
            horaIni=horaInicio.getHour()+":"+horaInicio.getMinute();
            horaF=horaFin.getHour()+":"+horaFin.getMinute();
            temas=listadoTemas.toString();

            if (evento!=null){
                evento.setHoraInicio(horaIni);
                evento.setHoraFin(horaF);
                gestorEvento.modificarEvento(examen.getIdEvento(), fecha, evento);
            }

            ModeloExamen examenModificado = new ModeloExamen(fecha,temas,opcionesMaterias.getSelectedItem().toString(),idMateria);

            gestorExamen.modificarExamen(idExamen,examenModificado);
            Toast.makeText(iu_modificar_examen.this, "Completado.", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iu_modificar_examen.this.finish();
            }
        }, 1000);

    }

    /**
     * Este método esta dedicado al pedido de los permisos necesarios para leer y/o escribir sobre
     * la aplicación de calendario existente en el dispositivo.
     */
    public void pedirPermisosEscribir(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CALENDAR)) {


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        MY_PERMISSIONS_REQUEST_WRITE);
            }
        }
    }

}
