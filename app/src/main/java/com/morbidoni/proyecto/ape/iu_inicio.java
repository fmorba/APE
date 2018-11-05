package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import modelos.ModeloEvento;
import servicios.GestorEvento;

public class iu_inicio extends AppCompatActivity {
    ImageButton btnAgenda, btnMateria, btnExamen, btnPlanificador, btnMetrica, btnArchivo;
    ListView listRecordatorios, listActividadesHoy;
    GestorEvento gestorEvento;
    int idUsuario;
    ArrayList<ModeloEvento> listadoEventosHoy = new ArrayList<>();
    ArrayList<ModeloEvento> listadoRecordatorios = new ArrayList<>();

    //Esta parte del programa detecta cual boton fue presionado y abre la actividad correspondiente.
    //No realiza tareas aparte del iniciado de otras ventanas.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarInicio);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle getuserID = getIntent().getExtras();
        idUsuario = getuserID.getInt(iu_login.EXTRA_MESSAGE);

        gestorEvento = new GestorEvento();

        final Intent intentAgenda = new Intent(this, iu_agenda.class);
        final Intent intentMateria = new Intent(this, iu_materias.class);
        final Intent intentExamen = new Intent(this,iu_examenes.class);
        final Intent intentPlanificador = new Intent(this, iu_planificador.class);
        final Intent intentMetricas = new Intent(this,iu_metricas.class);
        final Intent intentArchivo = new Intent(this, iu_archivos.class);

        btnAgenda = (ImageButton) findViewById(R.id.boton_agenda);
        btnMateria = (ImageButton) findViewById(R.id.boton_materia);
        btnExamen = (ImageButton) findViewById(R.id.boton_examenes);
        btnPlanificador = (ImageButton) findViewById(R.id.boton_planificador);
        btnMetrica = (ImageButton) findViewById(R.id.boton_metricas);
        btnArchivo = (ImageButton) findViewById(R.id.boton_archivos);
        listRecordatorios = (ListView) findViewById(R.id.lista_recordatorios);
        listActividadesHoy = (ListView) findViewById(R.id.lista_eventos_hoy);


        btnAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentAgenda.putExtra(iu_login.EXTRA_MESSAGE, idUsuario);
                startActivity(intentAgenda);
            }
        });

        btnMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMateria.putExtra(iu_login.EXTRA_MESSAGE, idUsuario);
                startActivity(intentMateria);
            }
        });

        btnExamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentExamen.putExtra(iu_login.EXTRA_MESSAGE, idUsuario);
                startActivity(intentExamen);
            }
        });

        btnPlanificador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentPlanificador);
            }
        });

        btnMetrica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentMetricas);
            }
        });

        btnArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentArchivo);
            }
        });

        listActividadesHoy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(iu_inicio.this, listadoEventosHoy.get(i).getDescripcionEvento().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        listRecordatorios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(iu_inicio.this, listadoRecordatorios.get(i).getDescripcionEvento().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        String hoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        EventosDelDia(hoy,idUsuario);
        Recordatorios(hoy,idUsuario);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String hoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        EventosDelDia(hoy, idUsuario);
        Recordatorios(hoy, idUsuario);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el meno de la barra de tareas de la aplicación.
        int id = item.getItemId();

        if (id == R.id.menu_inicio_agenda) {
            final Intent intentAgenda = new Intent(this, iu_agenda.class);
            intentAgenda.putExtra(iu_login.EXTRA_MESSAGE, idUsuario);
            startActivity(intentAgenda);
            return true;
        }
        if (id == R.id.menu_inicio_materias) {
            final Intent intentMateria = new Intent(this, iu_materias.class);
            intentMateria.putExtra(iu_login.EXTRA_MESSAGE, idUsuario);
            startActivity(intentMateria);
            return true;
        }
        if (id == R.id.menu_inicio_examen) {
            final Intent intentExamen = new Intent(this,iu_examenes.class);
            intentExamen.putExtra(iu_login.EXTRA_MESSAGE, idUsuario);
            startActivity(intentExamen);
            return true;
        }
        if (id == R.id.menu_inicio_planificador) {
            final Intent intentPlanificador = new Intent(this, iu_planificador.class);
            startActivity(intentPlanificador);
            return true;
        }
        if (id == R.id.menu_inicio_metricas) {
            final Intent intentMetricas = new Intent(this,iu_metricas.class);
            intentMetricas.putExtra(iu_login.EXTRA_MESSAGE, idUsuario);
            startActivity(intentMetricas);
            return true;
        }
        if (id == R.id.menu_inicio_archivos) {
            final Intent intentArchivo = new Intent(this, iu_archivos.class);
            intentArchivo.putExtra(iu_login.EXTRA_MESSAGE, idUsuario);
            startActivity(intentArchivo);
            return true;
        }
        if (id == R.id.menu_inicio_configuracion) {
            Intent intentConfiguracion= new Intent(this,iu_configuracion.class);
            intentConfiguracion.putExtra("id",idUsuario);
            startActivity(intentConfiguracion);
            return true;
        }
        if (id == R.id.menu_inicio_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Recordatorios(String hoy, int usuario){
        ArrayList<String> array = new ArrayList<>();
        listadoRecordatorios = gestorEvento.ObtenerRecordatorios(hoy,usuario);
        if (listadoRecordatorios!=null) {
            for (ModeloEvento modelo:listadoRecordatorios) {
                array.add(modelo.getNombreEvento()+" - "+modelo.getFechaEvento()+" - "+modelo.getHoraInicioEvento()+" - "+modelo.getHoraFinEvento());
            }
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listRecordatorios.setAdapter(itemsAdapter);
        }else {
            array.clear();
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listRecordatorios.setAdapter(itemsAdapter);
        }
    }

    public void EventosDelDia(String hoy, int usuario){
        ArrayList<String> array = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dia= sdf.format(hoy);
        listadoEventosHoy=gestorEvento.ObtenerHorariosSegunFechas(hoy,dia,usuario);
        if (listadoEventosHoy!=null) {
            for (ModeloEvento modelo:listadoEventosHoy) {
                array.add(modelo.getNombreEvento()+" - "+modelo.getHoraInicioEvento()+" - "+modelo.getHoraFinEvento());
            }
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listActividadesHoy.setAdapter(itemsAdapter);
        }else {
            array.clear();
            ArrayAdapter itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            itemsAdapter.notifyDataSetChanged();
            listActividadesHoy.setAdapter(itemsAdapter);
        }
    }

}
