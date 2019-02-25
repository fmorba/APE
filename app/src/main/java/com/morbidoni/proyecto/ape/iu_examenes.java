package com.morbidoni.proyecto.ape;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import modelos.ModeloEvento;
import modelos.ModeloExamen;
import servicios.GestorArchivos;
import servicios.GestorEvento;
import servicios.GestorExamen;
import servicios.GestorMateria;
import servicios.GestorPlanificador;

/**
 * Esta clase en particular se encarga del manejo de la interfaz de los exámenes del usuario, donde
 * el mismo puede obtener información de los exámenes registrados e iniciar tareas de gestión sobre
 * los mismos.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_examenes extends AppCompatActivity {
    ImageButton btnAgregarExamen, btnObservarExamen, btnModificarExamen, btnEliminarExamen, btnResultado;
    ListView listadoExamenes;
    int indicador;
    ProgressBar progressBar;
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
        progressBar = (ProgressBar) findViewById(R.id.progressBarExamenes);
        progressBar.setMax(10);
        gestorExamen = new GestorExamen();
        gestorMateria = new GestorMateria();
        gestorEvento = new GestorEvento();
        gestorPlanificador = new GestorPlanificador();

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
                if (itemSeleccionado!=null) {
                    intentModificarExamen.putExtra("idUsuario", idUsuario);
                    intentModificarExamen.putExtra("idExamen", itemSeleccionado);
                    intentModificarExamen.putExtra("fecha", fechaExamen);
                    startActivity(intentModificarExamen);
                }else {
                    Toast.makeText(iu_examenes.this, getResources().getString(R.string.error_objeto_no_seleccionado), Toast.LENGTH_SHORT).show();
                }
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
        progressBar.setVisibility(View.VISIBLE);
        Thread linea = new Thread (new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cargarListadoExamenes();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        linea.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_examen, menu);
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
            if (itemSeleccionado!=null) {
                intentModificarExamen.putExtra("idUsuario", idUsuario);
                intentModificarExamen.putExtra("idExamen", itemSeleccionado);
                intentModificarExamen.putExtra("fecha", fechaExamen);
                startActivity(intentModificarExamen);
            }else {
                Toast.makeText(iu_examenes.this, getResources().getString(R.string.error_objeto_no_seleccionado), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == R.id.menu_barra_eliminar) {
            eliminarExamen();
            return true;
        }
        if (id == R.id.menu_barra_buscar_archivos) {
            buscarArchivosRelevantes();
            return true;
        }
        if (id == R.id.menu_barra_consejos) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            intentAyuda.putExtra("ayuda", "consejos");
            startActivity(intentAyuda);
            return true;
        }

        if (id == R.id.menu_barra_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            intentAyuda.putExtra("ayuda", "examenes");
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Esta clase controla la lista de datos que se muestra en la ventana del usuario, y realiza
     * las actualizaciones pertinentes de la misma.
     *
     * @param array Array de Strings que necesitan ser ingresados en el ListView.
     */
    private void controlListView(ArrayList<String> array){
        if (array!=null) {
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listadoExamenes.setAdapter(itemsAdapter);
        }
    }

    /**
     * Método que obtiene la lista de exámenes registrados y genera un array de strings para su
     * presentación en la interfaz.
     */
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

    /**
     * Método que maneja la eliminación de los registro de exámenes, luego de recibir la
     * confirmación por parte del usuario. La eliminación de los registros se realiza mediante los
     * gestores de datos.
     */
    public void eliminarExamen(){
        if(itemSeleccionado!=null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_examenes.this);
            builder.setMessage(R.string.mensaje_eliminar)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            gestorExamen.eliminarExamen(itemSeleccionado, fechaExamen);
                            gestorEvento.eliminarEvento(itemSeleccionado, fechaExamen);
                            arrayExamenes.remove(indicador);
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

    /**
     * Método que presenta al usuario, mediante la interfaz, la información del examen seleccionado.
     */
    public void mostrarDatosExamen() {
        if (itemSeleccionado != null) {
            String mensaje;
            ModeloExamen examen = gestorExamen.obtenerDatosExamenPorId(itemSeleccionado);
            ModeloEvento evento = gestorEvento.obtenerDatosEventoPorId(examen.getIdEvento(),examen.getFecha());
            if (evento==null){
                mensaje=getString(R.string.datos_examen,examen.getFecha(),nombreMateria,"Horario no definido", "Horario no definido", examen.getResultado(), examen.getTemas());
            }else{
                mensaje=getString(R.string.datos_examen,examen.getFecha(),nombreMateria,evento.getHoraInicio(), evento.getHoraFin(), examen.getResultado(), examen.getTemas());
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_examenes.this);
            builder.setMessage(mensaje)
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

    /**
     * Método que permite ingresar el valor del resultado a un examen registrado, así como
     * modificar un previo valor colocado.
     */
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
                        gestorEvento.eliminarEvento(examen.getIdEvento(), examen.getFecha());
                        cargarListadoExamenes();
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

    /**
     * Método llamado por el menú en la barra de tareas, permite al usuario ver una lista de los
     * archivos almacenados que compartan palabras claves con los temas del examen seleccionado.
     */
    public void buscarArchivosRelevantes(){
        GestorArchivos gestorArchivos = new GestorArchivos();
        if(itemSeleccionado!=null) {
            String claveOriginal = arrayExamenes.get(indicador).getTemas();
            claveOriginal=claveOriginal.replace("[","");
            claveOriginal=claveOriginal.replace("]","");
            if (claveOriginal.trim().isEmpty()){
                Toast.makeText(iu_examenes.this, R.string.error_examen_sin_clave, Toast.LENGTH_SHORT).show();
            }else {
                ArrayList<String> claves = new ArrayList<>(Arrays.asList(claveOriginal.split(",")));
                ArrayList<String> resultados = gestorArchivos.obtenerListadoArchivosPorClaves(claves);

                final AlertDialog.Builder builder = new AlertDialog.Builder(iu_examenes.this);
                Context context = iu_examenes.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final ListView listaArchivos= new ListView(context);
                ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultados);
                itemsAdapter.notifyDataSetChanged();
                listaArchivos.setAdapter(itemsAdapter);
                layout.addView(listaArchivos);
                builder.setView(layout);

                builder.setMessage(getResources().getString(R.string.archivos_encontrados,resultados.size()))
                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });

                builder.setCancelable(true);
                builder.show();
            }
        }else{
            Toast.makeText(iu_examenes.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }


    }

}
