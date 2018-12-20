package com.morbidoni.proyecto.ape;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import modelos.ModeloEvento;
import servicios.GestorEvento;

public class iu_agenda extends AppCompatActivity {
    ImageButton btnAgregarEvento, btnModificarEvento, btnEliminarEvento;
    CalendarView calendario;
    ListView listEventos;
    GestorEvento gestorEvento;
    ArrayList<ModeloEvento> listadoEventos;
    ArrayList<String> arrayEntradas, arrayID;
    int indicador;
    String idUsuario, itemSeleccionado, fechaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_agenda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAgenda);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getString("idUsuario");
        fechaSeleccionada =  new SimpleDateFormat("yyyy-MM-dd").format(new Date()); //fecha actual

        arrayEntradas = new ArrayList<String>();
        arrayID = new ArrayList<String>();

        btnAgregarEvento = (ImageButton) findViewById(R.id.botonAgregarEvento);
        btnModificarEvento = (ImageButton) findViewById(R.id.botonModificarEvento);
        btnEliminarEvento = (ImageButton) findViewById(R.id.botonEliminarEvento);
        calendario = (CalendarView) findViewById(R.id.calendario);
        listEventos = (ListView) findViewById(R.id.listaEventosAgenda);

        final Intent intentAgregar = new Intent(this,iu_agregar_eventos.class);
        final Intent intentModificar = new Intent(this, iu_modificar_eventos.class);
        gestorEvento = new GestorEvento();

        btnAgregarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentAgregar.putExtra("idUsuario",idUsuario);
                startActivity(intentAgregar);
            }
        });

        btnModificarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemSeleccionado!=null) {
                    intentModificar.putExtra("idEvento", itemSeleccionado);
                    intentModificar.putExtra("idUsuario",idUsuario);
                    intentModificar.putExtra("fechaEvento",fechaSeleccionada);
                    startActivity(intentModificar);
                }else{
                    Toast.makeText(iu_agenda.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
                }
                ControlListView(arrayEntradas);
            }
        });

        btnEliminarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                
                EliminarEvento();
                ControlListView(arrayEntradas);
            }
        });

        listEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionado= arrayID.get(i);
                indicador=i;
            }
        });

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int año, int mes, int dia) {
                itemSeleccionado=null;
                if (arrayID!=null && arrayEntradas!=null) {
                    arrayID.clear();
                    arrayEntradas.clear();
                    ControlListView(arrayEntradas);
                }
                mes+=1;
                if (mes<10){fechaSeleccionada=año+"-"+"0"+mes+"-"+dia;}
                else {fechaSeleccionada=año+"-"+mes+"-"+dia;}
                try{
                    listadoEventos = new ArrayList<>();
                    arrayID = gestorEvento.obtenerIdSegunFechas(fechaSeleccionada);
                    listadoEventos = gestorEvento.obtenerHorariosSegunFechas(fechaSeleccionada);

                    for (ModeloEvento evento:listadoEventos) {
                        arrayEntradas.add(evento.getNombre()+" - "+evento.getHoraInicio()+" - "+evento.getHoraFin());
                    }

                    ControlListView(arrayEntradas);

                }
                catch (NullPointerException e){
                    ControlListView(null);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_agenda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el meno de la barra de tareas de la aplicación.
        int id = item.getItemId();

        if (id == R.id.menu_agenda_agregar) {
            final Intent intentAgregar = new Intent(this,iu_agregar_eventos.class);
            intentAgregar.putExtra(iu_login.EXTRA_MESSAGE,idUsuario);
            startActivity(intentAgregar);
            return true;
        }
        if (id == R.id.menu_agenda_modificar) {
            final Intent intentModificar = new Intent(this, iu_modificar_eventos.class);
            if(itemSeleccionado!=null) {
                intentModificar.putExtra("mensaje", itemSeleccionado); //Se usa el nombre mensaje para definir el enviao de datos a otras clases.
                intentModificar.putExtra(iu_login.EXTRA_MESSAGE,idUsuario);
                startActivity(intentModificar);
            }else{
                Toast.makeText(iu_agenda.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
            }
            ControlListView(arrayEntradas);
        }
        if (id == R.id.menu_agenda_eliminar) {
            EliminarEvento();
            ControlListView(arrayEntradas);
            return true;
        }
        if (id == R.id.menu_agenda_ayuda) {
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
            listEventos.setAdapter(itemsAdapter);
        }else{
            listEventos.setAdapter(null);
        }
    }

    private void EliminarEvento(){

        if(itemSeleccionado!=null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_agenda.this);
            builder.setMessage(R.string.mensaje_eliminar)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            gestorEvento.eliminarEvento(itemSeleccionado,fechaSeleccionada);
                            arrayEntradas.remove(indicador);
                            ControlListView(arrayEntradas);
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.show();
        }else{
            Toast.makeText(iu_agenda.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String hoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (arrayID!=null && arrayEntradas!=null) {
            arrayID.clear();
            arrayEntradas.clear();
        }
        try{
            listadoEventos = new ArrayList<>();
            arrayID = gestorEvento.obtenerIdSegunFechas(hoy);
            listadoEventos = gestorEvento.obtenerHorariosSegunFechas(hoy);

            for (ModeloEvento evento:listadoEventos) {
                arrayEntradas.add(evento.getNombre()+" - "+evento.getHoraInicio()+" - "+evento.getHoraFin());
            }

            ControlListView(arrayEntradas);
        }
        catch (NullPointerException e){
            ControlListView(null);
        }
    }
}
