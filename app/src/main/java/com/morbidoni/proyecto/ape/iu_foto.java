package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import modelos.ModeloArchivo;
import servicios.GestorArchivos;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Esta clase en particular se encarga de la captura de imágenes mediante la cámara propia del
 * dispositivo. Luego se encarga del registro y almacenamiento local de dicha foto.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_foto extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView fotoTomada;
    EditText editNombre, editTemas;
    Button btnAgregar;
    String nombreFoto="jpeg", temasFoto="", direccion, tipo="Fotos", fechaFoto, idArchivo;
    GestorArchivos gestorArchivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_foto);

        Intent intent = getIntent();

        fotoTomada = (ImageView) findViewById(R.id.ivFotoSacada);
        editNombre = (EditText) findViewById(R.id.editNombreFoto);
        editTemas = (EditText) findViewById(R.id.editFotoTemas);
        btnAgregar = (Button) findViewById(R.id.btnAgregarFoto);
        gestorArchivos = new GestorArchivos();

        iniciarCapturaDeFoto();

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarALaGaleria();
                gestorArchivos.agregarArchivo(generarRegistroFoto());
                Toast.makeText(iu_foto.this, getResources().getString(R.string.mensaje_archivo_guardado), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iu_foto.this.finish();
                    }
                }, 1000);
            }
        });

        ViewTreeObserver vto = fotoTomada.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                fotoTomada.getViewTreeObserver().removeOnPreDrawListener(this);
                setPic();
                return true;
            }
        });

    }

    /**
     * Método llamado al inicio de la aplicación, inicia la captura de una imagen llamado a los
     * controladores de la cámara.
     */
    private void iniciarCapturaDeFoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = crearArchivoImagen();
            } catch (IOException ex) {
                Toast.makeText(this, getResources().getString(R.string.error_creacion_archivo), Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.morbidoni.proyecto.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Imagen capturada.", Toast.LENGTH_SHORT).show();
        }else {
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
    private File crearArchivoImagen() throws IOException {
        String fechaID = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fechaFoto = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (editNombre.getText().toString().trim().isEmpty()==false){
            nombreFoto=editNombre.getText().toString();
        }
        idArchivo = nombreFoto+ "_" + fechaID + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                idArchivo,  /* prefix */
                ".jpg",         /* suffix */
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
            nombreFoto=editNombre.getText().toString();
        }
        if (editTemas.getText().toString().trim().isEmpty()==false){
            temasFoto=editTemas.getText().toString();
        }

        ModeloArchivo archivoNuevo = new ModeloArchivo(nombreFoto,tipo,fechaFoto,direccion,temasFoto);
        archivoNuevo.setIdArchivo(idArchivo);
        return archivoNuevo;
    }

    /**
     * Método que se encarga de colocar la imagen capturada por la cámara en la interfaz del
     * usuario, para que el mismo pueda observarla una última vez antes de decidir si agregarla
     * o no.
     */
    private void setPic() {
        int targetW = fotoTomada.getMeasuredWidth();
        int targetH = fotoTomada.getMeasuredHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(direccion, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(direccion, bmOptions);
        fotoTomada.setImageBitmap(bitmap);
    }

}
