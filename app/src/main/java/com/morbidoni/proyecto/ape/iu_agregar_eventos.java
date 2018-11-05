package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import servicios.GestorEvento;

public class iu_agregar_eventos extends AppCompatActivity {
    EditText edNombre, edDescripcion;
    DatePicker dpFechaEvento;
    TimePicker tpHoraInicio, tpHoraFin;
    CheckBox checkRecordatorio;
    Button btnAgregar;
    GestorEvento gestorEvento = new GestorEvento();
    int idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_agregar_eventos);

        Intent intent = getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getInt(iu_login.EXTRA_MESSAGE);

        edNombre = (EditText) findViewById(R.id.editNombreAgregarEvento);
        edDescripcion = (EditText) findViewById(R.id.editDescripcionEvento);
        dpFechaEvento = (DatePicker) findViewById(R.id.agregarFechaEvento);
        tpHoraInicio = (TimePicker) findViewById(R.id.agregarEventoHoraIni);
        tpHoraFin = (TimePicker) findViewById(R.id.agregarEventoHoraFin);
        checkRecordatorio = (CheckBox) findViewById(R.id.checkboxRecordatorio);
        btnAgregar = (Button) findViewById(R.id.btnAgregarEvento);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AgregarEvento();

            }
        });
    }

    private boolean ValidarEntradas(){
        boolean respuesta = true;
        try{
            String zux = edNombre.getText().toString();
            if (edNombre.getText().toString().equals("") || edNombre.getText().toString().replace(" ","").equals("")){
                throw new InstantiationException("Campo vacio.");
            }
            if (tpHoraInicio.getHour()>tpHoraFin.getHour() || (tpHoraInicio.getHour()==tpHoraFin.getHour() && tpHoraInicio.getMinute()>=tpHoraFin.getMinute()) ){
                throw new InstantiationException("Horarios asignados invalidos.");
            }
        }
        catch (InstantiationException e){
            respuesta=false;
            MensajeError(e.getMessage());
        }
        return respuesta;
    }

    private boolean ValidarHorarios(String fecha, String horaIni, String horaFin){
        boolean resultado = false;
        String respuesta = gestorEvento.ValidarHorarios(fecha, horaIni, horaFin,idUsuario);
        if (respuesta.equals("")){
            resultado=true;
            return resultado;
        }else {
            MensajeError(respuesta);
            return resultado;
        }
    }

    private void MensajeError(String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje)
                .setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void AgregarEvento(){

        String nombre, fecha, horaInicio, horaFin, descripcion;
        boolean recordatorio, validacionHorarios, validacionEntradas;

        validacionEntradas=ValidarEntradas();

        nombre = edNombre.getText().toString();
        if (nombre==""||nombre.replace(" ","")==""){nombre="NULL";}
        int año = dpFechaEvento.getYear();
        int mes = dpFechaEvento.getMonth()+1;
        int dia = dpFechaEvento.getDayOfMonth();
        if (mes<10){fecha=año+"-"+"0"+mes+"-"+dia;}
        else {fecha=año+"-"+mes+"-"+dia;}
        horaInicio=tpHoraInicio.getHour()+":"+tpHoraInicio.getMinute();
        horaFin=tpHoraFin.getHour()+":"+tpHoraFin.getMinute();
        if (edDescripcion.getText()==null){
            descripcion="";
        }else { descripcion=edDescripcion.getText().toString();}
        recordatorio=checkRecordatorio.isChecked();

        validacionHorarios=ValidarHorarios(fecha,horaInicio,horaFin);
        if (validacionHorarios&&validacionEntradas) {
            String respuesta=gestorEvento.AgregarEvento(nombre, fecha, horaInicio, horaFin, descripcion, recordatorio,idUsuario);
            Toast.makeText(iu_agregar_eventos.this, respuesta, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iu_agregar_eventos.this.finish();
                }
            }, 1000);
        }
    }
}
