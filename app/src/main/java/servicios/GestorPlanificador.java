package servicios;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import modelos.ModeloPlanEstudio;
import modelos.ModeloPlanificacion;

public class GestorPlanificador {

    public String registarPlanificacion(ModeloPlanificacion planificacion, String tipoMateria, String idUsuario){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(idUsuario).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(tipoMateria);

        String id = agendaReferencia.push().getKey();
        agendaReferencia.child(id).setValue(planificacion);
        return id;
    }

    public void actualizarPlanificacion(String idPlan, ModeloPlanificacion plan, String idUsuario){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO);

        agendaReferencia.child(idUsuario).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(idPlan).setValue(plan);
    }

    public void eliminarPlanificacion(String idPlanificacion) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(idPlanificacion);

        agendaReferencia.removeValue();
    }

    public void agregarPlanes(String idPlanificacion, String tipoMateria, ArrayList<ModeloPlanEstudio> planes){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(tipoMateria).child(idPlanificacion);
        for (ModeloPlanEstudio plan: planes) {
            agendaReferencia.child(FirebaseReferencias.REFERENCIA_PLANESTUDIO).push().setValue(plan);
        }
    }

    public void eliminarPlanes(String idPlanificacion, String tipoMateria){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(tipoMateria).child(idPlanificacion).child(FirebaseReferencias.REFERENCIA_PLANESTUDIO);
        agendaReferencia.removeValue();
    }

}
