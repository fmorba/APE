package servicios;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.IDN;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import modelos.ModeloEvento;
import modelos.ModeloExamen;

public class GestorExamen {
    ConexionBDOnline conexion = new ConexionBDOnline();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    JSONObject resultadoObtenido = new JSONObject();

    public ArrayList<ModeloExamen> obtenerListadoExamenes() {
        ArrayList<ModeloExamen> array = new ArrayList<>();
        ArrayList<String> idExamenes = new ArrayList<>();
        try {

            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + ".json");

            JSONObject examenes = resultadoObtenido.getJSONObject(FirebaseReferencias.REFERENCIA_EXAMEN);
            Iterator iterator = examenes.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                resultadoJSON.put(examenes.get(key));
                idExamenes.add(key);
            }

            for (int i = 0; i < resultadoJSON.length(); i++) {
                ModeloExamen modelo = new ModeloExamen(resultadoJSON.getJSONObject(i).getString("fecha"),
                        resultadoJSON.getJSONObject(i).getString("temas"),
                        resultadoJSON.getJSONObject(i).getString("materia"),
                        resultadoJSON.getJSONObject(i).getString("idMateria"));
                modelo.setResultado(resultadoJSON.getJSONObject(i).getString("resultado"));
                modelo.setIdEvento(resultadoJSON.getJSONObject(i).getString("idEvento"));
                modelo.setIdExamen(idExamenes.get(i));
                array.add(modelo);
            }

        } catch (JSONException e) {
            array = null;
        }
        return array;
    }

    public ArrayList<String> obtenerIdsExamenes(String idUsuario) {
        ArrayList<String> arrayID = new ArrayList<>();
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + idUsuario + ".json");

        try {
            JSONObject examenes = resultadoObtenido.getJSONObject(FirebaseReferencias.REFERENCIA_EXAMEN);
            Iterator iterator = examenes.keys();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                arrayID.add(key);
            }

        } catch (JSONException e) {
            arrayID = null;
        }
        return arrayID;
    }

    public void agregarExamen(ModeloExamen examen, ModeloEvento evento) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid());
        String key = agendaReferencia.child(FirebaseReferencias.REFERENCIA_EVENTO).child(examen.getFecha()).push().getKey();
        evento.setTipo("examen");
        examen.setIdEvento(key);
        agendaReferencia.child(FirebaseReferencias.REFERENCIA_EVENTO).child(examen.getFecha()).child(key).setValue(evento);
        agendaReferencia.child(FirebaseReferencias.REFERENCIA_EXAMEN).child(key).setValue(examen);
    }

    public void modificarExamen(String idExamen, ModeloExamen examenModificado) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EXAMEN).child(idExamen);
        agendaReferencia.child("fecha").setValue(examenModificado.getFecha());
        if (examenModificado.getResultado() != null) {
            agendaReferencia.child("resultado").setValue(examenModificado.getResultado());
        }
        if (examenModificado.getTemas() != null) {
            agendaReferencia.child("temas").setValue(examenModificado.getTemas());
        }
        agendaReferencia.child("idMateria").setValue(examenModificado.getIdMateria());
    }

    public void eliminarExamen(String id, String fecha) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EXAMEN).child(id);
        DatabaseReference agendaReferencia2 = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EVENTO).child(fecha).child(id);
        agendaReferencia.removeValue();
        agendaReferencia2.removeValue();
    }

    public ModeloExamen obtenerDatosExamenPorId(String idExamen) {
        ModeloExamen examenBuscado;
        String resultado = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/examenes/" + idExamen + ".json");
        try {
            String fecha = resultadoObtenido.getString("fecha");
            String materia = resultadoObtenido.getString("materia");
            String temas = resultadoObtenido.getString("temas");
            String idMateria = resultadoObtenido.getString("idMateria");
            String idEvento = resultadoObtenido.getString("idEvento");
            resultado = resultadoObtenido.getString("resultado");
            examenBuscado = new ModeloExamen(fecha, temas, materia, idMateria);
            examenBuscado.setIdExamen(idExamen);
            examenBuscado.setIdEvento(idEvento);
            examenBuscado.setResultado(resultado);
        } catch (JSONException e) {
            examenBuscado = null;
        }
        return examenBuscado;
    }

    public ArrayList<ModeloExamen> obtenerListadoExamenesPendientes() {
        ArrayList<ModeloExamen> array = new ArrayList<>();
        ArrayList<String> idExamenes = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        try {

            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + ".json");

            JSONObject examenes = resultadoObtenido.getJSONObject(FirebaseReferencias.REFERENCIA_EXAMEN);
            Iterator iterator = examenes.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                resultadoJSON.put(examenes.get(key));
                idExamenes.add(key);
            }

            for (int i = 0; i < resultadoJSON.length(); i++) {
                if (resultadoJSON.getJSONObject(i).getString("resultado").isEmpty()) {
                    ModeloExamen modelo = new ModeloExamen(resultadoJSON.getJSONObject(i).getString("fecha"),
                            resultadoJSON.getJSONObject(i).getString("temas"),
                            resultadoJSON.getJSONObject(i).getString("materia"),
                            resultadoJSON.getJSONObject(i).getString("idMateria"));
                    modelo.setIdExamen(idExamenes.get(i));
                    array.add(modelo);
                }
            }

        } catch (JSONException e) {
            array=null;
        }
        return array;
    }


}
