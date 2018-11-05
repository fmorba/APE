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
import android.widget.Toast;

import java.util.ArrayList;

import modelos.ModeloMateria;
import servicios.GestorMateria;

public class iu_materias extends AppCompatActivity {
    GestorMateria gestorMateria;
    ImageButton btnAgregarMateria, btnVerMateria, btnEliminarMateria, btnModificarMateria;
    ListView listadoMaterias;
    ArrayList<String> listado = new ArrayList<>();
    String itemSeleccionado;
    int indicador, idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_materias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMaterias);
        setSupportActionBar(toolbar);

        final Intent intent= getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getInt(iu_login.EXTRA_MESSAGE);

        btnAgregarMateria = (ImageButton) findViewById(R.id.boton_agregar_materia);
        btnVerMateria = (ImageButton) findViewById(R.id.boton_ver_materia);
        btnModificarMateria = (ImageButton) findViewById(R.id.boton_modificar_materia);
        btnEliminarMateria = (ImageButton) findViewById(R.id.boton_eliminar_materia);
        listadoMaterias = (ListView) findViewById(R.id.listado_menu_materias);
        gestorMateria = new GestorMateria();

        final Intent intentAgregarMateria = new Intent(this,iu_agregar_materia.class);
        final Intent intentModificarMateria = new Intent(this,iu_modificar_materia.class);
        CargarListadoMaterias();

        btnAgregarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentAgregarMateria.putExtra(iu_login.EXTRA_MESSAGE,idUsuario);
                startActivity(intentAgregarMateria);
            }
        });

        btnVerMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MostrarDatosMateria();
            }
        });

        btnModificarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemSeleccionado!=null) {
                    intentModificarMateria.putExtra("mensaje", itemSeleccionado); //Se usa el nombre mensaje para definir el enviao de datos a otras clases.
                    intentModificarMateria.putExtra(iu_login.EXTRA_MESSAGE,idUsuario);
                    startActivity(intentModificarMateria);
                }else{
                    Toast.makeText(iu_materias.this, R.string.mensaje_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
                }
                ControlListView(listado);
            }
        });

        btnEliminarMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EliminarMateria();
            }
        });

        listadoMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionado=listado.get(i).split(" - ")[0];
                indicador=i;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        CargarListadoMaterias();
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
            intentAgregarMateria.putExtra(iu_login.EXTRA_MESSAGE,idUsuario);
            startActivity(intentAgregarMateria);
            return true;
        }
        if (id == R.id.menu_barra_observar) {
            MostrarDatosMateria();
            return true;
        }
        if (id == R.id.menu_barra_modificar) {
            final Intent intentModificarMateria = new Intent(this,iu_modificar_materia.class);
            intentModificarMateria.putExtra("mensaje", itemSeleccionado);
            startActivity(intentModificarMateria);
            return true;
        }
        if (id == R.id.menu_barra_eliminar) {
            EliminarMateria();
            return true;
        }
        if (id == R.id.menu_barra_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void CargarListadoMaterias(){
        listado.clear();
        listado=gestorMateria.ObtenerListadoMaterias(idUsuario);
        for (int i = 0; i < listado.size(); i++) {
            String s = listado.get(i);
            s.replace("-000-"," - ");
            listado.add(i,s);
        }
        ControlListView(listado);
    }

    public void EliminarMateria(){
        if(itemSeleccionado!=null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_materias.this);
            builder.setMessage(R.string.mensaje_eliminar)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int aux = Integer.valueOf(itemSeleccionado);
                            gestorMateria.EliminarMateria(Integer.valueOf(itemSeleccionado));
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
            Toast.makeText(iu_materias.this, R.string.mensaje_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }

    private void ControlListView(ArrayList<String> array){
        if (array!=null) {
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listadoMaterias.setAdapter(itemsAdapter);
        }
    }

    public void MostrarDatosMateria() {
        if (itemSeleccionado != null) {
            ModeloMateria materia = gestorMateria.ObtenerDatosMateria(itemSeleccionado);
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_materias.this);
            builder.setTitle(materia.getNombre());
            builder.setMessage(getString(R.string.datos_materia,materia.getTipo(),materia.getDificultad(),materia.getEstado()))
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.show();
            builder.setCancelable(true);
        } else {
            Toast.makeText(iu_materias.this, R.string.mensaje_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }
}
