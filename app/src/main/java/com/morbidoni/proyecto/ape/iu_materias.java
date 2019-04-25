package com.morbidoni.proyecto.ape;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import modelos.ModeloMateria;
import servicios.GestorMateria;

/**
 * Clase que maneja el control de la interfaz de materias del usuario, así como realiza las tareas
 * de administración de las mismas.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_materias extends AppCompatActivity {
    GestorMateria gestorMateria;
    ImageButton btnAgregarMateria, btnVerMateria, btnEliminarMateria, btnModificarMateria;
    ListView listadoMaterias;
    ProgressBar progressBar;
    ArrayList<ModeloMateria> arrayMaterias = new ArrayList<>();
    ArrayList<String> listado = new ArrayList<>();
    String itemSeleccionado, idUsuario;
    int indicador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_materias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMaterias);
        setSupportActionBar(toolbar);

        final Intent intent= getIntent();

        btnAgregarMateria = (ImageButton) findViewById(R.id.boton_agregar_materia);
        btnVerMateria = (ImageButton) findViewById(R.id.boton_ver_materia);
        btnModificarMateria = (ImageButton) findViewById(R.id.boton_modificar_materia);
        btnEliminarMateria = (ImageButton) findViewById(R.id.boton_eliminar_materia);
        listadoMaterias = (ListView) findViewById(R.id.listado_menu_materias);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMaterias);
        progressBar.setMax(10);
        gestorMateria = new GestorMateria();

        final Intent intentAgregarMateria = new Intent(this,iu_agregar_materia.class);
        final Intent intentModificarMateria = new Intent(this,iu_modificar_materia.class);

        btnAgregarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentAgregarMateria.putExtra("idUsuario",idUsuario);
                startActivity(intentAgregarMateria);
            }
        });

        btnVerMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDatosMateria();
            }
        });

        btnModificarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemSeleccionado!=null) {
                    intentModificarMateria.putExtra("idMateria", itemSeleccionado);
                    intentModificarMateria.putExtra("idUsuario",idUsuario);
                    startActivity(intentModificarMateria);
                }else{
                    Toast.makeText(iu_materias.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
                }
                controlListView(listado);
            }
        });

        btnEliminarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarMateria();
            }
        });

        listadoMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionado=arrayMaterias.get(i).getIdMateria();
                indicador=i;
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
                        cargarListadoMaterias();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        linea.start();
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
            final Intent intentAgregarMateria = new Intent(this,iu_agregar_materia.class);
            intentAgregarMateria.putExtra("idUsuario",idUsuario);
            startActivity(intentAgregarMateria);
            return true;
        }
        if (id == R.id.menu_barra_observar) {
            mostrarDatosMateria();
            return true;
        }
        if (id == R.id.menu_barra_modificar) {
            final Intent intentModificarMateria = new Intent(this,iu_modificar_materia.class);
            if (itemSeleccionado!=null) {
                intentModificarMateria.putExtra("idMateria", itemSeleccionado);
                intentModificarMateria.putExtra("idUsuario", idUsuario);
                startActivity(intentModificarMateria);
            }else{
                Toast.makeText(this, getResources().getString(R.string.error_objeto_no_seleccionado), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == R.id.menu_barra_eliminar) {
            eliminarMateria();
            return true;
        }
        if (id == R.id.menu_barra_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            intentAyuda.putExtra("ayuda", "materias");
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que obtiene la lista de materias registradas y genera un array de strings para su
     * presentación en la interfaz.
     */
    public void cargarListadoMaterias(){
        listado.clear();
        arrayMaterias=gestorMateria.obtenerListadoMaterias();
        if (arrayMaterias!=null) {
            for (int i = 0; i < arrayMaterias.size(); i++) {
                listado.add(arrayMaterias.get(i).getNombre() + " - " + arrayMaterias.get(i).getEstado());
            }
            controlListView(listado);
        }
    }

    /**
     * Método que se encarga de eliminar el registro de una materia seleccionada, pidiendo
     * previamente una confirmación por parte del mismo.
     */
    public void eliminarMateria(){
        if(itemSeleccionado!=null && arrayMaterias.size()>0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_materias.this);
            builder.setMessage(R.string.mensaje_eliminar)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            gestorMateria.eliminarMateria(itemSeleccionado);
                            arrayMaterias.remove(indicador);
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
            Toast.makeText(iu_materias.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
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
            listadoMaterias.setAdapter(itemsAdapter);
        }
    }

    /**
     * Método que presenta al usuario los datos almacenados sobre la materia seleccionada por el
     * mismo, en la lista presente en la interfaz.
     */
    public void mostrarDatosMateria() {
        if (itemSeleccionado != null) {
            ModeloMateria materia = gestorMateria.obtenerDatosMateria(itemSeleccionado);
            if(materia!=null) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(iu_materias.this);
                builder.setTitle(materia.getNombre());
                builder.setMessage(getString(R.string.datos_materia, materia.getTipo(), materia.getDificultad(), materia.getEstado()))
                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.show();
                builder.setCancelable(true);
            }
        } else {
            Toast.makeText(iu_materias.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }

}
