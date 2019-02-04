package servicios;

import android.text.InputType;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import modelos.ModeloExamen;
import modelos.ModeloPlanEstudio;
import modelos.ModeloPlanificacion;

/**
 * Clase que se encarga de las actividades de gestión de la información relacionada a las
 * planificaciones del usuario, principalmente el registro de datos, y su posterior recuperación.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class GestorPlanificador {
    ConexionBDOnline conexion = new ConexionBDOnline();
    JSONObject resultadoObtenido = new JSONObject();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    /**
     * Método que permite registrar una planificación en la base de datos.
     *
     * @param planificacion Planificacón a registrar.
     * @param tipoMateria String que identfica el tipo de materia vinculada al examen de la
     *                    planificación
     * @return Retorna el identificador generado por el sistema.
     */
    public String registarPlanificacion(ModeloPlanificacion planificacion, String tipoMateria){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(tipoMateria);

        String id = agendaReferencia.push().getKey();
        agendaReferencia.child(id).setValue(planificacion);
        return id;
    }

    /**
     * Método que permite actualizar una planificación en la base de datos.
     *
     * @param plan Planificación a actualizar.
     */
    public void actualizarPlanificacion(ModeloPlanificacion plan){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO);

        agendaReferencia.child(user.getUid()).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(plan.getTipoMateria()).child(plan.getIdPlanificacion()).setValue(plan);
    }

    /**
     * Método que permite eliminar una planificación en la base de datos.
     *
     * @param modelo Planificación a eliminar.
     */
    public void eliminarPlanificacion(ModeloPlanificacion modelo) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(modelo.getTipoMateria()).child(modelo.getIdPlanificacion());

        agendaReferencia.removeValue();
    }

    /**
     * Método que permite registrar los planes de estudio que conforman una planificación.
     *
     * @param idPlanificacion Identificador de la planificación.
     * @param tipoMateria String correspondiente al tipo de materia del examen vinculado.
     * @param planes Conjunto de planes pertenecientes a la planificación.
     */
    public void agregarPlanes(String idPlanificacion, String tipoMateria, ArrayList<ModeloPlanEstudio> planes){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(tipoMateria).child(idPlanificacion);
        for (ModeloPlanEstudio plan: planes) {
            String idPlan= agendaReferencia.child(FirebaseReferencias.REFERENCIA_PLANESTUDIO).push().getKey();
            plan.setIdPlanEstudio(idPlan);
            agendaReferencia.child(FirebaseReferencias.REFERENCIA_PLANESTUDIO).child(idPlan).setValue(plan);
        }
    }

    /**
     * Método que permite actualizar los planes de estudio que conforman una planificación
     * en particular.
     *
     * @param idPlanificacion Identificador de la planificación.
     * @param tipoMateria String correspondiente al tipo de materia del examen vinculado.
     * @param planes Conjunto de planes pertenecientes a la planificación.
     */
    public void actualizarPlanes(String idPlanificacion, String tipoMateria, ArrayList<ModeloPlanEstudio> planes){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(tipoMateria).child(idPlanificacion);
        for (ModeloPlanEstudio plan: planes) {
            agendaReferencia.child(FirebaseReferencias.REFERENCIA_PLANESTUDIO).child(plan.getIdPlanEstudio()).setValue(plan);
        }
    }

    /**
     * Método que permite eliminar los planes de estudio que conforman una planificación especifica.
     *
     * @param idPlanificacion Identificador de la planificación.
     * @param tipoMateria String correspondiente al tipo de materia del examen vinculado.
     */
    public void eliminarPlanes(String idPlanificacion, String tipoMateria){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(tipoMateria).child(idPlanificacion).child(FirebaseReferencias.REFERENCIA_PLANESTUDIO);
        agendaReferencia.removeValue();
    }

    /**
     * Este método permite la obtención del listado de las planificaciones registradas en la base
     * de datos.
     *
     * @return Colección del planificaciones.
     */
    public ArrayList<ModeloPlanificacion> obtenerListadoPlanificaciones() {
        ArrayList<ModeloPlanificacion> array = new ArrayList<>();
        ArrayList<String> idPlanificaciones= new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        try {

            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() +"/planificaciones.json");

            Iterator iterator = resultadoObtenido.keys();

            while (iterator.hasNext()) {
                JSONArray resultadoJSON = new JSONArray();
                String tipo = (String) iterator.next();
                JSONObject planificaciones = resultadoObtenido.getJSONObject(tipo);
                Iterator iterator2 = planificaciones.keys();


                while (iterator2.hasNext()) {
                    String key2 = (String) iterator2.next();
                    resultadoJSON.put(planificaciones.get(key2));
                    idPlanificaciones.add(key2);
                }

                for (int i = 0; i < resultadoJSON.length(); i++) {
                    ModeloPlanificacion modelo = new ModeloPlanificacion(resultadoJSON.getJSONObject(i).getString("idExamen"), resultadoJSON.getJSONObject(i).getString("fechaInicio"));
                    modelo.setResultado(resultadoJSON.getJSONObject(i).getLong("resultado"));
                    modelo.setIdPlanificacion(idPlanificaciones.get(i));
                    modelo.setTipoMateria(tipo);
                    array.add(modelo);
                }

                idPlanificaciones.clear();
            }

        } catch (JSONException e) {
            array = null;
        }
        return array;
    }

    /**
     * Este método permite la obtención del listado de los planes de estudios que pertenecen a una
     * planificación registrada en la base de datos.
     *
     * @param planificacion Planificacion seleccionada.
     * @return Coleccón de planes de estudio.
     */
    public ArrayList<ModeloPlanEstudio> obtenerPlanesDeEstudioRegistrados(ModeloPlanificacion planificacion){
        ArrayList<ModeloPlanEstudio> array = new ArrayList<>();

        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() +"/planificaciones/"+planificacion.getTipoMateria()+"/"+planificacion.getIdPlanificacion()+"/planes.json");

        try {
            Iterator iterator = resultadoObtenido.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                resultadoJSON.put(resultadoObtenido.get(key));
            }

            for (int i = 0; i < resultadoJSON.length(); i++) {
                ModeloPlanEstudio plan = new ModeloPlanEstudio(resultadoJSON.getJSONObject(i).getString("fecha"),resultadoJSON.getJSONObject(i).getBoolean("estado"),resultadoJSON.getJSONObject(i).getString("idEvento"),resultadoJSON.getJSONObject(i).getString("idPlanEstudio"));
                plan.setEstado(resultadoJSON.getJSONObject(i).getBoolean("estado"));
                array.add(plan);
            }

        } catch (JSONException e) {
            array = null;
        }
        return array;
    }

    /**
     * Método que actualiza la planificación al registrar el resultado de un examen.
     *
     * @param idExamen String correspondiente al identificador del examen.
     * @param resultado Resultado del examen.
     */
    public void registrarResultadosPlanificacion(String idExamen, String resultado){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_PLANIFICACION);

        float nota = Float.parseFloat(resultado);
        ArrayList<ModeloPlanificacion> array = this.obtenerListadoPlanificaciones();
        for (ModeloPlanificacion plan: array) {
            if (plan.getIdExamen().equals(idExamen)){
                agendaReferencia.child(plan.getTipoMateria()).child(plan.getIdPlanificacion()).child("resultado").setValue(nota);
                this.verificarRealizacionDelPlan(plan);
            }
        }
    }

    /**
     * Método que remueve los planes de estudio una vez la planificación finaliza, y se asegura de
     * salvaguardar los datos importantes para las demás funciones de la aplicación.
     *
     * @param planificacion Planificacion seleccionada.
     */
    public void verificarRealizacionDelPlan(ModeloPlanificacion planificacion){
        GestorEvento gestorEvento = new GestorEvento();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_PLANIFICACION).child(planificacion.getTipoMateria()).child(planificacion.getIdPlanificacion());
        int aux =0;

        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() +"/planificaciones/"+planificacion.getTipoMateria()+"/"+planificacion.getIdPlanificacion()+"/planes.json");

        try {
            Iterator iterator = resultadoObtenido.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                resultadoJSON.put(resultadoObtenido.get(key));
            }

            for (int i = 0; i < resultadoJSON.length(); i++) {
               if (resultadoJSON.getJSONObject(i).getBoolean("estado")==true){
                   aux++;
               }
            }

            if (aux>0){
                agendaReferencia.child("planes").removeValue();
            }else{
                agendaReferencia.removeValue();
            }
            for (int i = 0; i < resultadoJSON.length(); i++) {
                gestorEvento.eliminarEvento(resultadoJSON.getJSONObject(i).getString("idEvento"),resultadoJSON.getJSONObject(i).getString("fecha"));
            }

        } catch (JSONException e) {
        }

    }

    /**
     * Método que genera un listado de planificación según el tipo de materia al que pertenece el
     * examen vinculado con la planificación.
     *
     * @param tipoMateria String que corresponde al tipo de materia del examen.
     * @param nombreMateria String que corresponde al nombre de la materia del examen.
     * @return Retorna una coleccion de planificaciones encontradas.
     */
    public ArrayList<ModeloPlanificacion> obtenerListadoPlanificacionesPorTipo(String tipoMateria, String nombreMateria) {
        ArrayList<ModeloPlanificacion> array = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        GestorExamen gestorExamen = new GestorExamen();
        int aux=0;

        try {

            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() +"/planificaciones/"+tipoMateria+".json");

            Iterator iterator = resultadoObtenido.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                resultadoJSON.put(resultadoObtenido.get(key));

                ModeloExamen examen = gestorExamen.obtenerDatosExamenPorId(resultadoJSON.getJSONObject(aux).getString("idExamen"));

                if (examen.getResultado().isEmpty()==false && examen.getMateria().equals(nombreMateria)) {
                    ModeloPlanificacion modelo = new ModeloPlanificacion(resultadoJSON.getJSONObject(aux).getString("idExamen"), resultadoJSON.getJSONObject(aux).getString("fechaInicio"));
                    modelo.setHoras(resultadoJSON.getJSONObject(aux).getInt("horasSemanales"));
                    modelo.setResultado(BigDecimal.valueOf(resultadoJSON.getJSONObject(aux).getDouble("resultado")).floatValue());
                    modelo.setIdPlanificacion(key);
                    modelo.setTipoMateria(tipoMateria);
                    array.add(modelo);
                }
                aux++;
            }
            aux=0;

        } catch (JSONException e) {
            array = null;
        }
        return array;
    }

}
