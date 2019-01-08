package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import modelos.ModeloHorarios;
import modelos.ModeloMateria;
import servicios.GestorMateria;

public class iu_agregar_materia extends AppCompatActivity {
    TextView horariosIngresados;
    Spinner opcionesTipo, opcionesDificultad, opcionesDias, opcionesEstado;
    EditText nombreMateria;
    TimePicker horaInicio, horaFin;
    ImageButton agregarHorario;
    Button agregarMateria;
    ArrayList<ModeloHorarios> listadoHorarios = new ArrayList<>();
    String seleccion = "";
    GestorMateria gestorMateria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_agregar_materia);

        Intent intent = getIntent();

        horariosIngresados = (TextView) findViewById(R.id.txtHorariosAgregados);
        opcionesTipo = (Spinner) findViewById(R.id.opcionesTipo);
        opcionesDificultad = (Spinner) findViewById(R.id.opcionesDificultad);
        opcionesDias = (Spinner) findViewById(R.id.opcionesDias);
        opcionesEstado = (Spinner) findViewById(R.id.opcionesEstado);
        nombreMateria = (EditText) findViewById(R.id.editNombreMateriaAgregar);
        horaFin = (TimePicker) findViewById(R.id.agregarMateriaHoraFin);
        horaInicio = (TimePicker) findViewById(R.id.agregarMateriaHoraInicio);
        agregarHorario = (ImageButton) findViewById(R.id.btnAgregarHorario);
        agregarMateria = (Button) findViewById(R.id.btnAgregarMateria);
        gestorMateria = new GestorMateria();

        agregarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dia, horaInicioElegida, horaFinElegida;
                if (ValidarHorarios()){
                    dia = opcionesDias.getSelectedItem().toString();
                    horaInicioElegida=horaInicio.getHour()+":"+horaInicio.getMinute();
                    horaFinElegida=horaFin.getHour()+":"+horaFin.getMinute();
                    listadoHorarios.add(new ModeloHorarios(dia,horaInicioElegida,horaFinElegida));
                    seleccion = seleccion +"\n"+ dia + " - " + horaInicioElegida + " - "+ horaFinElegida;
                    horariosIngresados.setText(seleccion);
                }

            }
        });

        agregarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre, tipo, dificultad, estado;
                if (ValidarEntradas()){
                    nombre=nombreMateria.getText().toString();
                    tipo = opcionesTipo.getSelectedItem().toString();
                    dificultad = opcionesDificultad.getSelectedItem().toString();
                    estado = opcionesEstado.getSelectedItem().toString();
                    ModeloMateria materia = new ModeloMateria(nombre,tipo,dificultad,estado);

                    gestorMateria.registrarMateria(materia,listadoHorarios);
                    Toast.makeText(iu_agregar_materia.this, "Completado.", Toast.LENGTH_SHORT).show();

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iu_agregar_materia.this.finish();
                    }
                }, 1000);

            }
        });

    }

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

    private boolean ValidarEntradas(){
        boolean respuesta = true;
        try{
            if (nombreMateria.getText().toString().trim().isEmpty()){
                throw new InstantiationException("Campo vacio.");
            }
            if (horariosIngresados.getText().toString().trim().isEmpty()){
                throw new InstantiationException("No se ha ingresado horarios.");
            }
        }
        catch (InstantiationException e){
            respuesta=false;
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return respuesta;
    }
}
