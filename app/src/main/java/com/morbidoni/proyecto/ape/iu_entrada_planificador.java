package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class iu_entrada_planificador extends AppCompatActivity {
    Button btnIniciar, btnRevisar, btnPlanesViejos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_entrada_planificador);

        Intent intent = getIntent();

        btnIniciar = (Button) findViewById(R.id.btnIniciarPlanificacion);
        btnRevisar = (Button) findViewById(R.id.btnRevisarPlanificacion);
        btnPlanesViejos = (Button) findViewById(R.id.btnRevisarPlanesAntiguos);

        final Intent intentInicio = new Intent(this, iu_planificador.class);
        final Intent intentRevision = new Intent(this, iu_revisar_plan.class);
        final Intent intentAntiguos = new Intent(this,iu_revisar_planes_antiguos.class);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentInicio);
            }
        });

        btnRevisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentRevision);
            }
        });

        btnPlanesViejos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentAntiguos);
            }
        });

    }
}
