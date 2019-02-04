package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import servicios.GestorEvento;

/**
 * Una pequeña clase que se encarga de agregar nuevos planes de estudio a una planificación
 * propuesta, mediante el llamado a un simple interfaz, si el usuario así lo deseara.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_agregar_planes_de_estudio extends AppCompatActivity {
    GestorEvento gestorEvento;
    DatePicker fechaPlan;
    TimePicker horaInicio,horaFin;
    Button aceptar;
    boolean validacion =false;
    String fecha, inicio, fin;
    static String PUBLIC_STATIC_DATE_IDENTIFIER="fechaModificada", PUBLIC_STATIC_HOURF_IDENTIFIER="horaInicial", PUBLIC_STATIC_HOURL_IDENTIFIER="horaFinal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_agregar_planes_de_estudio);

        fechaPlan = (DatePicker) findViewById(R.id.dpAgregarFechaPlanEstudio);
        horaFin = (TimePicker) findViewById(R.id.tpAgregarHoraFinPlanEstudio);
        horaInicio = (TimePicker) findViewById(R.id.tpAgregarHoraInicioPlanEstudio);
        aceptar = (Button) findViewById(R.id.btnAgregarPlanEstudio);
        gestorEvento = new GestorEvento();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerDatosIngresados();

                if (validacion){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(PUBLIC_STATIC_DATE_IDENTIFIER, fecha);
                    resultIntent.putExtra(PUBLIC_STATIC_HOURF_IDENTIFIER, inicio);
                    resultIntent.putExtra(PUBLIC_STATIC_HOURL_IDENTIFIER, fin);
                    setResult(iu_planificador.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

    }

    /**
     * Método que obtiene la fecha y horarios según lo ingresado mediante la interfaz.
     */
    private void obtenerDatosIngresados(){
        int año = fechaPlan.getYear();
        int mes = fechaPlan.getMonth() + 1;
        int dia = fechaPlan.getDayOfMonth();
        if (mes < 10) {
            fecha = año + "-" + "0" + mes + "-" + dia;
        } else {
            fecha = año + "-" + mes + "-" + dia;
        }
        inicio = horaInicio.getHour() + ":" + horaInicio.getMinute();
        fin = horaFin.getHour() + ":" + horaFin.getMinute();

        validacion = ValidarHorarios();
    }

    /**
     * Método que verifica que la hora ingresada sea correcta dentro de los límites del día, de lo
     * contrario genera un mensaje de error.
     *
     * @return true: horario valido – false: horario inválido.
     */
    private boolean ValidarHorarios(){
        boolean respuesta = true;
        String respuestaString;
        try{
            if (horaInicio.getHour()>horaFin.getHour() || (horaInicio.getHour()==horaFin.getHour() && horaInicio.getMinute()>=horaFin.getMinute()) ){
                throw new InstantiationException("Horarios asignados invalidos.");
            }
            else{
                respuestaString=gestorEvento.validarHorarios(fecha,inicio,fin);
                if (respuestaString.equals("")==false){
                    throw new InstantiationException(respuestaString);
                }
            }
        }
        catch (InstantiationException e){
            respuesta=false;
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return respuesta;
    }
}
