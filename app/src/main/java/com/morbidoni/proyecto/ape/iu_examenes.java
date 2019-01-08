package com.morbidoni.proyecto.ape;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import modelos.ModeloEvento;
import modelos.ModeloExamen;
import servicios.GestorEvento;
import servicios.GestorExamen;
import servicios.GestorMateria;
import servicios.GestorPlanificador;

public class iu_examenes extends AppCompatActivity {
    ImageButton btnAgregarExamen, btnObservarExamen, btnModificarExamen, btnEliminarExamen, btnResultado;
    ListView listadoExamenes;
    int indicador;
    String itemSeleccionado, nombreMateria, fechaExamen, idUsuario, idMateria;
    ArrayList<String> listado = new ArrayList<>();
    ArrayList<ModeloExamen> arrayExamenes = new ArrayList<>();
    GestorExamen gestorExamen;
    GestorMateria gestorMateria;
    GestorPlanificador gestorPlanificador;
    GestorEvento gestorEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_examenes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarExamenes);
        setSupportActionBar(toolbar);

        final Intent intent= getIntent();

        btnAgregarExamen = (ImageButton) findViewById(R.id.boton_agregar_examen);
        btnObservarExamen = (ImageButton) findViewById(R.id.boton_ver_examen);
        btnModificarExamen = (ImageButton) findViewById(R.id.boton_modificar_examen);
        btnEliminarExamen = (ImageButton) findViewById(R.id.boton_eliminar_examen);
        btnResultado = (ImageButton) findViewById(R.id.boton_ingresar_resultados);
        listadoExamenes = (ListView) findViewById(R.id.listadoMenuExamenes);
        gestorExamen = new GestorExamen();
        gestorMateria = new GestorMateria();
        gestorEvento = new GestorEvento();
        gestorPlanificador = new GestorPlanificador();

        cargarListadoExamenes();

        final Intent intentAgregarExamen = new Intent(this, iu_agregar_examen.class);
        final Intent intentModificarExamen = new Intent(this, iu_modificar_examen.class);

        btnAgregarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentAgregarExamen.putExtra("idUsuario",idUsuario);
                startActivity(intentAgregarExamen);
            }
        });

        btnObservarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDatosExamen();
            }
        });

        btnModificarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentModificarExamen.putExtra("idUsuario",idUsuario);
                intentModificarExamen.putExtra("idExamen", itemSeleccionado);
                intentModificarExamen.putExtra("fecha",fechaExamen);
                startActivity(intentModificarExamen);
            }
        });

        btnEliminarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarExamen();
            }
        });

        btnResultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresarResultados();
            }
        });

        listadoExamenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionado=arrayExamenes.get(i).getIdExamen();
                fechaExamen = listado.get(i).split(" - ")[0];
                nombreMateria = listado.get(i).split(" - ")[1];
                indicador=i;
                idMateria = arrayExamenes.get(i).getIdMateria();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarListadoExamenes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_gestion_basica, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el menu de la barra de tareas de la aplicación.
        int id = item.getItemId();

        if (id == R.id.menu_barra_agregar) {
            final Intent intentAgregarExamen = new Intent(this, iu_agregar_examen.class);
            intentAgregarExamen.putExtra("idUsuario",idUsuario);
            startActivity(intentAgregarExamen);
            return true;
        }
        if (id == R.id.menu_barra_observar) {
            mostrarDatosExamen();
            return true;
        }
        if (id == R.id.menu_barra_modificar) {
            final Intent intentModificarExamen = new Intent(this, iu_modificar_examen.class);
            intentModificarExamen.putExtra("idExamen", itemSeleccionado);
            intentModificarExamen.putExtra("idUsuario",idUsuario);
            startActivity(intentModificarExamen);
            return true;
        }
        if (id == R.id.menu_barra_eliminar) {
            eliminarExamen();
            return true;
        }
        if (id == R.id.menu_barra_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void controlListView(ArrayList<String> array){
        if (array!=null) {
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listadoExamenes.setAdapter(itemsAdapter);
        }
    }

    public void cargarListadoExamenes(){
        if (listado !=null) {
            listado.clear();
        }
        arrayExamenes=gestorExamen.obtenerListadoExamenes();
        if (arrayExamenes!=null) {
            for (int i = 0; i < arrayExamenes.size(); i++) {
                listado.add(arrayExamenes.get(i).getFecha() + " - " + arrayExamenes.get(i).getMateria()+" - Resultado:"+arrayExamenes.get(i).getResultado());
            }
            controlListView(listado);
        }
    }

    public void eliminarExamen(){
        if(itemSeleccionado!=null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_examenes.this);
            builder.setMessage(R.string.mensaje_eliminar)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            gestorExamen.eliminarExamen(itemSeleccionado, fechaExamen);
                            listado.remove(indicador);
                            controlListView(listado);
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.show();
        }else{
            Toast.makeText(iu_examenes.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }

    public void mostrarDatosExamen() {
        if (itemSeleccionado != null) {
            ModeloExamen examen = gestorExamen.obtenerDatosExamenPorId(itemSeleccionado);
            ModeloEvento evento = gestorEvento.obtenerDatosEventoPorId(examen.getIdEvento(),examen.getFecha());
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_examenes.this);
            builder.setMessage(getString(R.string.datos_examen,examen.getFecha(),nombreMateria,evento.getHoraInicio(), evento.getHoraFin(), examen.getResultado()))
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.show();
            builder.setCancelable(true);
        } else {
            Toast.makeText(iu_examenes.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }

    public void ingresarResultados(){
        if (itemSeleccionado != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_examenes.this);
            builder.setTitle(getResources().getString(R.string.resultado));
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            builder.setView(input);
            builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    float valor = Float.parseFloat(input.getText().toString());
                    if (input.getText().toString().trim().isEmpty()==false && valor>=0 && valor<=10) {
                        ModeloExamen examen = gestorExamen.obtenerDatosExamenPorId(itemSeleccionado);
                        examen.setResultado(input.getText().toString());
                        gestorExamen.modificarExamen(examen.getIdExamen(), examen);
                        gestorPlanificador.registrarResultadosPlanificacion(examen.getIdExamen(),examen.getResultado());
                    }else {
                        Toast.makeText(iu_examenes.this, R.string.error_campos_vacios, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setCancelable(true);
            builder.show();
        } else {
            Toast.makeText(iu_examenes.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }

}
