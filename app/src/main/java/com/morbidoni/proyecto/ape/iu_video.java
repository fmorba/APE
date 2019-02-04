package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import modelos.ModeloArchivo;
import servicios.GestorArchivos;

/**
 * Esta clase controla la interfaz del usuario, que es llamada para la captura de un archivo de
 * video, mediante la cámara del dispositivo, también maneja el almacenamiento y registro del
 * video capturado.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_video extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;
    VideoView videoTomado;
    EditText editNombre, editTemas;
    Button btnAgregar;
    String nombreVideo="mp4", temasVideo="", direccion, tipo="Videos", fechaCreacion, idArchivo;
    GestorArchivos gestorArchivos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_video);

        videoTomado = (VideoView) findViewById(R.id.vvVideoTomado);
        btnAgregar = (Button) findViewById(R.id.btnAgregarVideo);
        editNombre = (EditText) findViewById(R.id.editNombreVideo);
        editTemas = (EditText) findViewById(R.id.editVideoTemas);
        gestorArchivos= new GestorArchivos();

        iniciarCapturaDeVideo();

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarALaGaleria();
                gestorArchivos.agregarArchivo(generarRegistroFoto());
                Toast.makeText(iu_video.this, getResources().getString(R.string.mensaje_archivo_guardado), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iu_video.this.finish();
                    }
                }, 1000);
            }
        });

        ViewTreeObserver vto = videoTomado.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                videoTomado.getViewTreeObserver().removeOnPreDrawListener(this);
                setVideo();
                return true;
            }
        });


    }

    /**
     * Método que se llama al inicio de la actividad, para utilizar la cámara del dispositivo con
     * el fin de capturar un archivo de video.
     */
    private void iniciarCapturaDeVideo(){
        Intent takeIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeIntent.resolveActivity(getPackageManager()) != null) {
            File vFile = null;
            try {
                vFile = crearArchivoVideo();
            } catch (IOException ex) {
                Toast.makeText(this, getResources().getString(R.string.error_creacion_archivo), Toast.LENGTH_SHORT).show();
            }
            if (vFile != null) {
                Uri videoURI = FileProvider.getUriForFile(this,
                        "com.morbidoni.proyecto.fileprovider",
                        vFile);
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                startActivityForResult(takeIntent, REQUEST_VIDEO_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Video capturado.", Toast.LENGTH_SHORT).show();
        }else{
            btnAgregar.setEnabled(false);
        }
    }

    /**
     * Método que crea un archivo temporal en el dispositivo físico, donde luego se almacenara la
     * imagen capturada por la cámara.     *
     *
     * @return Archivo temporal donde si indicara la dirección de almacenamiento.
     * @throws IOException Problemas al generar el archivo.
     */
    private File crearArchivoVideo() throws IOException {
        String fechaID = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fechaCreacion = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (editNombre.getText().toString().trim().isEmpty()==false){
            nombreVideo=editNombre.getText().toString();
        }
        idArchivo = nombreVideo+ "_" + fechaID + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File image = File.createTempFile(
                idArchivo,  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );

        direccion = image.getAbsolutePath();
        return image;
    }

    /**
     *  Método que agrega la imagen capturada a la galería del dispositivo.
     */
    private void agregarALaGaleria() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(direccion);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    /**
     * Método que genera un Modelo de Archivo con los datos del registro del mismo.
     *
     * @return Modelo de Archivo con datos de registo.
     */
    private ModeloArchivo generarRegistroFoto(){
        if (editNombre.getText().toString().trim().isEmpty()==false){
            nombreVideo=editNombre.getText().toString();
        }
        if (editTemas.getText().toString().trim().isEmpty()==false){
            temasVideo=editTemas.getText().toString();
        }

        ModeloArchivo archivoNuevo = new ModeloArchivo(nombreVideo,tipo,fechaCreacion,direccion,temasVideo);
        archivoNuevo.setIdArchivo(idArchivo);
        return archivoNuevo;
    }

    /**
     * Método que luego de haber capturado el video, lo presenta en la interfaz para que el usuario
     * pueda verlo otra vez antes de finalizar el registro del archivo.
     */
    private void setVideo(){
        File video = new File(direccion);
        Uri contentUri = FileProvider.getUriForFile(this,"com.morbidoni.proyecto.fileprovider",video);
        videoTomado.setVideoURI(contentUri);
    }

}
