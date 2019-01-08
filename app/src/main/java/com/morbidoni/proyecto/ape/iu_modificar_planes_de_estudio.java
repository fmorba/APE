package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import modelos.ModeloEvento;
import servicios.GestorEvento;

public class iu_modificar_planes_de_estudio extends AppCompatActivity {
    GestorEvento gestorEvento;
    ModeloEvento plan = new ModeloEvento();
    DatePicker fechaPlan;
    TimePicker horaInicio,horaFin;
    Button aceptar;
    String fecha, inicio, fin, fechaOriginal, inicioOriginal, finOrginal;
    static String PUBLIC_STATIC_DATE_IDENTIFIER="fechaModificada", PUBLIC_STATIC_HOURF_IDENTIFIER="horaInicial", PUBLIC_STATIC_HOURL_IDENTIFIER="horaFinal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_modificar_planes_de_estudio);

        Intent intent = getIntent();
        Bundle getData = getIntent().getExtras();
        fechaOriginal = getData.getString(PUBLIC_STATIC_DATE_IDENTIFIER);
        inicioOriginal = getData.getString(PUBLIC_STATIC_HOURF_IDENTIFIER);
        finOrginal = getData.getString(PUBLIC_STATIC_HOURL_IDENTIFIER);

        fechaPlan = (DatePicker) findViewById(R.id.dpModificarFechaPlan);
        horaFin = (TimePicker) findViewById(R.id.tpModificarHoraFinPlan);
        horaInicio = (TimePicker) findViewById(R.id.tpModificarHoraInicioPlan);
        aceptar = (Button) findViewById(R.id.btnModificarPlan);
        gestorEvento = new GestorEvento();

        CompletarDatos();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerDatosIngresados();

                Intent resultIntent = new Intent();
                resultIntent.putExtra(PUBLIC_STATIC_DATE_IDENTIFIER,fecha);
                resultIntent.putExtra(PUBLIC_STATIC_HOURF_IDENTIFIER,inicio);
                resultIntent.putExtra(PUBLIC_STATIC_HOURL_IDENTIFIER,fin);
                setResult(iu_planificador.RESULT_OK, resultIntent);
                finish();
            }
        });

    }

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

            boolean validacion = ValidarHorarios();

            if (validacion==false){
                fecha=fechaOriginal;
                inicio=inicioOriginal;
                fin=finOrginal;
            }

    }

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

    private void CompletarDatos(){
        int año = Integer.valueOf(fechaOriginal.split("-")[0]);
        int mes = Integer.valueOf(fechaOriginal.split("-")[1]);
        int dia = Integer.valueOf(fechaOriginal.split("-")[2]);
        fechaPlan.init(año, mes - 1, dia, null);
        int hI = Integer.valueOf(inicioOriginal.split(":")[0]);
        int mI = Integer.valueOf(inicioOriginal.split(":")[1]);
        int hF = Integer.valueOf(finOrginal.split(":")[0]);
        int mF = Integer.valueOf(finOrginal.split(":")[1]);
        horaInicio.setHour(hI);
        horaInicio.setMinute(mI);
        horaFin.setHour(hF);
        horaFin.setMinute(mF);
    }
}
