package com.morbidoni.proyecto.ape;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

/**
 * Esta es la clase que maneja la interfaz principal de la agenda, y envía al usuario a las
 * actividades correspondiente según sus necesidades.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_agenda extends AppCompatActivity {
    final int MY_PERMISSIONS_REQUEST_WRITE=0;
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
        pedirPermisosEscribir();
        gestorEvento = new GestorEvento(this);

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
                controlListView(arrayEntradas);
            }
        });

        btnEliminarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                
                EliminarEvento();
                controlListView(arrayEntradas);
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
                String diaFecha;
                itemSeleccionado=null;
                if (arrayID!=null && arrayEntradas!=null) {
                    arrayID.clear();
                    arrayEntradas.clear();
                    controlListView(arrayEntradas);
                }
                mes+=1;
                if (dia<10){
                    diaFecha="0"+dia;
                }else {
                    diaFecha=dia+"";
                }
                if (mes<10){fechaSeleccionada=año+"-"+"0"+mes+"-"+diaFecha;}
                else {fechaSeleccionada=año+"-"+mes+"-"+diaFecha;}
                try{
                    listadoEventos = new ArrayList<>();
                    arrayID = gestorEvento.obtenerIdSegunFechas(fechaSeleccionada);
                    listadoEventos = gestorEvento.obtenerEventosSegunFechas(fechaSeleccionada);

                    for (ModeloEvento evento:listadoEventos) {
                        arrayEntradas.add(evento.getNombre()+" - "+evento.getHoraInicio()+" - "+evento.getHoraFin());
                    }

                    controlListView(arrayEntradas);

                }
                catch (NullPointerException e){
                    controlListView(null);
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
        // Esta parte controla el menu de la barra de tareas de la aplicación.
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
            controlListView(arrayEntradas);
        }
        if (id == R.id.menu_agenda_eliminar) {
            EliminarEvento();
            controlListView(arrayEntradas);
            return true;
        }
        if (id == R.id.menu_agenda_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            intentAyuda.putExtra("ayuda", "agenda");
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
            listEventos.setAdapter(itemsAdapter);
        }else{
            listEventos.setAdapter(null);
        }
    }

    /**
     *Ese método es llamado cuando el usuario decide eliminar un evento, primero determina que allá
     * seleccionado un evento de la lista presente al usuario, luego genera un AlertDialog para
     * confirmar la decisión del usuario. Si se recibe la confirmación, se llama al método
     * eliminarEvento del Gestor de eventos, y se actualiza la vista.
     */
    private void EliminarEvento(){
        if(itemSeleccionado!=null && arrayEntradas.size()>0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_agenda.this);
            builder.setMessage(R.string.mensaje_eliminar)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            gestorEvento.eliminarEvento(itemSeleccionado,fechaSeleccionada);
                            arrayID.remove(indicador);
                            arrayEntradas.remove(indicador);
                            controlListView(arrayEntradas);
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
            listadoEventos = gestorEvento.obtenerEventosSegunFechas(hoy);

            for (ModeloEvento evento:listadoEventos) {
                arrayEntradas.add(evento.getNombre()+" - "+evento.getHoraInicio()+" - "+evento.getHoraFin());
            }

            controlListView(arrayEntradas);
        }
        catch (NullPointerException e){
            controlListView(null);
        }
    }

    /**
     * Este método esta dedicado al pedido de los permisos necesarios para leer y/o escribir sobre
     * la aplicación de calendario existente en el dispositivo.
     */
    public void pedirPermisosEscribir(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CALENDAR)) {


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CALENDAR},
                        MY_PERMISSIONS_REQUEST_WRITE);
            }
        }
    }
}
