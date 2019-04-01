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

/**
 * Clase que se encarga de las actividades de gestión de la información relacionada a los exámenes
 * del usuario, principalmente el registro de datos, y su posterior recuperación.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class GestorExamen {
    ConexionBDOnline conexion = new ConexionBDOnline();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    JSONObject resultadoObtenido = new JSONObject();

    /**
     * Método que obtiene una lista con todos los exámenes registrados en la base de datos.
     *
     * @return Listado de examenes encontrados.
     */
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

    /**
     * Método que permite el registro de exámenes en la base de datos.
     *
     * @param examen Examen a registrar.
     * @param evento Evento vinculado al examen a registrar.
     */
    public void agregarExamen(ModeloExamen examen, String evento) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid());
        examen.setIdEvento(evento);
        agendaReferencia.child(FirebaseReferencias.REFERENCIA_EXAMEN).child(evento).setValue(examen);
    }

    /**
     * Método que permite el actualizar un examen en la base de datos.
     *
     * @param idExamen String que corresponde al identificador del examen.
     * @param examenModificado examen a modificar.
     */
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

    /**
     * Método que permite eliminar el registro de un examen en la base de datos.
     *
     * @param id String correspondiente al identificador del examen.
     * @param fecha String que represena la fecha del examen.
     */
    public void eliminarExamen(String id, String fecha) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EXAMEN).child(id);
        DatabaseReference agendaReferencia2 = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EVENTO).child(fecha).child(id);
        agendaReferencia.removeValue();
        agendaReferencia2.removeValue();
    }

    /**
     * Método que permite obtener la información de un examen, mediante su identificador.
     *
     * @param idExamen String correspondiente al identificador del examen.
     * @return ModeloExamen con la informacion buscada.
     */
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

    /**
     * Método que obtiene un listado de los exámenes a los que todavía no se les ha asignado
     * un resultado.
     *
     * @return Retorna un listado de exámenes.
     */
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

    /**
     * Método que obtiene un listado de los exámenes que pertenezcan a una materia en particular.
     *
     * @param materia String que identifica la materia.
     * @return Retorna un listado de examenes.
     */
    public ArrayList<ModeloExamen> obtenerListadoExamenesPorMateria(String materia) {
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
                if (materia.equals(resultadoJSON.getJSONObject(i).getString("materia")) && resultadoJSON.getJSONObject(i).getString("resultado").equals("")==false) {
                    ModeloExamen modelo = new ModeloExamen(resultadoJSON.getJSONObject(i).getString("fecha"),
                            resultadoJSON.getJSONObject(i).getString("temas"),
                            resultadoJSON.getJSONObject(i).getString("materia"),
                            resultadoJSON.getJSONObject(i).getString("idMateria"));
                    modelo.setResultado(resultadoJSON.getJSONObject(i).getString("resultado"));
                    modelo.setIdEvento(resultadoJSON.getJSONObject(i).getString("idEvento"));
                    modelo.setIdExamen(idExamenes.get(i));
                    array.add(modelo);
                }
            }
            if (array.isEmpty()){array=null;}

        } catch (JSONException e) {
            array = null;
        }
        return array;
    }


}
