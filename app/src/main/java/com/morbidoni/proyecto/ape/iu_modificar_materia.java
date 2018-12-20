package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class iu_modificar_materia extends AppCompatActivity {
    String idMateria;
    TextView listadoModficadoHorarios;
    EditText nombreMateria;
    TimePicker horaInicio, horaFin;
    Spinner opcionesDias, opcionesTipos, opcionesDificultades, opcionesEstados;
    ImageButton btnModificarHorarios;
    CheckBox checkModificarHorarios;
    Button btnModificarMateria;
    ArrayList<ModeloHorarios> listadoHorarios = new ArrayList<>();
    String seleccion="", idUsuario;
    GestorMateria gestorMateria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_modificar_materia);

        Intent intent = getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getString("idUsuario");
        idMateria = getuserID.getString("idMateria");

        listadoModficadoHorarios = (TextView) findViewById(R.id.horariosModificadosMateria);
        nombreMateria = (EditText) findViewById(R.id.editNombreMateriaModificar);
        horaInicio = (TimePicker) findViewById(R.id.modificarMateriaHoraInicio);
        horaFin = (TimePicker) findViewById(R.id.modificarMateriaHoraFin);
        opcionesDias = (Spinner) findViewById(R.id.opcionesDiasModificar);
        opcionesDificultades = (Spinner) findViewById(R.id.opcionesDificultadModificar);
        opcionesTipos = (Spinner) findViewById(R.id.opcionesTipoModificar);
        opcionesEstados = (Spinner) findViewById(R.id.opcionesEstadoModificar);
        btnModificarHorarios = (ImageButton) findViewById(R.id.botonModificarHorarioMateria);
        btnModificarMateria = (Button) findViewById(R.id.btnModificarMateria);
        checkModificarHorarios = (CheckBox) findViewById(R.id.checkModificarHorarios);
        gestorMateria = new GestorMateria();
        CompletarDatos();
        horaInicio.setEnabled(false);
        horaFin.setEnabled(false);
        btnModificarHorarios.setEnabled(false);
        opcionesDias.setEnabled(false);

        checkModificarHorarios.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkModificarHorarios.isChecked()){
                    horaInicio.setEnabled(true);
                    horaFin.setEnabled(true);
                    btnModificarHorarios.setEnabled(true);
                    opcionesDias.setEnabled(true);
                    listadoModficadoHorarios.setText("");
                    listadoHorarios.clear();
                    seleccion=null;
                }else{
                    horaInicio.setEnabled(false);
                    horaFin.setEnabled(false);
                    btnModificarHorarios.setEnabled(false);
                    opcionesDias.setEnabled(false);
                }
            }
        });

        btnModificarHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dia, horaInicioElegida, horaFinElegida;
                if (ValidarHorarios()){
                    dia = opcionesDias.getSelectedItem().toString();
                    horaInicioElegida=horaInicio.getHour()+":"+horaInicio.getMinute();
                    horaFinElegida=horaFin.getHour()+":"+horaFin.getMinute();
                    listadoHorarios.add(new ModeloHorarios(dia,horaInicioElegida,horaFinElegida));
                    seleccion = seleccion +"\n"+ dia + " - " + horaInicioElegida + " - "+ horaFinElegida;
                    listadoModficadoHorarios.setText(seleccion);
                }

            }
        });

        btnModificarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre, tipo, dificultad, estado;
                if (ValidarEntradas()){
                    nombre=nombreMateria.getText().toString();
                    tipo = opcionesTipos.getSelectedItem().toString();
                    dificultad = opcionesDificultades.getSelectedItem().toString();
                    estado = opcionesEstados.getSelectedItem().toString();
                    ModeloMateria materia = new ModeloMateria(nombre,tipo,dificultad,estado);

                    gestorMateria.actualizarDatosMateria(idMateria,materia,listadoHorarios);
                    Toast.makeText(iu_modificar_materia.this, "Completado", Toast.LENGTH_SHORT).show();

                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iu_modificar_materia.this.finish();
                    }
                }, 1000);
            }
        });

    }

    private void CompletarDatos(){
        String[] dificultades = getResources().getStringArray(R.array.opciones_dificultades);
        String[] tipos = getResources().getStringArray(R.array.opciones_tipos);
        String[] estados = getResources().getStringArray(R.array.opciones_estados_materias);

        ModeloMateria materia = gestorMateria.obtenerDatosMateria(idMateria);
        listadoHorarios=materia.getHorarios();

        nombreMateria.setText(materia.getNombre());
        for (int i = 0; i < dificultades.length ; i++) {
            if (dificultades[i].equals(materia.getDificultad())){
                opcionesDificultades.setSelection(i);
            }
        }
        for (int i = 0; i < tipos.length; i++) {
            if (tipos[i].equals(materia.getTipo())){
                opcionesTipos.setSelection(i);
            }
        }
        for (int i = 0; i < estados.length ; i++) {
            if (estados[i].equals(materia.getEstado())){
                opcionesEstados.setSelection(i);
            }
        }
        for (ModeloHorarios hora: listadoHorarios) {
            seleccion = seleccion +"\n"+ hora.getDia() + " - " + hora.getHoraInicio() + " - "+ hora.getHoraFin();
        }
        listadoModficadoHorarios.setText(seleccion);

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
            if (listadoModficadoHorarios.getText().toString().trim().isEmpty()){
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
