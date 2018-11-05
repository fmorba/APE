package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import servicios.GestorUsuario;

public class iu_registro extends AppCompatActivity {
    GestorUsuario gestor = new GestorUsuario();
    Button btnRegistro;
    EditText editNombre, editClave, editClaveRepeticion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_registro);

        final Intent intent= getIntent();

        btnRegistro = (Button) findViewById(R.id.botonRegistro);
        editNombre = (EditText) findViewById(R.id.editNombreUsuarioRegistro);
        editClave = (EditText) findViewById(R.id.editContraseñaRegistro);
        editClaveRepeticion = (EditText) findViewById(R.id.editContraseñaRepeticion);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre, clave, claveRepeticion;
                nombre = editNombre.getText().toString();
                clave = editClave.getText().toString();
                claveRepeticion = editClaveRepeticion.getText().toString();

                if (nombre.equals("") || clave.equals("") || claveRepeticion.equals("") || nombre.replace(" ","").equals("")){
                    Toast.makeText(iu_registro.this, R.string.campos_vacios, Toast.LENGTH_SHORT).show();
                }else{

                    int libre = gestor.ObtenerIDUsuario(nombre);
                    if (libre!=0){
                        Toast.makeText(iu_registro.this, R.string.nombre_utilizado, Toast.LENGTH_SHORT).show();
                    }else {

                        if (clave.equals(claveRepeticion)) {

                            String resultado;
                            resultado=gestor.RegistarUsuario(nombre,clave);

                            Toast.makeText(iu_registro.this, resultado, Toast.LENGTH_SHORT).show();

                            try {
                                Thread.sleep(1000);
                                finish();
                            } catch (InterruptedException e) {
                            }

                        } else {
                            Toast.makeText(iu_registro.this, R.string.contraseña_incorrecta, Toast.LENGTH_SHORT).show();
                        }

                    }
                }

            }
        });

    }
}
