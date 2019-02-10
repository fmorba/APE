package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

/**
 * Esta clase maneja la presentación mostrada la iniciar la aplicación.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class SplashScreen extends AppCompatActivity {
    VideoView icono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        icono = (VideoView) findViewById(R.id.videoIcono);

        String pathIcono = "android.resource://" + getPackageName() + "/" + R.raw.video_inicio;

        icono.setVideoURI(Uri.parse(pathIcono));

        icono.start();

        icono.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Intent intent = new Intent(SplashScreen.this,iu_login.class);
                startActivity(intent);
            }
        });

    }
}
