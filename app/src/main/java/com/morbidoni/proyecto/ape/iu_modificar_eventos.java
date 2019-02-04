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
 * Esta clase se encarga de la interfaz cuya función es modificar los registros de eventos ya
 * guardados previamente, a decision del usuario.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_modificar_eventos extends AppCompatActivity {
    GestorEvento gestorEvento;
    EditText editNombre, editDescripcion;
    DatePicker dpFechaEvento;
    TimePicker tpHoraInicio, tpHoraFinal;
    CheckBox checkRecordatorio;
    Button btnModificar;
    String idUsuario, idEvento, fechaEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_modificar_eventos);

        Intent intent = getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getString("idUsuario");
        idEvento = getuserID.getString("idEvento");
        fechaEvento = getuserID.getString("fechaEvento");
        gestorEvento = new GestorEvento();

        editNombre = (EditText) findViewById(R.id.editModificarNombreEvento);
        editDescripcion = (EditText) findViewById(R.id.editModificarDescripcionEvento);
        dpFechaEvento = (DatePicker) findViewById(R.id.dpModificarFechaEvento);
        tpHoraInicio = (TimePicker) findViewById(R.id.tpModificarHoraInicioEvento);
        tpHoraFinal = (TimePicker) findViewById(R.id.tpModificarHoraFinEvento);
        checkRecordatorio = (CheckBox) findViewById(R.id.checkboxModificarRecordatorio);
        btnModificar = (Button) findViewById(R.id.btnActualizarEvento);

        CompletarDatos(idEvento, fechaEvento);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarRegistroEvento();
            }
        });

    }

    /**
     * Método que autocompleta los campos de la interfaz con la información ya registrada del
     * evento a modificar.
     *
     * @param idE String correspondiente al id del evento.
     * @param fechaO String correspondiente a la fecha original del evento.
     */
    private void CompletarDatos(String idE, String fechaO) {
        ModeloEvento modelo = gestorEvento.obtenerDatosEventoPorId(idE, fechaO);

        editNombre.setText(modelo.getNombre());
        editDescripcion.setText(modelo.getDescripcion());
        String fecha = fechaO;
        int año = Integer.valueOf(fecha.split("-")[0]);
        int mes = Integer.valueOf(fecha.split("-")[1]);
        int dia = Integer.valueOf(fecha.split("-")[2]);
        dpFechaEvento.init(año, mes - 1, dia, null);
        String horaI = modelo.getHoraInicio();
        String horaF = modelo.getHoraFin();
        int hI = Integer.valueOf(horaI.split(":")[0]);
        int mI = Integer.valueOf(horaI.split(":")[1]);
        int hF = Integer.valueOf(horaF.split(":")[0]);
        int mF = Integer.valueOf(horaF.split(":")[1]);
        tpHoraInicio.setHour(hI);
        tpHoraInicio.setMinute(mI);
        tpHoraFinal.setHour(hF);
        tpHoraFinal.setMinute(mF);
        boolean estadoRecordatorio = modelo.isRecordatorio();
        checkRecordatorio.setChecked(estadoRecordatorio);
    }

    /**
     * Método que validada la información ingresada por parte del usuario para no dejar campos
     * importantes vacíos.
     *
     * @return true: datos validos– false datos inválidos
     */
    private boolean ValidarEntradas() {
        boolean respuesta = true;
        try {
            if (editNombre.getText() == null) {
                throw new InstantiationException("Campo vacio.");

            }
            if (tpHoraInicio.getHour() > tpHoraFinal.getHour() || (tpHoraInicio.getHour() == tpHoraFinal.getHour() && tpHoraInicio.getMinute() >= tpHoraFinal.getMinute())) {
                throw new InstantiationException("Horarios asignados invalidos.");
            }
        } catch (InstantiationException e) {
            respuesta = false;
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return respuesta;
    }

    /**
     * Método que se encarga de actualizar los registro del evento, con la nueva información
     * ingresada por parte del usuario.
     */
    private void modificarRegistroEvento(){
        boolean validacionEntradas;
        String fechaModificada;
        validacionEntradas = ValidarEntradas();

        String respuesta;
        String nombreModificado = editNombre.getText().toString();
        if (dpFechaEvento.getMonth()<10){
            fechaModificada = dpFechaEvento.getYear() + "-" + "0"+(dpFechaEvento.getMonth() + 1) + "-" + dpFechaEvento.getDayOfMonth();
        }else {
            fechaModificada = dpFechaEvento.getYear() + "-" + (dpFechaEvento.getMonth() + 1) + "-" + dpFechaEvento.getDayOfMonth();
        }
        String horaInicioModificada = tpHoraInicio.getHour() + ":" + tpHoraInicio.getMinute();
        String horaFinalModificada = tpHoraFinal.getHour() + ":" + tpHoraFinal.getMinute();
        String descripcionModificada;
        if (editDescripcion.getText() == null) {
            descripcionModificada = "";
        } else {
            descripcionModificada = editDescripcion.getText().toString();
        }
        boolean recordatorioModificado = checkRecordatorio.isChecked();

        if (validacionEntradas) {
            ModeloEvento eventoNuevo = new ModeloEvento(nombreModificado, horaInicioModificada, horaFinalModificada, descripcionModificada, recordatorioModificado);
            eventoNuevo.setTipo("evento");
            respuesta = gestorEvento.modificarEvento(idEvento,fechaModificada, eventoNuevo);
            Toast.makeText(iu_modificar_eventos.this, respuesta, Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iu_modificar_eventos.this.finish();
            }
        }, 1000);
    }
}
