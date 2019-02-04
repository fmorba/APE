package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Esta clase controla la interfaz para agregar archivos, y envía al usuario a la interfaz
 * correspondiente dependiendo del tipo de archivo que desea agregar.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_agregar_archivo extends AppCompatActivity {
    ImageButton btnNota,btnAudio,btnVideo,btnFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_agregar_archivo);

        final Intent intent = getIntent();

        btnFoto = (ImageButton) findViewById(R.id.boton_agregar_foto);
        btnNota = (ImageButton) findViewById(R.id.boton_agregar_nota);
        btnAudio = (ImageButton) findViewById(R.id.boton_agregar_audio);
        btnVideo = (ImageButton) findViewById(R.id.boton_agregar_video);

        final Intent intentoFoto = new Intent(this,iu_foto.class);
        final Intent intentAudio = new Intent(this,iu_audio.class);
        final Intent intentNota = new Intent(this,iu_nota.class);
        final Intent intentVideo = new Intent(this,iu_video.class);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intentoFoto);
            }
        });

        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentVideo);
            }
        });

        btnNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentNota);
            }
        });

        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAudio);
            }
        });
    }
}
