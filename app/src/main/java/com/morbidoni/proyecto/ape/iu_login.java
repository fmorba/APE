package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import servicios.GestorUsuario;

/**
 * Clase correspondiente a la ventana de registro de nuevos usuario, solo se llama la primera vez o
 * si la información cache del usuario se borró, permite el ingreso e identificación del usuario,
 * así como el registro de un nuevo usuario.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_login extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "id";
    GestorUsuario gestor = new GestorUsuario();
    Button botonIngreso, botonRegistro;
    EditText editEmailUsuario, editContrasena;
    int idUsuario;
    FirebaseAuth.AuthStateListener firebaseListener;
    FirebaseUser usuarioSesion;
    GestorUsuario gestorUsuario = new GestorUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_login);

        botonIngreso = (Button) findViewById(R.id.botonIngresarLogin);
        botonRegistro = (Button) findViewById(R.id.botonRegistroLogin);
        editEmailUsuario = (EditText) findViewById(R.id.editNombreUsuarioLogin);
        editContrasena = (EditText) findViewById(R.id.editContraseñaLogin);

        final Intent intentoInicio = new Intent(this, iu_inicio.class);

        firebaseListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               usuarioSesion = firebaseAuth.getCurrentUser();
               if (usuarioSesion!=null){
                   final Intent intentoInicio = new Intent(iu_login.this, iu_inicio.class);
                   intentoInicio.putExtra(iu_login.EXTRA_MESSAGE, usuarioSesion.getUid());
                   startActivity(intentoInicio);
               }
            }
        };


        botonIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailUsuario = editEmailUsuario.getText().toString();
                String claveUsuario = editContrasena.getText().toString();
                iniciarSesion(emailUsuario,claveUsuario);

            }
        });

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailUsuario = editEmailUsuario.getText().toString();
                String claveUsuario = editContrasena.getText().toString();
                if (claveUsuario.length()<6){
                    Toast.makeText(iu_login.this, getResources().getString(R.string.error_contrasena_corta), Toast.LENGTH_SHORT).show();
                }else {
                    registrarUsuario(emailUsuario, claveUsuario);
                }
            }
        });

    }

    /**
     * Método que gestiona el registro de un nuevo usuario en  el sistema, mediante la interfaz de
     * Firebase.
     *
     * @param email String que contiene el email del usuario, se usa como identificador.
     * @param password String que contiene la contraseña elegida por el usuario.
     */
    private void registrarUsuario(final String email, final String password){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    gestorUsuario.registarUsuario(id,email,password);
                    Toast.makeText(iu_login.this, getResources().getString(R.string.mensaje_registro_exitoso), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(iu_login.this, getResources().getString(R.string.error_datos_invalido), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Método que permite el ingreso a la aplicación, pendiente de la identificación del mismo en
     * el sistema.
     *
     * @param email String que contiene el email del usuario, se usa como identificador.
     * @param password String que contiene la contraseña elegida por el usuario.
     */
    private void iniciarSesion(String email, String password){
        final Intent intentoInicio = new Intent(this, iu_inicio.class);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                }else {
                    Toast.makeText(iu_login.this, getResources().getString(R.string.error_datos_invalido), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(firebaseListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(firebaseListener);
        }
    }
}
