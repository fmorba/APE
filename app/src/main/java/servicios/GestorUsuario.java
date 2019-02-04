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

/**
 * Clase que se encarga de las actividades de gestión de la información relacionada a los datos
 * del usuario, principalmente su registro y recuperación.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
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

    /**
     * Método cuya función es recuperar la información del usuario guarda en la base de datos.
     *
     * @param ID Identificador del usuario.
     * @return Retorna la información del usuario encontrada.
     */
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

    /**
     * Método que permite actualizar el registro del usuario.
     *
     * @param nombre String correspondiente al nombre del usuario.
     * @param fechaNacimiento String correspondiente a la fecha de nacimiento del usuario.
     * @param provincia String correspondiene a la provincia del usuario.
     * @param localidad String correspondiente a la localidad del usuario.
     * @param carrera String correspondiente a la carrera acádemica del usuario.
     */
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

    /**
     * Método que permite registrar la información del usuario.
     *
     * @param id Identificador del usuario.
     * @param emailUsuario email del usuario.
     * @param claveUsuario contraseña del usuario.
     */
    public void registarUsuario(String id, String emailUsuario, String claveUsuario){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO);

        ModeloUsuario usuario = new ModeloUsuario(emailUsuario,claveUsuario);
        agendaReferencia.child(id).child(FirebaseReferencias.REFERENCIA_DATO).setValue(usuario);
    }



}
