package com.morbidoni.proyecto.ape;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import modelos.ModeloEvento;
import modelos.ModeloExamen;
import modelos.ModeloPlanEstudio;
import modelos.ModeloPlanificacion;
import servicios.GestorEvento;
import servicios.GestorExamen;
import servicios.GestorPlanificador;

/**
 * Clase que controla la interfaz cuya función es verificar el estado de una planificación
 * existente y activa, para que el usuario pueda confirmar que plan de estudio cumplió o no.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_revisar_plan extends AppCompatActivity {

    Spinner planificaciones;
    ListView listaPlanesDeEstudio;
    Button btnConfirmar;
    GestorPlanificador gestorPlanificador;
    ProgressBar progressBar;
    GestorExamen gestorExamen;
    GestorEvento gestorEvento;
    ArrayList<ModeloPlanEstudio> listadoPlanesDeEstudio = new ArrayList<>();
    ArrayList<ModeloPlanificacion> listadoPlanificaciones = new ArrayList<>();
    List<iu_revisar_plan.Item> items;
    iu_revisar_plan.ItemsListAdapter myItemsListAdapter;
    ModeloPlanificacion planificacion;
    ModeloExamen examen;
    final int MY_PERMISSIONS_REQUEST_WRITE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_revisar_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRevisar);
        setSupportActionBar(toolbar);

        planificaciones = (Spinner) findViewById(R.id.spListadoPlanificaciones);
        btnConfirmar = (Button) findViewById(R.id.btnConfirmarEstadoPlanes);
        listaPlanesDeEstudio = (ListView) findViewById(R.id.listaPlanesEstudio);
        pedirPermisosEscribir();
        gestorExamen = new GestorExamen();
        gestorPlanificador = new GestorPlanificador();
        gestorEvento = new GestorEvento(this);

        cargarPlanificaciones();

        planificaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                planificacion = listadoPlanificaciones.get(i);
                examen = gestorExamen.obtenerDatosExamenPorId(planificacion.getIdExamen());
                CargarPlanesDeEstudio(planificacion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              registrarEstadoPlanificacion();
            }
        });

    }

    /**
     * Método que carga en la interfaz gráfica, el listado de las planificaciones activas para que
     * el usuario pueda escoger, al elegir una se actualiza el ListView inferior para mostrar el
     * listado de planes de estudio correspondiente a la planificación escogida.
     */
    public void cargarPlanificaciones(){
        ArrayList<String> listado = new ArrayList<>();
        ArrayList<ModeloPlanificacion> PlaniList = new ArrayList<>();
        listadoPlanificaciones = gestorPlanificador.obtenerListadoPlanificaciones();
        if (listadoPlanificaciones==null){
            btnConfirmar.setEnabled(false);
        }else {
            for (ModeloPlanificacion modelo : listadoPlanificaciones) {
                if (modelo.getResultado()==0) {
                    ModeloExamen examen = gestorExamen.obtenerDatosExamenPorId(modelo.getIdExamen());
                    listado.add(examen.getFecha() + " - " + examen.getMateria());
                }else {
                    PlaniList.add(modelo);
                }
            }
            listadoPlanificaciones.removeAll(PlaniList);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listado);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            planificaciones.setAdapter(adapter);
        }
    }

    /**
     * Método que obtiene la lista de planes de estudio correspondiente a la planificacion elegida
     * y genera un array de strings para su presentación en la interfaz.
     */
    public void CargarPlanesDeEstudio(ModeloPlanificacion modelo){
        ArrayList<String> listado = new ArrayList<>();
        listadoPlanesDeEstudio = gestorPlanificador.obtenerPlanesDeEstudioRegistrados(modelo);
        initItems();
        myItemsListAdapter = new iu_revisar_plan.ItemsListAdapter(this, items);
        listaPlanesDeEstudio.setAdapter(myItemsListAdapter);
    }

    /**
     * Método que carga las lista en el ListView personalizado para incluir checkpoints.
     */
    private void initItems(){
        items = new ArrayList<iu_revisar_plan.Item>();

        for(int i=0; i<listadoPlanesDeEstudio.size(); i++){
            ModeloEvento evento = gestorEvento.obtenerDatosEventoPorId(listadoPlanesDeEstudio.get(i).getIdEvento(),listadoPlanesDeEstudio.get(i).getFecha());
            String s = evento.getNombre()+" - "+listadoPlanesDeEstudio.get(i).getFecha()+" - "+evento.getHoraInicio()+" - "+evento.getHoraFin();
            boolean b = listadoPlanesDeEstudio.get(i).isEstado();
            iu_revisar_plan.Item item = new iu_revisar_plan.Item(s, b);
            items.add(item);
        }
    }

    /**
     * Clase que representa cada elemento del ListView personalizado.
     */
    public class Item{
        boolean checked;
        String ItemString;
        Item(String t, boolean b){
            ItemString = t;
            checked = b;
        }

        public boolean isChecked(){
            return checked;
        }
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView text;
    }

    /**
     * Clase que maneja el ListView con checkpoint incluidos.
     */
    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<iu_revisar_plan.Item> list;

        ItemsListAdapter(Context c, List<iu_revisar_plan.Item> l) {
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            iu_revisar_plan.ViewHolder viewHolder = new iu_revisar_plan.ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.row, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (iu_revisar_plan.ViewHolder) rowView.getTag();
            }

            viewHolder.checkBox.setChecked(list.get(position).checked);

            final String itemStr = list.get(position).ItemString;
            viewHolder.text.setText(itemStr);

            viewHolder.checkBox.setTag(position);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;
                    listadoPlanesDeEstudio.get(position).setEstado(list.get(position).isChecked());
                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_ayuda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el menu de la barra de tareas de la aplicación.
        int id = item.getItemId();

        if (id == R.id.menu_unico_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            intentAyuda.putExtra("ayuda", "revisar_plan");
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Este método realiza el registro del estado actual de la planificación escogida, guardando
     * cuales planes fueron cumplidos, y la cantidad de horas dedicadas hasta el momento.
     */
    private void registrarEstadoPlanificacion(){
        if (planificacion!=null){
            int aux = 0, horasSemanales=0;
            for (int i=0; i<listadoPlanesDeEstudio.size(); i++){
                if(listadoPlanesDeEstudio.get(i).isEstado()){
                    ModeloEvento evento = gestorEvento.obtenerDatosEventoPorId(listadoPlanesDeEstudio.get(i).getIdEvento(), listadoPlanesDeEstudio.get(i).getFecha());
                    aux+= Integer.valueOf(evento.getHoraFin().split(":")[0]) - Integer.valueOf(evento.getHoraInicio().split(":")[0]);
                }
            }
            try {
                DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaInicial = formato.parse(planificacion.getFechaInicio());
                String fechaExamen = gestorExamen.obtenerDatosExamenPorId(planificacion.getIdExamen()).getFecha();
                Date fechaFinal = formato.parse(fechaExamen);
                Calendar c = Calendar.getInstance();
                c.setTime(fechaInicial);
                double dias=((fechaFinal.getTime()-fechaInicial.getTime())/86400000); //milisegundos en un dia;
                if(dias<7){
                    dias=7;
                }
                double d = dias/7.0;
                horasSemanales = (int) (aux/Math.ceil(dias/7.0));

            }catch (ParseException e){
                horasSemanales=aux;
            }

            planificacion.setHoras(horasSemanales);
            if (examen.getResultado().isEmpty()==false){
                planificacion.setResultado(Float.parseFloat(examen.getResultado()));
            }
            gestorPlanificador.actualizarPlanificacion(planificacion);
            gestorPlanificador.actualizarPlanes(planificacion.getIdPlanificacion(),planificacion.getTipoMateria(),listadoPlanesDeEstudio);
            Toast.makeText(iu_revisar_plan.this, getResources().getString(R.string.completado), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iu_revisar_plan.this.finish();
                }
            }, 1000);

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
