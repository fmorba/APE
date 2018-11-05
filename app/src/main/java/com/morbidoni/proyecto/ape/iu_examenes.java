package com.morbidoni.proyecto.ape;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import java.util.ArrayList;

import modelos.ModeloEvento;
import modelos.ModeloExamen;
import servicios.GestorEvento;
import servicios.GestorExamen;

public class iu_examenes extends AppCompatActivity {
    ImageButton btnAgregarExamen, btnObservarExamen, btnModificarExamen, btnEliminarExamen;
    ListView listadoExamenes;
    int idUsuario, indicador, idMateria, idEvento;
    String itemSeleccionado, nombreMateria;
    ArrayList<String> listado = new ArrayList<>();
    ArrayList<String> listadoID = new ArrayList<>();
    GestorExamen gestorExamen;
    GestorEvento gestorEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_examenes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarExamenes);
        setSupportActionBar(toolbar);

        final Intent intent= getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getInt(iu_login.EXTRA_MESSAGE);

        btnAgregarExamen = (ImageButton) findViewById(R.id.boton_agregar_examen);
        btnObservarExamen = (ImageButton) findViewById(R.id.boton_ver_examen);
        btnModificarExamen = (ImageButton) findViewById(R.id.boton_modificar_examen);
        btnEliminarExamen = (ImageButton) findViewById(R.id.boton_eliminar_examen);
        listadoExamenes = (ListView) findViewById(R.id.listadoMenuExamenes);
        gestorExamen = new GestorExamen();
        gestorEvento = new GestorEvento();

        CargarListadoExamenes();

        final Intent intentAgregarExamen = new Intent(this, iu_agregar_examen.class);
        final Intent intentModificarExamen = new Intent(this, iu_modificar_examen.class);
        final Intent intentObservarExamen = new Intent(this,iu_datos_examenes.class);

        btnAgregarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentAgregarExamen.putExtra(iu_login.EXTRA_MESSAGE,idUsuario);
                startActivity(intentAgregarExamen);
            }
        });

        btnObservarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDatosExamen();
            }
        });

        btnModificarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentModificarExamen.putExtra(iu_login.EXTRA_MESSAGE,idUsuario);
                intentModificarExamen.putExtra("mensaje", itemSeleccionado);
                startActivity(intentModificarExamen);
            }
        });

        btnEliminarExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EliminarExamen();
            }
        });

        listadoExamenes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionado=listado.get(i).split(" - ")[0];
                nombreMateria=listado.get(i).split(" - ")[2];
                indicador=i;
                idEvento = Integer.valueOf(listadoID.get(i).split("-000-")[1].toString());
                idMateria = Integer.valueOf(listadoID.get(i).split("-000-")[2].toString());
            }
        });
    }

    @Override
    protected void onResume() {
        CargarListadoExamenes();
        super.onResume();
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
            intentAgregarExamen.putExtra(iu_login.EXTRA_MESSAGE,idUsuario);
            startActivity(intentAgregarExamen);
            return true;
        }
        if (id == R.id.menu_barra_observar) {
            MostrarDatosExamen();
            return true;
        }
        if (id == R.id.menu_barra_modificar) {
            final Intent intentModificarExamen = new Intent(this, iu_modificar_examen.class);
            intentModificarExamen.putExtra("mensaje", itemSeleccionado);
            intentModificarExamen.putExtra(iu_login.EXTRA_MESSAGE,idUsuario);
            startActivity(intentModificarExamen);
            return true;
        }
        if (id == R.id.menu_barra_eliminar) {
            EliminarExamen();
            return true;
        }
        if (id == R.id.menu_barra_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ControlListView(ArrayList<String> array){
        if (array!=null) {
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listadoExamenes.setAdapter(itemsAdapter);
        }
    }

    public void CargarListadoExamenes(){
        listado.clear();
        listadoID.clear();
        listado=gestorExamen.ObtenerListadoExamenes(idUsuario+"");
        listadoID=gestorExamen.ObtenerIdsExamenes(idUsuario+"");
        for (int i = 0; i < listado.size(); i++) {
            String s = listado.get(i);
            s.replace("-000-"," - ");
            listado.add(i,s);
        }
        ControlListView(listado);
    }

    public void EliminarExamen(){
        if(itemSeleccionado!=null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_examenes.this);
            builder.setMessage(R.string.mensaje_eliminar)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int aux = Integer.valueOf(itemSeleccionado);
                            gestorExamen.EliminarExamen(Integer.valueOf(itemSeleccionado));
                            gestorEvento.EliminarEvento(idEvento);
                            listado.remove(indicador);
                            ControlListView(listado);
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.show();
        }else{
            Toast.makeText(iu_examenes.this, R.string.mensaje_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }

    public void MostrarDatosExamen() {
        if (itemSeleccionado != null) {
            ModeloExamen examen = gestorExamen.ObtenerDatosExamenPorId(itemSeleccionado);
            ModeloEvento evento = gestorEvento.ObtenerDatosEventoPorId(idEvento+"");
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_examenes.this);
            builder.setMessage(getString(R.string.datos_examen,examen.getFecha(),nombreMateria,evento.getHoraInicioEvento(), evento.getHoraFinEvento(), examen.getResultado()))
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.show();
            builder.setCancelable(true);
        } else {
            Toast.makeText(iu_examenes.this, R.string.mensaje_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }

}
