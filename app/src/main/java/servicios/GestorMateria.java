package servicios;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import modelos.ModeloHorarios;
import modelos.ModeloMateria;

public class GestorMateria {
    ConexionBDOnline conexion = new ConexionBDOnline();
    JSONObject resultadoObtenido = new JSONObject();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public void actualizarDatosMateria(String idMateria, ModeloMateria materia, ArrayList<ModeloHorarios> horarios){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_MATERIA).child(idMateria);
        agendaReferencia.child("nombre").setValue(materia.getNombre());
        agendaReferencia.child("tipo").setValue(materia.getTipo());
        agendaReferencia.child("dificultad").setValue(materia.getDificultad());
        agendaReferencia.child("estado").setValue(materia.getEstado());

        DatabaseReference horariosReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_MATERIA).child(idMateria).child(FirebaseReferencias.REFERENCIA_HORARIO);
        horariosReferencia.removeValue();
        for (ModeloHorarios hora: horarios) {
            horariosReferencia.push().setValue(hora);
        }
    }

    public void registrarMateria(ModeloMateria materia, final ArrayList<ModeloHorarios> horarios){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO);
        String idMateria = agendaReferencia.child(user.getUid()).child(FirebaseReferencias.REFERENCIA_MATERIA).push().getKey();
        agendaReferencia.child(user.getUid()).child(FirebaseReferencias.REFERENCIA_MATERIA).child(idMateria).setValue(materia);

        for (ModeloHorarios hora: horarios) {
            agendaReferencia.child(user.getUid()).child(FirebaseReferencias.REFERENCIA_MATERIA).child(idMateria).child(FirebaseReferencias.REFERENCIA_HORARIO).push().setValue(hora);
        }

    }

    public void eliminarMateria(String idMateria) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_MATERIA).child(idMateria);
        agendaReferencia.removeValue();
    }

    public ModeloMateria obtenerDatosMateria(String ID){
        ModeloMateria modelo;
        String nombre,tipo, dificultad, estado;
        ArrayList<ModeloHorarios> arrayHorarios = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/materias/"+ID+".json");

        try {
            nombre= resultadoObtenido.getString("nombre");
            tipo = resultadoObtenido.getString("tipo");
            dificultad = resultadoObtenido.getString("dificultad");
            estado = resultadoObtenido.getString("estado");
            modelo = new ModeloMateria(nombre,tipo,dificultad,estado);

            arrayHorarios= obtenerHorariosPorMateria(ID);
            modelo.setHorarios(arrayHorarios);

        } catch (JSONException e) {
            modelo=null;
        }

        return modelo;
    }

    public ArrayList<ModeloMateria> obtenerListadoMaterias(){
        ArrayList<ModeloMateria> array = new ArrayList<>();
        ArrayList<String> idMaterias = new ArrayList<>();
        try {

            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + ".json");

            JSONObject eventos = resultadoObtenido.getJSONObject("materias");
            Iterator iterator = eventos.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()){
               String key = (String) iterator.next();
               resultadoJSON.put(eventos.get(key));
               idMaterias.add(key);
            }


            for (int i = 0; i < resultadoJSON.length(); i++) {
                ModeloMateria modelo = new ModeloMateria(resultadoJSON.getJSONObject(i).getString("nombre"),
                        resultadoJSON.getJSONObject(i).getString("tipo"),
                        resultadoJSON.getJSONObject(i).getString("dificultad"),
                        resultadoJSON.getJSONObject(i).getString("estado"));
                modelo.setIdMateria(idMaterias.get(i));
                array.add(modelo);
            }

        }catch (JSONException e){
            array=null;
        }
        return array;
    }

    public ArrayList<ModeloHorarios> obtenerHorariosPorMateria(String idMateria){
        ArrayList<ModeloHorarios> array = new ArrayList<>();

        try {

            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/materias/"+idMateria+".json");

            JSONObject horarios = resultadoObtenido.getJSONObject("horarios");
            Iterator iterator = horarios.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()){
                String key = (String) iterator.next();
                resultadoJSON.put(horarios.get(key));
            }

            for (int i = 0; i < resultadoJSON.length(); i++) {
                ModeloHorarios modelo = new ModeloHorarios(resultadoJSON.getJSONObject(i).getString("dia"),
                        resultadoJSON.getJSONObject(i).getString("horaInicio"),
                        resultadoJSON.getJSONObject(i).getString("horaFin"));
                array.add(modelo);
            }

        }catch (JSONException e){
            array=null;
        }
        return array;
    }



}
