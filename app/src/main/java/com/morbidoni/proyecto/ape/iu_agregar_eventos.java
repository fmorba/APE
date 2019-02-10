package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import modelos.ModeloEvento;
import servicios.GestorEvento;

/**
 * Esta clase lleva el control de la interfaz para agregar eventos a la agenda, llamando a los
 * métodos correspondientes del Gestor de Eventos.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_agregar_eventos extends AppCompatActivity {
    EditText edNombre, edDescripcion;
    DatePicker dpFechaEvento;
    TimePicker tpHoraInicio, tpHoraFin;
    CheckBox checkRecordatorio;
    Button btnAgregar;
    GestorEvento gestorEvento = new GestorEvento();
    String idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_agregar_eventos);

        Intent intent = getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getString("idUsuario");

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

                agregarEvento();

            }
        });
    }

    /**
     * Este método valida los datos ingresado por el usuario y presenta un mensaje de error,
     * debería hallarse algún problema con los mismos.
     *
     * @return true: datos validos – false: datos inválidos.
     */
    private boolean validarEntradas() {
        boolean respuesta = true;
        try {

            if (edNombre.getText().toString().trim().isEmpty()) {
                throw new InstantiationException("Campo vacio.");
            }
            int hI = tpHoraInicio.getHour();
            int hF = tpHoraFin.getHour();


            if (tpHoraInicio.getHour() > tpHoraFin.getHour() || (tpHoraInicio.getHour() == tpHoraFin.getHour() && tpHoraInicio.getMinute() >= tpHoraFin.getMinute())) {
                throw new InstantiationException("Horarios asignados invalidos.");
            }
        } catch (InstantiationException e) {
            respuesta = false;
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return respuesta;
    }

    /**
     * Método que transforma los datos ingresados en un Modelo de Evento, para luego llamar a
     * Gestor de eventos para registrar dicha acción en la agenda.
     */
    private void agregarEvento() {

        String nombre, fecha, horaInicio, horaFin, descripcion, diaFecha;
        boolean recordatorio, validacionEntradas;

        validacionEntradas = validarEntradas();

        nombre = edNombre.getText().toString();
        if (nombre == "" || nombre.trim().isEmpty()) {
            nombre = "NULL";
        }
        int año = dpFechaEvento.getYear();
        int mes = dpFechaEvento.getMonth() + 1;
        int dia = dpFechaEvento.getDayOfMonth();
        if (dia<10){
            diaFecha="0"+dia;
        }else {
            diaFecha=dia+"";
        }
        if (mes < 10) {
            fecha = año + "-" + "0" + mes + "-" + diaFecha;
        } else {
            fecha = año + "-" + mes + "-" + diaFecha;
        }
        horaInicio = tpHoraInicio.getHour() + ":" + tpHoraInicio.getMinute();
        horaFin = tpHoraFin.getHour() + ":" + tpHoraFin.getMinute();
        if (edDescripcion.getText() == null) {
            descripcion = "";
        } else {
            descripcion = edDescripcion.getText().toString();
        }
        recordatorio = checkRecordatorio.isChecked();

        if (validacionEntradas) {
            ModeloEvento evento = new ModeloEvento(nombre, horaInicio, horaFin, descripcion, recordatorio);
            evento.setTipo("evento");
            String respuesta = gestorEvento.agregarEvento(fecha, evento);
            respuesta=respuesta.split(" - ")[0];
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
