package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import servicios.GestorUsuario;

public class iu_login extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "id";
    GestorUsuario gestor = new GestorUsuario();
    Button botonIngreso, botonRegistro;
    EditText editNombreUsuario, editContrasena;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_login);

        botonIngreso = (Button) findViewById(R.id.botonIngresarLogin);
        botonRegistro = (Button) findViewById(R.id.botonRegistroLogin);
        editNombreUsuario = (EditText) findViewById(R.id.editNombreUsuarioLogin);
        editContrasena = (EditText) findViewById(R.id.editContraseñaLogin);

        final Intent intentoInicio = new Intent(this, iu_inicio.class);
        final Intent intentoRegistro = new Intent(this, iu_registro.class);


        botonIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreUsuario = editNombreUsuario.getText().toString();
                String claveUsuario = editContrasena.getText().toString();

                boolean identificacionCorrecta = gestor.ComprobarContraseña(nombreUsuario,claveUsuario);

                if (identificacionCorrecta){
                   idUsuario = gestor.ObtenerIDUsuario(nombreUsuario);
                   intentoInicio.putExtra(EXTRA_MESSAGE,  idUsuario);
                   startActivity(intentoInicio);
                }else {
                    Toast.makeText(iu_login.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(intentoRegistro);
            }
        });


    }
}
