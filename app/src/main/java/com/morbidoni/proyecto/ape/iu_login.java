package com.morbidoni.proyecto.ape;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    Button botonIngreso;
    String idUsuario;
    private FirebaseAuth mAuth;
    GestorUsuario gestorUsuario = new GestorUsuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_login);

        botonIngreso = (Button) findViewById(R.id.boton_ingresar_login);

        mensajeConexion();
        mAuth = FirebaseAuth.getInstance();

        botonIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("917374848644-kj2pm51eeio94tlj522vt9hdgdu884t2.apps.googleusercontent.com")
                .requestEmail()
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar.events"))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    /**
     * Este método que inicia el proceso de ingreso a la aplicaciòn-
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(iu_login.this, getResources().getString(R.string.error_datos_invalido), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Este método verificar si la aplicacion posee conexion al internet.
     *
     * @param acct variable uqe vincula con la cuenta de Google.
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            gestorUsuario.registarUsuario(user.getUid(),user.getEmail());
                            updateUI(user);
                        } else {
                            Toast.makeText(iu_login.this, getResources().getString(R.string.error_datos_invalido), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Este método, una vez identificado el usuario, permite ingresar a la ventana inicial de la
     * aplicaciòn.
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            idUsuario=user.getUid();
            Intent intentoInicio = new Intent(this, iu_inicio.class);
            intentoInicio.putExtra(EXTRA_MESSAGE,idUsuario);
            startActivity(intentoInicio);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /**
     * Este método verificar si la aplicacion posee conexion al internet.
     *
     * @return existeConecion - true: conexión valida, false: conexión invalida.
     */
    private boolean verificarConexion(){
        boolean exiteConecion;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            exiteConecion=true;
        } else {
            exiteConecion=false;
        }

        return exiteConecion;
    }

    /**
     * Este método muestra una interfaz en el caso de no contar con una conexión, de tal forma de
     * avisar al usuario, y poder volver a intentarlo o cerrar la aplicación.     *
     */
    private void mensajeConexion(){
        if (verificarConexion()==false) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(iu_login.this);
            builder.setMessage(R.string.mensaje_conexion_fallida)
                    .setPositiveButton(R.string.reintentar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mensajeConexion();
                        }
                    })
                    .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            System.exit(0);
                        }
                    });
            builder.show();
        }
    }
}
