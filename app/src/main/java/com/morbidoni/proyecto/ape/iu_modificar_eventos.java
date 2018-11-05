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

import java.text.SimpleDateFormat;

import database.EventoModelo;
import modelos.ModeloEvento;
import servicios.GestorEvento;

public class iu_modificar_eventos extends AppCompatActivity {
    GestorEvento gestorEvento;
    EditText editNombre,editDescripcion;
    DatePicker dpFechaEvento;
    TimePicker tpHoraInicio, tpHoraFinal;
    CheckBox checkRecordatorio, checkEventoSemanal;
    Button btnModificar;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_modificar_eventos);

        Intent intent = getIntent();
        final String idEvento = intent.getStringExtra("mensaje"); //Se usa el nombre mensaje para definir el enviado de datos a otras clases.
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getInt(iu_login.EXTRA_MESSAGE);
        gestorEvento = new GestorEvento();

        editNombre = (EditText) findViewById(R.id.editModificarNombreEvento);
        editDescripcion = (EditText) findViewById(R.id.editModificarDescripcionEvento);
        dpFechaEvento = (DatePicker) findViewById(R.id.dpModificarFechaEvento);
        tpHoraInicio = (TimePicker) findViewById(R.id.tpModificarHoraInicioEvento);
        tpHoraFinal = (TimePicker) findViewById(R.id.tpModificarHoraFinEvento);
        checkEventoSemanal = (CheckBox) findViewById(R.id.checkboxEventoSemanalModificacion);
        checkRecordatorio = (CheckBox) findViewById(R.id.checkboxModificarRecordatorio);
        btnModificar = (Button) findViewById(R.id.btnActualizarEvento);

        CompletarDatos(idEvento);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validacionHorarios, validacionEntradas;
                validacionEntradas=ValidarEntradas();

                String respuesta;
                String nombreModificado = editNombre.getText().toString();
                String fechaModificada = dpFechaEvento.getYear()+"-"+(dpFechaEvento.getMonth()+1)+"-"+dpFechaEvento.getDayOfMonth();
                String horaInicioModificada=tpHoraInicio.getHour()+":"+tpHoraInicio.getMinute();
                String horaFinalModificada=tpHoraFinal.getHour()+":"+tpHoraFinal.getMinute();
                String descripcionModificada;
                if (editDescripcion.getText()==null){
                    descripcionModificada="";
                }else { descripcionModificada=editDescripcion.getText().toString();}
                boolean recordatorioModificado=checkRecordatorio.isChecked();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                String diaNombre = sdf.format(dpFechaEvento);

                if (validacionEntradas){
                    if (checkEventoSemanal.isChecked()) {
                        respuesta = gestorEvento.ModificarEveto(idEvento, nombreModificado, fechaModificada, horaInicioModificada, horaFinalModificada, diaNombre,descripcionModificada, recordatorioModificado, idUsuario);
                        Toast.makeText(iu_modificar_eventos.this, respuesta, Toast.LENGTH_SHORT).show();
                    }else{
                        validacionHorarios=ValidarHorarios(fechaModificada,horaInicioModificada,horaFinalModificada, idEvento);
                        if (validacionHorarios){
                            respuesta = gestorEvento.ModificarEveto(idEvento, nombreModificado, fechaModificada, horaInicioModificada, horaFinalModificada, "",descripcionModificada, recordatorioModificado, idUsuario);
                            Toast.makeText(iu_modificar_eventos.this, respuesta, Toast.LENGTH_SHORT).show();
                        }
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iu_modificar_eventos.this.finish();
                        }
                    }, 1000);
                }
            }
        });

    }

    private void CompletarDatos(String idE){
        ModeloEvento modelo = gestorEvento.ObtenerDatosEventoPorId(idE);

        editNombre.setText(modelo.getNombreEvento());
        editDescripcion.setText(modelo.getDescripcionEvento());
        String fecha = modelo.getFechaEvento();
        int año = Integer.valueOf(fecha.split("-")[0]);
        int mes = Integer.valueOf(fecha.split("-")[1]);
        int dia = Integer.valueOf(fecha.split("-")[2]);
        dpFechaEvento.init(año,mes-1,dia,null);
        String horaI=modelo.getHoraInicioEvento();
        String horaF=modelo.getHoraFinEvento();
        int hI =Integer.valueOf(horaI.split(":")[0]);
        int mI =Integer.valueOf(horaI.split(":")[1]);
        int hF =Integer.valueOf(horaF.split(":")[0]);
        int mF =Integer.valueOf(horaF.split(":")[1]);
        tpHoraInicio.setHour(hI);
        tpHoraInicio.setMinute(mI);
        tpHoraFinal.setHour(hF);
        tpHoraFinal.setMinute(mF);
        int estadoRecordatorio = modelo.getRecordatorioEvento();
        if (estadoRecordatorio==1){
            checkRecordatorio.setChecked(true);
        }else {checkRecordatorio.setChecked(false);}
    }

    private boolean ValidarHorarios(String fecha, String horaIni, String horaFin, String idEvento){
        boolean resultado = false;
        String respuesta = gestorEvento.ValidarHorariosModificacion(fecha, horaIni, horaFin,idUsuario, idEvento);
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

    private boolean ValidarEntradas(){
        boolean respuesta = true;
        try{
            if (editNombre.getText()==null){
                throw new InstantiationException("Campo vacio.");

            }
            if (tpHoraInicio.getHour()>tpHoraFinal.getHour() || (tpHoraInicio.getHour()==tpHoraFinal.getHour() && tpHoraInicio.getMinute()>=tpHoraFinal.getMinute()) ){
                throw new InstantiationException("Horarios asignados invalidos.");
            }
        }
        catch (InstantiationException e){
            respuesta=false;
            MensajeError(e.getMessage());
        }
        return respuesta;
    }
}
