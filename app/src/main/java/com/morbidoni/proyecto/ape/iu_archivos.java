package com.morbidoni.proyecto.ape;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import modelos.ModeloArchivo;
import servicios.GestorArchivos;

/**
 * Es la clase que controla la interfaz de entrada al manejo de archivos, llama a los métodos
 * necesarios de los gestores de datos, si así lo requiriera.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_archivos extends AppCompatActivity {
    ImageButton btnAgregarArchivo, btnObservarArchivo, btnEliminarArchivo, btnModificarArchivo, btnAbrirArchivo;
    Spinner spTiposArchivos;
    EditText editPalabrasClaves;
    ListView listadoArchivos;
    GestorArchivos gestorArchivos;
    ArrayList<ModeloArchivo> arrayModelos = new ArrayList<>();
    ArrayList<String> listado = new ArrayList<>();
    ModeloArchivo itemSeleccionado;
    int indicador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_archivos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarArchivos);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();

        btnAgregarArchivo = (ImageButton) findViewById(R.id.boton_agregar_archivo);
        btnObservarArchivo = (ImageButton) findViewById(R.id.boton_ver_archivo);
        btnModificarArchivo = (ImageButton) findViewById(R.id.boton_modificar_archivo);
        btnEliminarArchivo = (ImageButton) findViewById(R.id.boton_eliminar_archivo);
        btnAbrirArchivo = (ImageButton) findViewById(R.id.boton_abrir_archivo);
        spTiposArchivos = (Spinner) findViewById(R.id.spOpcionesTiposArchivos);
        editPalabrasClaves = (EditText) findViewById(R.id.editBuscarPalabrasCalvesArchivos);
        listadoArchivos = (ListView) findViewById(R.id.listado_menu_archivos);
        gestorArchivos = new GestorArchivos();

        final Intent intentAgregar = new Intent(this,iu_agregar_archivo.class);

        cargarListadoArchivos();

        btnAgregarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAgregar);
            }
        });

        btnAbrirArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirArchivo(itemSeleccionado);
            }
        });

        btnObservarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verDatosArchivo();
            }
        });

        btnModificarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarArchivos();
            }
        });

        btnEliminarArchivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarArchivo();
            }
        });

        spTiposArchivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               String seleccion = getResources().getStringArray(R.array.opciones_tipos_archivos)[i];

               seleccionarPorTipo(seleccion);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listadoArchivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSeleccionado=arrayModelos.get(i);
                indicador=i;
            }
        });

        editPalabrasClaves.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                seleccionarPorClave(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_archivos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el menU de la barra de tareas de la aplicación.
        int id = item.getItemId();

        if (id == R.id.menu_barra_agregar) {
            final Intent intentAgregar = new Intent(this,iu_agregar_archivo.class);
            startActivity(intentAgregar);
            return true;
        }
        if (id == R.id.menu_barra_abrir) {
            if (itemSeleccionado!=null) {
                abrirArchivo(itemSeleccionado);
            }
            return true;
        }
        if (id == R.id.menu_barra_observar) {
            verDatosArchivo();
            return true;
        }
        if (id == R.id.menu_barra_modificar) {
            modificarArchivos();
            return true;
        }
        if (id == R.id.menu_barra_eliminar) {
            eliminarArchivo();
            return true;
        }
        if (id == R.id.menu_barra_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            intentAyuda.putExtra("ayuda", "archivos");
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarListadoArchivos();
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
            listadoArchivos.setAdapter(itemsAdapter);
        }else{
            listadoArchivos.setAdapter(null);
        }
    }

    /**
     * Método que obtiene la lista de archivos registrados y genera un array de strings para su
     * presentación en la interfaz.
     */
    private void cargarListadoArchivos(){
        listado.clear();
        arrayModelos = gestorArchivos.obtenerListadoArchivos();
        if (arrayModelos!=null) {
            for (int i = 0; i < arrayModelos.size(); i++) {
                listado.add(arrayModelos.get(i).getNombre() + " - " + arrayModelos.get(i).getTipo()+" - "+arrayModelos.get(i).getFechaCreacion());
            }
            controlListView(listado);
        }
    }

    /**
     * Método que se encarga de la eliminación de archivos, luego de que el usuario seleccione un
     * archivo de la lista, y decida eliminarlo, este método genera un AlertDialog para pedir
     * confirmación, de recibirla llama al Gestor de Archivos y su correspondiente método para
     * iniciar el eliminado del registro del mismo, y a su vez elimina el archivo del dispositivo
     * físico.
     */
    private void eliminarArchivo(){
        if(itemSeleccionado!=null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_archivos.this);
            builder.setMessage(R.string.mensaje_eliminar)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            gestorArchivos.eliminarArchivo(itemSeleccionado.getIdArchivo(),itemSeleccionado.getTipo());
                            arrayModelos.remove(indicador);
                            listado.remove(indicador);
                            controlListView(listado);
                            File f = new File(itemSeleccionado.getDireccion());
                            f.delete();
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.setCancelable(true);
            builder.show();
        }else{
            Toast.makeText(iu_archivos.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Se genera un AlertDialog especializado para la captura de los datos modificados del registro
     * del archivo ingresados por el usuario. Este método modifica los datos del registro pero no
     * cambia el archivo en sí.
     */
    private void modificarArchivos(){
        if(itemSeleccionado!=null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_archivos.this);

            Context context = iu_archivos.this;
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            final TextView txtNombre= new TextView(context);
            txtNombre.setText(getResources().getString(R.string.nombre_archivo));
            layout.addView(txtNombre);
            final EditText nombreBox = new EditText(context);
            nombreBox.setText(itemSeleccionado.getNombre());
            layout.addView(nombreBox);
            final TextView txtTemas= new TextView(context);
            txtTemas.setText(getResources().getString(R.string.temas_archivo));
            layout.addView(txtTemas);
            final EditText temaBox = new EditText(context);
            temaBox.setInputType(InputType.TYPE_CLASS_TEXT);
            temaBox.setLines(5);
            temaBox.setMaxLines(5);
            temaBox.setGravity(Gravity.LEFT | Gravity.TOP);
            temaBox.setText(itemSeleccionado.getClave());
            layout.addView(temaBox);
            builder.setView(layout);

            builder.setMessage(R.string.mensaje_modificar_archivo)
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ModeloArchivo archivoNuevo = itemSeleccionado;
                            archivoNuevo.setNombre(nombreBox.getText().toString());
                            archivoNuevo.setClave(temaBox.getText().toString());
                            gestorArchivos.modificarArchivo(archivoNuevo);
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.setCancelable(true);
            builder.show();
        }else{
            Toast.makeText(iu_archivos.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Se llama un AlertDialog para mostrar los datos del registro del elemento seleccionado.
     */
    private void verDatosArchivo(){
        if(itemSeleccionado!=null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_archivos.this);
            builder.setMessage(getString(R.string.informe_datos_archivo,itemSeleccionado.getNombre(),itemSeleccionado.getTipo(),itemSeleccionado.getFechaCreacion(),itemSeleccionado.getClave(),itemSeleccionado.getDireccion()))
                    .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.setCancelable(true);
            builder.show();
        }else{
            Toast.makeText(iu_archivos.this, R.string.error_objeto_no_seleccionado, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Este método actualiza la lista de archivos disponible en la interfaz, solo mostrando los
     * archivos pertenecientes al tipo seleccionado.
     *
     * @param tipo String que determina el tipo de archivo elegido por el usuario.
     */
    private void seleccionarPorTipo(String tipo){
        cargarListadoArchivos();
        if (tipo.equals("Todos")==false) {
            ArrayList<ModeloArchivo> arrayAuxiliar = new ArrayList<>();
            for (ModeloArchivo archivo : arrayModelos) {
                if (archivo.getTipo().equals(tipo)) {
                    arrayAuxiliar.add(archivo);
                }
            }
            arrayModelos = arrayAuxiliar;
            listado.clear();
            if (arrayModelos != null) {
                for (int i = 0; i < arrayModelos.size(); i++) {
                    listado.add(arrayModelos.get(i).getNombre() + " - " + arrayModelos.get(i).getTipo() + " - " + arrayModelos.get(i).getFechaCreacion());
                }
            }
            controlListView(listado);
        }
    }

    /**
     *Este método actualiza la lista de archivos disponible en la interfaz, solo mostrando los
     * archivos que tengan las palabras claves o similares, a las ingresadas por el usuario. Este
     * método es compatible con la selección por tipo, permitiendo búsquedas específicas.
     *
     * @param clave String que contiene las palabras claves a buscar por deseo del usuario.
     */
    private void seleccionarPorClave(String clave){
        if (clave.isEmpty()){
            seleccionarPorTipo(spTiposArchivos.getSelectedItem().toString());
        }
        else {
            ArrayList<ModeloArchivo> arrayAuxiliar = new ArrayList<>();
            for (ModeloArchivo archivo : arrayModelos) {
                if (archivo.getClave().toLowerCase().contains(clave.toLowerCase())) {
                    arrayAuxiliar.add(archivo);
                }
            }
            arrayModelos = arrayAuxiliar;
            listado.clear();
            if (arrayModelos != null) {
                for (int i = 0; i < arrayModelos.size(); i++) {
                    listado.add(arrayModelos.get(i).getNombre() + " - " + arrayModelos.get(i).getTipo() + " - " + arrayModelos.get(i).getFechaCreacion());
                }
            }
            controlListView(listado);
        }
    }

    /**
     * Método que abre un archivo utilizando las herramientas propias del dispositivo físico, para
     * mostrar al usuario el contenido de dicho archivo.
     *
     * @param archivoSeleccionado Registro del archivo, usado para determinar su tipo y dirección
     *                            de almacenamiento.
     */
    private void abrirArchivo(ModeloArchivo archivoSeleccionado){
        File f = new File(itemSeleccionado.getDireccion());
        Uri contentUri;
        switch (archivoSeleccionado.getTipo()){
            case "Fotos":
                Intent intentFoto = new Intent();
                intentFoto.setAction(Intent.ACTION_VIEW);
                contentUri = FileProvider.getUriForFile(this,"com.morbidoni.proyecto.fileprovider",f);
                intentFoto.setDataAndType(contentUri,"image/*");
                intentFoto.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intentFoto);
                break;

            case "Videos":
                Intent intentVideo = new Intent();
                intentVideo.setAction(Intent.ACTION_VIEW);
                contentUri = FileProvider.getUriForFile(this,"com.morbidoni.proyecto.fileprovider",f);
                intentVideo.setDataAndType(contentUri,"video/mp4");
                intentVideo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intentVideo);
                break;

            case "Audios":
                Intent intentAudio = new Intent();
                intentAudio.setAction(Intent.ACTION_VIEW);
                contentUri = FileProvider.getUriForFile(this,"com.morbidoni.proyecto.fileprovider",f);
                intentAudio.setDataAndType(contentUri,"audio/*");
                intentAudio.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intentAudio);
                break;

            case "Notas":
               String texto = null;

                try {
                    FileInputStream fileInputStream = new FileInputStream (f);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (texto = bufferedReader.readLine()) != null )
                    {
                        stringBuilder.append(texto + System.getProperty("line.separator"));
                    }
                    fileInputStream.close();
                    texto = stringBuilder.toString();

                    bufferedReader.close();
                }
                catch(FileNotFoundException ex) {
                    Toast.makeText(this, getResources().getString(R.string.error_archivo_no_encontrado), Toast.LENGTH_SHORT).show();
                }
                catch(IOException ex) {
                    Toast.makeText(this, getResources().getString(R.string.error_archivo_no_encontrado), Toast.LENGTH_SHORT).show();
                }

                if (texto.isEmpty()==false ){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(iu_archivos.this);
                    builder.setMessage(texto)
                            .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    builder.setCancelable(true);
                    builder.show();
                }else{
                    Toast.makeText(this, getResources().getString(R.string.error_archivo_vacio), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }


    }

}
