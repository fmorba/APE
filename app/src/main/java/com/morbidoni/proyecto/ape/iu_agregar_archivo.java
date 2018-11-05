package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class iu_agregar_archivo extends AppCompatActivity {
    ImageButton btnNota,btnAudio,btnVideo,btnFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_agregar_archivo);

        Intent intent = getIntent();
    }
}
