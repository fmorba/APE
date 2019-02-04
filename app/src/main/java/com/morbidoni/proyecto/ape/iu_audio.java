package com.morbidoni.proyecto.ape;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import modelos.ModeloArchivo;
import servicios.GestorArchivos;

/**
 * Clase encargada de control de la interfaz, encargada de la captura de archivos de audio y su
 * posterior almacenamiento y registro.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_audio extends AppCompatActivity {
    static final int REQUEST_AUDIO_CAPTURE = 1;
    ImageView icono;
    boolean isRecording=false, isPlaying=false, isPaused=false;
    ImageButton btnPlay, btnPause, btnRecord;
    EditText editNombre, editTemas;
    TextView txtEstado, txtTime;
    Button btnAgregar;
    String nombreAudio = "mp3", temasAudio = "", direccion, tipo = "Audios", fechaCreacion, idArchivo;
    private static int oTime =0, sTime =0, eTime =0;
    private Handler hdlr = new Handler();
    private int musicLegth;
    GestorArchivos gestorArchivos;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_audio);

        icono = (ImageView) findViewById(R.id.iconoAudio);
        editNombre = (EditText) findViewById(R.id.editNombreAudio);
        editTemas = (EditText) findViewById(R.id.editAudioTemas);
        btnAgregar = (Button) findViewById(R.id.btnAgregarAudio);
        btnPlay = (ImageButton) findViewById(R.id.btnPlayRecord);
        btnRecord = (ImageButton) findViewById(R.id.btnRecord);
        btnPause = (ImageButton) findViewById(R.id.btnPauseRecord);
        txtEstado = (TextView) findViewById(R.id.txtEstadoAudio);
        txtTime = (TextView) findViewById(R.id.txtAudioTime);
        btnPlay.setEnabled(false);
        btnPause.setEnabled(false);
        gestorArchivos = new GestorArchivos();

        iniciarCapturaDeAudio();

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gestorArchivos.agregarArchivo(generarRegistroFoto());
                Toast.makeText(iu_audio.this, getResources().getString(R.string.mensaje_archivo_guardado), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iu_audio.this.finish();
                    }
                }, 1000);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer = new MediaPlayer();
                if (isPlaying){
                    btnRecord.setEnabled(true);
                    btnPause.setEnabled(false);
                    isPlaying=false;
                    btnPlay.setImageDrawable(getDrawable(R.drawable.ic_ver_audio_audioplay));
                    icono.setImageDrawable(getDrawable(R.drawable.ic_ver_audio_inactivo));
                    mPlayer.reset();
                    mPlayer.stop();
                    txtEstado.setText("");
                    txtTime.setText("");
                }else {
                    try {
                        isPlaying=true;
                        btnRecord.setEnabled(false);
                        btnPause.setEnabled(true);
                        mPlayer.setDataSource(direccion);
                        mPlayer.prepare();
                        mPlayer.start();
                        btnPlay.setImageDrawable(getDrawable(R.drawable.ic_ver_audio_audiooff));
                        icono.setImageDrawable(getDrawable(R.drawable.ic_ver_audio_audioplay));
                        eTime = mPlayer.getDuration();
                        sTime = mPlayer.getCurrentPosition();
                        if(oTime == 0){
                            oTime =1;
                        }
                        txtEstado.setText(getResources().getString(R.string.audio_play));
                        txtTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                        hdlr.postDelayed(UpdateSongTime, 100);
                    } catch (IOException e) {
                        Toast.makeText(iu_audio.this, getResources().getString(R.string.error_archivo_no_encontrado), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    btnPlay.setEnabled(true);
                    isRecording=false;
                    icono.setImageDrawable(getDrawable(R.drawable.ic_ver_audio_inactivo));
                    btnRecord.setImageDrawable(getDrawable(R.drawable.ic_archivo_audio));
                    txtEstado.setText(getResources().getString(R.string.audio_grabacion_detenida));

                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;

                }else{
                    isRecording=true;
                    icono.setImageDrawable(getDrawable(R.drawable.ic_archivo_audio));
                    btnRecord.setImageDrawable(getDrawable(R.drawable.ic_ver_audio_inactivo));

                    File aFile = null;
                    try {
                        if (direccion!=null){
                         File auxF = new File(direccion);
                         auxF.delete();
                        }
                        aFile = crearArchivoAudio();
                    } catch (IOException ex) {
                        Toast.makeText(iu_audio.this, getResources().getString(R.string.error_creacion_archivo), Toast.LENGTH_SHORT).show();
                    }
                    if (aFile != null) {

                        ActivityCompat.requestPermissions(iu_audio.this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

                        mRecorder = new MediaRecorder();
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mRecorder.setOutputFile(direccion);
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                        try {
                            mRecorder.prepare();
                            txtEstado.setText(getResources().getString(R.string.audio_grabacion));
                        } catch (IOException e) {
                            Toast.makeText(iu_audio.this, getResources().getString(R.string.error_creacion_archivo), Toast.LENGTH_SHORT).show();
                        }
                        mRecorder.start();

                    }
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    if (isPaused) {
                        isPaused=false;
                        btnPause.setImageDrawable(getDrawable(R.drawable.ic_ver_audio_audiopause));
                        icono.setImageDrawable(getDrawable(R.drawable.ic_ver_audio_audioplay));
                        mPlayer.seekTo(musicLegth);
                        mPlayer.start();
                        txtEstado.setText(getResources().getString(R.string.audio_play));
                        txtTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                        hdlr.postDelayed(UpdateSongTime, 100);
                    } else {
                        isPaused=true;
                        mPlayer.pause();
                        musicLegth=mPlayer.getCurrentPosition();
                        btnPause.setImageDrawable(getDrawable(R.drawable.ic_ver_audio_audioplay));
                        icono.setImageDrawable(getDrawable(R.drawable.ic_ver_audio_audiopause));
                        txtEstado.setText(getResources().getString(R.string.audio_pausa));
                        txtTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
                                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
                    }
                }
            }
        });
    }

    /**
     * Método que inicia el proceso de captura de audio, se llama al iniciar la actividad.
     */
    private void iniciarCapturaDeAudio() {
        File aFile = null;
        try {
            aFile = crearArchivoAudio();
        } catch (IOException ex) {
            Toast.makeText(this, getResources().getString(R.string.error_creacion_archivo), Toast.LENGTH_SHORT).show();
        }
        if (aFile != null) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_AUDIO_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Audio capturado.", Toast.LENGTH_SHORT).show();
        } else {
            btnAgregar.setEnabled(false);
            btnPlay.setEnabled(false);
            btnPause.setEnabled(false);
            btnRecord.setEnabled(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * Método que genera un archivo temporal en el dispositivo físico, donde se guardara la
     * información capturada.
     *
     * @return Archivo temporal con la informacion del almacenamiento fisico.
     * @throws IOException Problemas al generar el archivo.
     */
    private File crearArchivoAudio() throws IOException {
        String fechaID = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fechaCreacion = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (editNombre.getText().toString().trim().isEmpty() == false) {
            nombreAudio = editNombre.getText().toString();
        }
        idArchivo = nombreAudio + "_" + fechaID + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File image = File.createTempFile(
                idArchivo,  /* prefix */
                ".mp3",         /* suffix */
                storageDir      /* directory */
        );

        direccion = image.getAbsolutePath();
        return image;
    }

    /**
     * Método que valida los datos ingresados y genera un modelo del archivo con los datos del
     * registro.
     *
     * @return ModeloArchivo con los datos del registro.
     */
    private ModeloArchivo generarRegistroFoto() {
        if (editNombre.getText().toString().trim().isEmpty() == false) {
            nombreAudio = editNombre.getText().toString();
        }
        if (editTemas.getText().toString().trim().isEmpty() == false) {
            temasAudio = editTemas.getText().toString();
        }

        ModeloArchivo archivoNuevo = new ModeloArchivo(nombreAudio, tipo, fechaCreacion, direccion, temasAudio);
        archivoNuevo.setIdArchivo(idArchivo);
        return archivoNuevo;
    }

    /**
     * Método que actualiza el tiempo del audio en la interfaz gráfica.
     */
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            if (isPlaying && mPlayer!=null) {
                sTime = mPlayer.getCurrentPosition();
                txtTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                        TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))));
                hdlr.postDelayed(this, 100);
            }
        }
    };

}
