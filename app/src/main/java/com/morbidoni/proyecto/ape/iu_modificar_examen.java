package com.morbidoni.proyecto.ape;

import android.content.Intent;
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
import servicios.GestorEvento;
import servicios.GestorExamen;
import servicios.GestorMateria;

public class iu_modificar_examen extends AppCompatActivity {
    int idUsuario;
    String idExamen, idEvento, temas="";
    Spinner opcionesMaterias;
    DatePicker fechaModificada;
    TimePicker horaInicio, horaFin;
    ImageButton btnModificarTema;
    Button btnModificarExamen;
    CheckBox checkModificarTemas;
    EditText editTemasExamen, editResultado;
    TextView txtTemasExamen;
    ModeloExamen examen;
    ModeloEvento evento;
    GestorExamen gestorExamen;
    GestorEvento gestorEvento;
    GestorMateria gestorMateria;
    ArrayList<String> listadoTemas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_modificar_examen);

        Intent intent = getIntent();
        idExamen = intent.getStringExtra("mensaje"); //Se usa el nombre mensaje para definir el enviado de datos a otras clases.
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getInt(iu_login.EXTRA_MESSAGE);

        opcionesMaterias = (Spinner) findViewById(R.id.opcionesMateriasModificar);
        fechaModificada = (DatePicker) findViewById(R.id.editFechaExamenModificar);
        horaInicio = (TimePicker) findViewById(R.id.modificarMateriaHoraInicio);
        horaFin = (TimePicker) findViewById(R.id.modificarExamenHoraFin);
        checkModificarTemas = (CheckBox) findViewById(R.id.checkModificarHorariosTemas);
        btnModificarTema = (ImageButton) findViewById(R.id.botonModificarTema);
        btnModificarExamen = (Button) findViewById(R.id.botonModificarExamen);
        txtTemasExamen = (TextView) findViewById(R.id.temasModificadosExamen);
        editTemasExamen = (EditText) findViewById(R.id.editModificarTemasExamen);
        editResultado = (EditText) findViewById(R.id.resultadoModificarExamen);
        btnModificarExamen.setEnabled(false);

        gestorEvento = new GestorEvento();
        gestorMateria = new GestorMateria();
        gestorExamen = new GestorExamen();
        examen = gestorExamen.ObtenerDatosExamenPorId(idExamen);
        evento = gestorEvento.ObtenerDatosEventoPorId(examen.getIdEvento()+"");
        idEvento=examen.getIdEvento()+"";

        CompletarMateriasDisponibles();
        CompletarDatos();

        btnModificarTema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTemasExamen.getText().toString().trim().isEmpty()){
                    Toast.makeText(iu_modificar_examen.this, getResources().getString(R.string.campos_vacios), Toast.LENGTH_SHORT).show();
                }else{
                    String temaIngresado = editTemasExamen.getText().toString();
                    listadoTemas.add(temaIngresado);
                    temas=temas+"\n"+temaIngresado;
                    txtTemasExamen.setText(temas);
                }
            }
        });

        btnModificarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validacion=ValidarHorarios();
                String nombre, fecha, resultado, horaIni, horaF;

                if (validacion){
                    nombre = "Examen de "+opcionesMaterias.getSelectedItem().toString().split(" - ")[1];

                    if (editResultado.getText().toString()==null){
                        resultado="";
                    }else {
                        resultado=editResultado.getText().toString();
                    }
                    int idMateria = Integer.valueOf(opcionesMaterias.getSelectedItem().toString().split(" - ")[0]);
                    int año = fechaModificada.getYear();
                    int mes = fechaModificada.getMonth()+1;
                    int dia = fechaModificada.getDayOfMonth();
                    if (mes<10){fecha=año+"-"+"0"+mes+"-"+dia;}
                    else {fecha=año+"-"+mes+"-"+dia;}
                    horaIni=horaInicio.getHour()+":"+horaInicio.getMinute();
                    horaF=horaFin.getHour()+":"+horaFin.getMinute();

                    String respuesta1 = gestorEvento.ModificarEveto(idEvento,nombre,fecha, horaIni,horaF,"",nombre,true,idUsuario);
                    String respuesta2 = gestorExamen.ModificarExamen(idExamen,fecha,resultado,temas,examen.getIdEvento(),idMateria);
                    Toast.makeText(iu_modificar_examen.this, respuesta2, Toast.LENGTH_SHORT).show();
                }

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

    private void CompletarMateriasDisponibles(){
        ArrayList<String> listado = new ArrayList<>();
        listado = gestorMateria.ObtenerListadoMaterias(idUsuario);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, listado);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        opcionesMaterias.setAdapter(adapter);
    }

    private void CompletarDatos(){
        String fecha = evento.getFechaEvento();
        int año = Integer.valueOf(fecha.split("-")[0]);
        int mes = Integer.valueOf(fecha.split("-")[1]);
        int dia = Integer.valueOf(fecha.split("-")[2]);
        fechaModificada.init(año,mes-1,dia,null);
        String horaI=evento.getHoraInicioEvento();
        String horaF=evento.getHoraFinEvento();
        int hI =Integer.valueOf(horaI.split(":")[0]);
        int mI =Integer.valueOf(horaI.split(":")[1]);
        int hF =Integer.valueOf(horaF.split(":")[0]);
        int mF =Integer.valueOf(horaF.split(":")[1]);
        horaInicio.setHour(hI);
        horaInicio.setMinute(mI);
        horaFin.setHour(hF);
        horaFin.setMinute(mF);
        txtTemasExamen.setText(examen.getTemas());
        if (examen.getResultado()==null){
            editResultado.setText("");
        }else {
            editResultado.setText(examen.getResultado());
        }
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
}
