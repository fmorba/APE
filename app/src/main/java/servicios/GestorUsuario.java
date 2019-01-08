package servicios;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelos.ModeloUsuario;

public class GestorUsuario {
    ConexionBDOnline conexion = new ConexionBDOnline();
    ModeloUsuario modelo;
    ArrayList<String> array = new ArrayList<String>();
    JSONObject resultadoObtenido = new JSONObject();
    String respuesta;

    public String cambiarContraseña(final String usuario, final String clave){
        respuesta = "No se pudo cambiar la contraseña.";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(clave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(usuario).child(FirebaseReferencias.REFERENCIA_DATO);
                    agendaReferencia.child("contraseña").setValue(clave);
                    respuesta = "Cambio realizado";
                }
            }
        });
        return respuesta;
    }


    public ModeloUsuario obtenerDatosUsuario(String ID){
        try {
            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + ID + "/datos.json");
            String email = resultadoObtenido.getString("email");
            String contraseña = resultadoObtenido.getString("contraseña");
            String nombre= resultadoObtenido.getString("nombre");
            String provincia= resultadoObtenido.getString("provincia");
            String localidad= resultadoObtenido.getString("localidad");
            String fechaNacimiento= resultadoObtenido.getString("fechaNacimiento");
            String carrera= resultadoObtenido.getString("carrera");
            modelo = new ModeloUsuario(email,contraseña, nombre, provincia, localidad, fechaNacimiento, carrera);
        }catch (JSONException e){
            modelo = null;
        }
        return modelo;
    }

    public void actualizarDatosUsuario(String nombre, String fechaNacimiento, String provincia, String localidad, String carrera){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_DATO);
        agendaReferencia.child("nombre").setValue(nombre);
        agendaReferencia.child("fechaNacimiento").setValue(fechaNacimiento);
        agendaReferencia.child("provincia").setValue(provincia);
        agendaReferencia.child("localidad").setValue(localidad);
        agendaReferencia.child("carrera").setValue(carrera);
    }

    public void registarUsuario(String id, String emailUsuario, String claveUsuario){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO);

        ModeloUsuario usuario = new ModeloUsuario(emailUsuario,claveUsuario);
        agendaReferencia.child(id).child(FirebaseReferencias.REFERENCIA_DATO).setValue(usuario);
    }



}
