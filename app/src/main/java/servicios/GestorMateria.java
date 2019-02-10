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

/**
 * Clase que se encarga de las actividades de gestión de la información relacionada a las materias
 * del usuario, principalmente el registro de datos, y su posterior recuperación.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class GestorMateria {
    ConexionBDOnline conexion = new ConexionBDOnline();
    JSONObject resultadoObtenido = new JSONObject();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    /**
     * Método que permite actualizar los registros de una materia en particular.
     *
     * @param idMateria Identificador de la materia.
     * @param materia Materia a registrar.
     * @param horarios Colección de horarios pertenecientes a una materia.
     */
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

    /**
     * Método que permite registrar una materia en particular.
     *
     * @param materia Materia a registrar.
     * @param horarios Horarios relacionados con la materia a registrar.
     */
    public void registrarMateria(ModeloMateria materia, final ArrayList<ModeloHorarios> horarios){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO);
        String idMateria = agendaReferencia.child(user.getUid()).child(FirebaseReferencias.REFERENCIA_MATERIA).push().getKey();
        agendaReferencia.child(user.getUid()).child(FirebaseReferencias.REFERENCIA_MATERIA).child(idMateria).setValue(materia);

        for (ModeloHorarios hora: horarios) {
            agendaReferencia.child(user.getUid()).child(FirebaseReferencias.REFERENCIA_MATERIA).child(idMateria).child(FirebaseReferencias.REFERENCIA_HORARIO).push().setValue(hora);
        }

    }

    /**
     * Método que permite la eliminación de una materia en particular.
     *
     * @param idMateria Identificador de la materia.
     */
    public void eliminarMateria(String idMateria) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_MATERIA).child(idMateria);
        agendaReferencia.removeValue();
    }

    /**
     * Método que permite la recuperación de la información perteneciente a una materia
     * en específico.
     *
     * @param ID Identificador de la materia.
     * @return Retorna un ModeloMateria.
     */
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

    /**
     * Método que genera un listado con todas las materias registradas en la base de datos.
     *
     * @return Listado de materias registradas.
     */
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

    /**
     * Método que permite obtener los horarios pertenecientes a una materia en particular.
     *
     * @param idMateria Identificador de la materia.
     * @return Retorna un listado con las materias registradas.
     */
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

    /**
     * Método que permite obtener las materias y sus horarios según el día de la semana
     * en particular.
     *
     * @param dia String que corresponde al nombre del dia de la semana.
     * @return Listado de materias y sus horarios.
     */
    public ArrayList<String> obtenerListadoMateriasHorariosPorDia(String dia){
        ArrayList<String> array = new ArrayList<>();
        ArrayList<String> idMaterias = new ArrayList<>();
        String diaTraduccido="";

        switch (dia.toLowerCase()){
            case "monday":
                diaTraduccido="Lunes";
                break;
            case "tuesday":
                diaTraduccido="Martes";
                break;
            case "wednesday":
                diaTraduccido="Miércoles";
                break;
            case "thursday":
                diaTraduccido="Jueves";
                break;
            case "friday":
                diaTraduccido="Viernes";
                break;
            case "saturday":
                diaTraduccido="Sábado";
                break;
            case "Sunday":
                diaTraduccido="Domingo";
                break;
            default:
                break;
        }

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
                if (resultadoJSON.getJSONObject(i).getString("estado").equals("Cursando")) {
                    ArrayList<ModeloHorarios> arrayHorarios = new ArrayList<>();
                    arrayHorarios= obtenerHorariosPorMateria(idMaterias.get(i));

                    for (ModeloHorarios hora:arrayHorarios) {
                        if (hora.getDia().equals(diaTraduccido)){
                            array.add("Cursado de: "+resultadoJSON.getJSONObject(i).getString("nombre")+" - desde:"+ hora.getHoraInicio()+" - hasta:"+hora.getHoraFin());
                        }
                    }
                }
            }
            if (array.isEmpty()){array=null;}

        }catch (JSONException e){
            array=null;
        }
        return array;
    }

    /**
     * Método similar al anterior pero que permite obtener los modelos de horarios de las materias
     * activas en un dia.
     *
     * @param dia String que corresponde al nombre del dia de la semana.
     * @return Listado de modelos de horarios.
     */
    public ArrayList<ModeloHorarios> obtenerListadoModelosHorariosPorDia(String dia){
        ArrayList<ModeloHorarios> array = new ArrayList<>();
        ArrayList<String> idMaterias = new ArrayList<>();
        String diaTraduccido="";

        switch (dia.toLowerCase()){
            case "monday":
                diaTraduccido="Lunes";
                break;
            case "tuesday":
                diaTraduccido="Martes";
                break;
            case "wednesday":
                diaTraduccido="Miércoles";
                break;
            case "thursday":
                diaTraduccido="Jueves";
                break;
            case "friday":
                diaTraduccido="Viernes";
                break;
            case "saturday":
                diaTraduccido="Sábado";
                break;
            case "Sunday":
                diaTraduccido="Domingo";
                break;
            default:
                break;
        }

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
                if (resultadoJSON.getJSONObject(i).getString("estado").equals("Cursando")) {
                    ArrayList<ModeloHorarios> arrayHorarios = new ArrayList<>();
                    arrayHorarios= obtenerHorariosPorMateria(idMaterias.get(i));

                    for (ModeloHorarios hora:arrayHorarios) {
                        if (hora.getDia().equals(diaTraduccido)){
                            array.add(new ModeloHorarios(diaTraduccido,hora.getHoraInicio(),hora.getHoraFin()));
                        }
                    }
                }
            }
            if (array.isEmpty()){array=null;}

        }catch (JSONException e){
            array=null;
        }
        return array;
    }



}
