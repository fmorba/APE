package com.morbidoni.proyecto.ape;

import android.content.Intent;
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
import servicios.GestorEvento;
import servicios.GestorExamen;
import servicios.GestorMateria;

public class iu_agregar_examen extends AppCompatActivity {
    int idUsuario;
    String idEvento, temas;
    TimePicker examenHoraInicio, examenHoraFin;
    DatePicker examenFecha;
    TextView listadoTemasExamen;
    EditText agregarTemasExamen;
    ImageButton btnAgregarTemas;
    Spinner opcionesMaterias;
    Button btnAgregarExamen;
    ArrayList<String> listadoTemas = new ArrayList<>();
    GestorExamen gestorExamen;
    GestorEvento gestorEvento;
    GestorMateria gestorMateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_agregar_examen);

        Intent intent = getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getInt(iu_login.EXTRA_MESSAGE);

        examenFecha = (DatePicker) findViewById(R.id.dtFechaExamen);
        examenHoraFin = (TimePicker) findViewById(R.id.agregarEventoHoraFin);
        examenHoraInicio = (TimePicker) findViewById(R.id.agregarMateriaHoraInicio);
        listadoTemasExamen = (TextView) findViewById(R.id.listadoTemasAgregados);
        agregarTemasExamen = (EditText) findViewById(R.id.editAgregarTemasExamen);
        btnAgregarTemas = (ImageButton) findViewById(R.id.botonAgregarTema);
        btnAgregarExamen = (Button) findViewById(R.id.btnAgregarExamen);
        opcionesMaterias = (Spinner) findViewById(R.id.opcionesMaterias);

        CargarMateriasDisponibles();

        btnAgregarTemas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agregarTemasExamen.getText().toString().trim().isEmpty()){
                    Toast.makeText(iu_agregar_examen.this, getResources().getString(R.string.campos_vacios), Toast.LENGTH_SHORT).show();
                }else{
                String temaIngresado = agregarTemasExamen.getText().toString();
                listadoTemas.add(temaIngresado);
                temas=temas+"\n"+temaIngresado;
                listadoTemasExamen.setText(temas);
                }
            }
        });

        btnAgregarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validacion=ValidarHorarios();
                String nombre, fecha, horaInicio, horaFin, descripcion;

                if (validacion){
                    nombre = "Examen de "+opcionesMaterias.getSelectedItem().toString().split(" - ")[1];
                    int idMateria = Integer.valueOf(opcionesMaterias.getSelectedItem().toString().split(" - ")[0]);
                    int año = examenFecha.getYear();
                    int mes = examenFecha.getMonth()+1;
                    int dia = examenFecha.getDayOfMonth();
                    if (mes<10){fecha=año+"-"+"0"+mes+"-"+dia;}
                    else {fecha=año+"-"+mes+"-"+dia;}
                    horaInicio=examenHoraInicio.getHour()+":"+examenHoraInicio.getMinute();
                    horaFin=examenHoraFin.getHour()+":"+examenHoraFin.getMinute();
                    String respuesta1 = gestorEvento.AgregarEvento(nombre,fecha, horaInicio,horaFin,"",nombre,true,idUsuario);
                    idEvento = gestorEvento.ObtenerUltimoIDEvento(idUsuario+"");
                    ModeloExamen examen = new ModeloExamen(fecha,"",temas,Integer.valueOf(idEvento),idMateria);
                    String respuesta2 = gestorExamen.AgregarExamen(examen);
                    Toast.makeText(iu_agregar_examen.this, respuesta2, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean ValidarHorarios(){
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

    private void CargarMateriasDisponibles(){
        ArrayList<String> listado = new ArrayList<>();
        listado = gestorMateria.ObtenerListadoMaterias(idUsuario);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, listado);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        opcionesMaterias.setAdapter(adapter);
    }
}
