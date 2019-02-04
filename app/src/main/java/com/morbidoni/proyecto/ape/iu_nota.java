package com.morbidoni.proyecto.ape;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import modelos.ModeloArchivo;
import servicios.GestorArchivos;

/**
 * Clase que controla la interfaz dedicada a la toma de notas por parte del usuario y su posterior
 * almacenamiento.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_nota extends AppCompatActivity {
    EditText editNombre, editTemas, editContenido;
    Button btnAgregar;
    String nombreNota="txt", temasNota="", direccion, tipo="Notas", fechaCreacion, idArchivo;
    GestorArchivos gestorArchivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_nota);

        editContenido = (EditText) findViewById(R.id.editContenidoNota);
        editNombre = (EditText) findViewById(R.id.editNombreNota);
        editTemas = (EditText) findViewById(R.id.editNotaTemas);
        btnAgregar = (Button) findViewById(R.id.btnAgregarNota);
        gestorArchivos = new GestorArchivos();

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escribirNota();
                gestorArchivos.agregarArchivo(generarRegistro());
                Toast.makeText(iu_nota.this, getResources().getString(R.string.mensaje_archivo_guardado), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iu_nota.this.finish();
                    }
                }, 1000);
            }
        });

    }

    /**
     * Método que genera un archivo temporal en el dispositivo físico, donde se guardara la
     * información capturada.
     *
     * @return Archivo temporal con la informacion del almacenamiento fisico.
     * @throws IOException Problemas al generar el archivo.
     */
    private File crearArchivo() throws IOException {
        String fechaID = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fechaCreacion = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (editNombre.getText().toString().trim().isEmpty()==false){
            nombreNota=editNombre.getText().toString();
        }
        idArchivo = nombreNota+ "_" + fechaID + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File note = File.createTempFile(
                idArchivo,  /* prefix */
                ".txt",         /* suffix */
                storageDir      /* directory */
        );

        direccion = note.getAbsolutePath();
        return note;
    }

    /**
     * Método que valida los datos ingresados y genera un modelo del archivo con los datos del
     * registro.
     *
     * @return ModeloArchivo con los datos del registro.
     */
    private ModeloArchivo generarRegistro(){
        if (editNombre.getText().toString().trim().isEmpty()==false){
            nombreNota=editNombre.getText().toString();
        }
        if (editTemas.getText().toString().trim().isEmpty()==false){
            temasNota=editTemas.getText().toString();
        }

        ModeloArchivo archivoNuevo = new ModeloArchivo(nombreNota,tipo,fechaCreacion,direccion,temasNota);
        archivoNuevo.setIdArchivo(idArchivo);
        return archivoNuevo;
    }

    /**
     * Método cuya función es tomar la nota escrita por el usuario, y guardarla localmente en el
     * dispositivo, mientras se realiza un registro del archivo en Firebase. Ante la presencia de
     * problemas, genera mensajes de error, para advertir al usuario.     *
     */
    private void escribirNota(){
        try {
            File f = crearArchivo();
            if (f!=null){
                if (editContenido.getText().toString().trim().isEmpty()==false){
                    try {
                        FileWriter writer = new FileWriter(f);
                        writer.append(editContenido.getText().toString());
                        writer.flush();
                        writer.close();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(iu_nota.this, getResources().getString(R.string.error_creacion_archivo), Toast.LENGTH_SHORT).show();
                    }catch (IOException e) {
                        Toast.makeText(iu_nota.this, getResources().getString(R.string.error_creacion_archivo), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(iu_nota.this, getResources().getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show();
                }
            }else {
                throw new IOException();
            }
        }catch (IOException e) {
            Toast.makeText(iu_nota.this, getResources().getString(R.string.error_creacion_archivo), Toast.LENGTH_SHORT).show();
        }
    }

}
