package com.morbidoni.proyecto.ape;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import modelos.ModeloExamen;
import modelos.ModeloMateria;
import servicios.GestorExamen;
import servicios.GestorMateria;

/**
 * Pequeña clase dedicada a la interfaz que permite al usuario obtener promedio dependiendo de las
 * materias ingresadas para el cálculo del mismo.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_promedio_especifico extends AppCompatActivity {
    GestorMateria gestorMateria;
    GestorExamen gestorExamen;
    ListView listaMaterias;
    Button btnCalcularPromedio;
    List<iu_promedio_especifico.Item> items;
    iu_promedio_especifico.ItemsListAdapter myItemsListAdapter;
    ArrayList<String> listado = new ArrayList<>();
    ArrayList<ModeloMateria> arrayMaterias = new ArrayList<>();
    ArrayList<ModeloMateria> materiasSeleccionadas= new ArrayList<>();
    float promedio=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_promedio_especifico);

        Intent intent = getIntent();

        listaMaterias = (ListView) findViewById(R.id.seleccion_materias);
        btnCalcularPromedio = (Button) findViewById(R.id.boton_calcular_promedio_especifico);
        gestorMateria = new GestorMateria();
        gestorExamen = new GestorExamen();

        cargarListadoMaterias();

        btnCalcularPromedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promedio=calcularPromedio();
                final AlertDialog.Builder builder = new AlertDialog.Builder(iu_promedio_especifico.this);
                builder.setTitle(getResources().getString(R.string.promedio_especifico));
                builder.setMessage(getResources().getString(R.string.calculo_promedio_especifico,promedio))
                        .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.show();
                builder.setCancelable(true);
            }
        });

    }

    /**
     * Método que obtiene la lista de materias registradas y genera un array de strings para su
     * presentación en la interfaz.
     */
    private void cargarListadoMaterias(){
        if (listado!=null) {
            listado.clear();
            arrayMaterias = gestorMateria.obtenerListadoMaterias();
            initItems();
            myItemsListAdapter = new iu_promedio_especifico.ItemsListAdapter(this, items);
            listaMaterias.setAdapter(myItemsListAdapter);
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
            listaMaterias.setAdapter(itemsAdapter);
        }
    }

    /**
     * Método simple que realiza el promedio de los exámenes de las materias ingresadas.
     *
     * @return Promedio obtenido.
     */
    private float calcularPromedio(){
        int contador =0;
        float sumaNotas=0, resultado=0;

        if (materiasSeleccionadas.isEmpty()==false) {

            for (ModeloMateria materia: materiasSeleccionadas) {
                ArrayList<ModeloExamen> examenesMaterias = new ArrayList<>();
                examenesMaterias = gestorExamen.obtenerListadoExamenesPorMateria(materia.getNombre());
                if(examenesMaterias!=null) {
                    for (ModeloExamen examen : examenesMaterias) {
                        if (examen.getResultado().equals("") == false) {
                            sumaNotas += Float.valueOf(examen.getResultado());
                            contador++;
                        }
                    }
                }
            }
            if (contador!=0) {
                resultado = sumaNotas / contador;
            }
        }
        return resultado;
    }

    /**
     * Método que carga las lista en el ListView personalizado para incluir checkpoints.
     */
    private void initItems(){
        items = new ArrayList<iu_promedio_especifico.Item>();
        if (arrayMaterias!=null) {
            for (int i = 0; i < arrayMaterias.size(); i++) {
                ModeloMateria materia = gestorMateria.obtenerDatosMateria(arrayMaterias.get(i).getIdMateria());
                String s = materia.getNombre() + " - " + materia.getTipo() + " - " + materia.getEstado();
                boolean b = false;
                iu_promedio_especifico.Item item = new iu_promedio_especifico.Item(s, b);
                items.add(item);
            }
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
        private List<iu_promedio_especifico.Item> list;

        ItemsListAdapter(Context c, List<iu_promedio_especifico.Item> l) {
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

            iu_promedio_especifico.ViewHolder viewHolder = new iu_promedio_especifico.ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.row, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (iu_promedio_especifico.ViewHolder) rowView.getTag();
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
                    if (newState){
                        materiasSeleccionadas.add(arrayMaterias.get(position));
                    }else{
                        materiasSeleccionadas.remove(arrayMaterias.get(position));
                    }
                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }
    }
}
