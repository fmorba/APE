package servicios;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import modelos.ModeloEvento;

/**
 * Clase que se encarga de las actividades de gestión de la información relacionada a los eventos
 * de la agenda del usuario, principalmente el registro de datos, y su posterior recuperación.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class GestorEvento {
    ConexionBDOnline conexion = new ConexionBDOnline();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    JSONObject resultadoObtenido = new JSONObject();

    /**
     * Método que obtienen un listado de eventos pertenecientes a una fecha específica.
     *
     * @param fecha String que representa una fecha en particular.
     * @return Retorna una coleccion de eventos.
     */
    public ArrayList<ModeloEvento> obtenerEventosSegunFechas(String fecha) {
        ArrayList<ModeloEvento> arrayEventos = new ArrayList<>();
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos.json");

        try {
            JSONObject eventos = resultadoObtenido.getJSONObject(fecha);
            Iterator iterator = eventos.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                resultadoJSON.put(eventos.get(key));
            }

            for (int i = 0; i < resultadoJSON.length(); i++) {
                ModeloEvento modelo = new ModeloEvento(resultadoJSON.getJSONObject(i).getString("nombre"),
                        resultadoJSON.getJSONObject(i).getString("horaInicio"),
                        resultadoJSON.getJSONObject(i).getString("horaFin"),
                        resultadoJSON.getJSONObject(i).getString("descripcion"),
                        resultadoJSON.getJSONObject(i).getBoolean("recordatorio"));
                modelo.setTipo(resultadoJSON.getJSONObject(i).getString("tipo"));
                arrayEventos.add(modelo);
            }
        } catch (JSONException e) {
            arrayEventos = null;
        }
        return arrayEventos;
    }

    /**
     * Método que obtienen un listado de los identificadores de eventos pertenecientes a una
     * fecha específica.
     *
     * @param fecha String que representa una fecha en particular.
     * @return Retorna una coleccion de eventos.
     */
    public ArrayList<String> obtenerIdSegunFechas(String fecha) {
        ArrayList<String> arrayID = new ArrayList<>();
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos.json");

        try {
            JSONObject eventos = resultadoObtenido.getJSONObject(fecha);
            Iterator iterator = eventos.keys();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                arrayID.add(key);
            }

        } catch (JSONException e) {
            arrayID = null;
        }
        return arrayID;
    }

    /**
     * Método que permite el registro de un evento en la base e de datos.
     *
     * @param fecha  String que representa una fecha en particular.
     * @param evento Evento a registrar.
     * @return Retorna mensaje de confirmación o error.
     */
    public String agregarEvento(String fecha, ModeloEvento evento) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EVENTO).child(fecha);

        String resultado = validarHorarios(fecha, evento.getHoraInicio(), evento.getHoraFin());

        if (resultado.equals("")) {
            String key = agendaReferencia.push().getKey();
            agendaReferencia.child(key).setValue(evento);
            return "Completado. - " + key;
        } else return resultado;
    }

    /**
     * Método que permite actualizar un evento de la base e de datos.
     *
     * @param idEvento String que representa el identificador del evento.
     * @param fecha    String que representa una fecha particular.
     * @param evento   Evento a actualizar.
     * @return Retorna un mensaje de confirmación o error.
     */
    public String modificarEvento(String idEvento, String fecha, ModeloEvento evento) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EVENTO).child(fecha).child(idEvento);

        String resultado = validarHorariosModificacion(fecha, evento.getHoraInicio(), evento.getHoraFin(), idEvento);
        if (resultado.equals("")) {
            agendaReferencia.setValue(evento);
            return "Completado.";
        } else return resultado;
    }

    /**
     * Método que permite borrar un evento de la base e de datos.
     *
     * @param idEvento String que representa el identificador del evento.
     * @param fecha    String que representa una fecha en particular.
     */
    public void eliminarEvento(String idEvento, String fecha) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EVENTO).child(fecha).child(idEvento);

        agendaReferencia.removeValue();
    }

    /**
     * Método que permite eliminar todos los eventos pertenecientes al usuario.
     */
    public void eliminarTodosLosEventoSegunUsuario() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EVENTO);

        agendaReferencia.removeValue();
    }

    /**
     * Método cuya función es verificar que no exista un evento registrado en el horario dado.
     *
     * @param fecha   String que representa un fecha.
     * @param horaIni String que representa una hora de inicio.
     * @param horaFin String que representa una hora de finalización.
     * @return Retorna un mensaje de confirmación o error.
     */
    public String validarHorarios(String fecha, String horaIni, String horaFin) {
        String resultado = "Error";
        try {
            DateFormat format = new SimpleDateFormat("HH:mm");
            java.util.Date inicioEvento = format.parse(horaIni);
            java.util.Date finEvento = format.parse(horaFin);


            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos.json");

            try {

                JSONObject eventos = resultadoObtenido.getJSONObject(fecha);
                Iterator iterator = eventos.keys();
                JSONArray resultadoJSON = new JSONArray();

                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    resultadoJSON.put(eventos.get(key));
                }

                for (int i = 0; i < resultadoJSON.length(); i++) {

                    java.util.Date inicioEventoRegistrado = format.parse(resultadoJSON.getJSONObject(i).getString("horaInicio"));
                    java.util.Date finEventoRegistrado = format.parse(resultadoJSON.getJSONObject(i).getString("horaFin"));

                    if ((inicioEvento.before(inicioEventoRegistrado) && finEvento.after(inicioEventoRegistrado)) || (inicioEvento.after(inicioEventoRegistrado) && inicioEvento.before(finEventoRegistrado)) || inicioEvento.toString().equals(inicioEventoRegistrado.toString()) || inicioEvento.toString().equals(finEventoRegistrado.toString()) || finEvento.toString().equals(inicioEventoRegistrado.toString()) || finEvento.toString().equals(finEventoRegistrado.toString())) {
                        throw new InstantiationException("Ya hay un evento registrado a esa hora.");
                    } else {
                        resultado = "";
                    }

                }

            } catch (JSONException e) {
                if (e.getMessage().equals("No value for " + fecha)) {
                    return "";
                }
                return e.getMessage();
            }
        } catch (ParseException e) {
            return e.getMessage();
        } catch (InstantiationException e) {
            return e.getMessage();
        }
        return resultado;
    }

    /**
     * Método cuya función es verificar que no exista un evento registrado en el horario dado, al
     * actualizar un registro.
     *
     * @param fecha    String que representa un fecha.
     * @param horaIni  String que representa una hora de inicio.
     * @param horaFin  String que representa una hora de finalización.
     * @param idEvento String correspondiente al identificador del evento.
     * @return
     */
    public String validarHorariosModificacion(String fecha, String horaIni, String horaFin, String idEvento) {
        String resultado = "Error";
        try {
            DateFormat format = new SimpleDateFormat("HH:mm");
            java.util.Date inicioEvento = format.parse(horaIni);
            java.util.Date finEvento = format.parse(horaFin);


            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos.json");

            try {
                JSONObject eventos = resultadoObtenido.getJSONObject(fecha);
                Iterator iterator = eventos.keys();
                JSONArray resultadoJSON = new JSONArray();

                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    if (key.equals(idEvento) == false) {
                        resultadoJSON.put(eventos.get(key));
                    }
                }
                if (resultadoJSON.length() > 0) {
                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        java.util.Date inicioEventoRegistrado = format.parse(resultadoJSON.getJSONObject(i).getString("horaInicio"));
                        java.util.Date finEventoRegistrado = format.parse(resultadoJSON.getJSONObject(i).getString("horaFin"));

                        if ((inicioEvento.before(inicioEventoRegistrado) && finEvento.after(inicioEventoRegistrado)) || (inicioEvento.after(inicioEventoRegistrado) && inicioEvento.before(finEventoRegistrado)) || inicioEvento.toString().equals(inicioEventoRegistrado.toString()) || inicioEvento.toString().equals(finEventoRegistrado.toString()) || finEvento.toString().equals(inicioEventoRegistrado.toString()) || finEvento.toString().equals(finEventoRegistrado.toString())) {
                            throw new InstantiationException("Ya hay un evento registrado a esa hora.");
                        } else {
                            resultado = "";
                        }
                    }
                } else {
                    resultado = "";
                }

            } catch (JSONException e) {
                return e.getMessage();
            }
        } catch (ParseException e) {
            return e.getMessage();
        } catch (InstantiationException e) {
            return e.getMessage();
        }
        return resultado;
    }

    /**
     * Método que obtiene los datos de  un evento en particular, según su identificador.
     *
     * @param idEvento String correspondiente al identificador del evento.
     * @param fecha    String que representa una fecha en particular.
     * @return Retorna un ModeloEvento con la información requerida.
     */
    public ModeloEvento obtenerDatosEventoPorId(String idEvento, String fecha) {
        ModeloEvento eventoBuscado = null;
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos/" + fecha + "/" + idEvento + ".json");

        try {
            String nombre = resultadoObtenido.getString("nombre");
            String horaInicio = resultadoObtenido.getString("horaInicio");
            String horaFin = resultadoObtenido.getString("horaFin");
            String descripcion = resultadoObtenido.getString("descripcion");
            boolean recordatorio = resultadoObtenido.getBoolean("recordatorio");

            eventoBuscado = new ModeloEvento(nombre, horaInicio, horaFin, descripcion, recordatorio);

        } catch (JSONException e) {
            eventoBuscado = null;
        }
        return eventoBuscado;
    }

    /**
     * Método que devuelve al usuario un listado con los eventos marcados para ser recordados.
     *
     * @param fecha String que representa una fecha especifica.
     * @return Retorna una colección de eventos a ser recordados.
     */
    public ArrayList<String> obtenerRecordatorios(String fecha) {
        ArrayList<String> arrayRecor = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        int año = Integer.valueOf(fecha.split("-")[0]);
        int mes = Integer.valueOf(fecha.split("-")[1]);
        int dia = Integer.valueOf(fecha.split("-")[2]);
        Calendar fechaHoy = Calendar.getInstance();
        fechaHoy.set(año, mes, dia);

        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos.json");

        try {

            Iterator iterator = resultadoObtenido.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();

                int añoEvento = Integer.valueOf(key.split("-")[0]);
                int mesEvento = Integer.valueOf(key.split("-")[1]);
                int diaEvento = Integer.valueOf(key.split("-")[2]);
                Calendar dateEvento = Calendar.getInstance();
                dateEvento.set(añoEvento, mesEvento, diaEvento);
                long diferencia = dateEvento.getTimeInMillis() - fechaHoy.getTimeInMillis();

                if (TimeUnit.MILLISECONDS.toDays(diferencia) < 7 && TimeUnit.MILLISECONDS.toDays(diferencia) > 0) {
                    JSONObject eventos = resultadoObtenido.getJSONObject(key);
                    Iterator iterator2 = eventos.keys();

                    while (iterator2.hasNext()) {
                        String key2 = (String) iterator2.next();
                        resultadoJSON.put(eventos.get(key2));
                    }

                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        if (resultadoJSON.getJSONObject(i).getBoolean("recordatorio") == true) {
                            String resultado = key + " - " + resultadoJSON.getJSONObject(i).getString("nombre") + " - " + resultadoJSON.getJSONObject(i).getString("horaInicio") + " - " + resultadoJSON.getJSONObject(i).getString("horaFin");
                            arrayRecor.add(resultado);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            arrayRecor = null;
        }
        return arrayRecor;
    }

    /**
     * Método cuya función es eliminar todos los eventos anteriores a la fecha del día de hoy.
     *
     * @param fecha String correspondiente al día de hoy.
     * @return
     */
    public String eliminarEventosAntiguos(String fecha) {
        int año = Integer.valueOf(fecha.split("-")[0]);
        int mes = Integer.valueOf(fecha.split("-")[1]);
        int dia = Integer.valueOf(fecha.split("-")[2]);
        Calendar fechaHoy = Calendar.getInstance();
        fechaHoy.set(año, mes, dia);
        Calendar fechaLimite = Calendar.getInstance();
        fechaLimite.add(fechaHoy.DATE, -1);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String limite = dateFormat.format(fechaLimite.getTime()).toString();

        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos.json?orderBy=\"$key\"&endAt=\"" + limite + "\"");

        try {

            Iterator iterator = resultadoObtenido.keys();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                JSONArray resultadoJSON = new JSONArray();
                ArrayList<String> ids = new ArrayList<>();

                JSONObject eventos = resultadoObtenido.getJSONObject(key);
                Iterator iterator2 = eventos.keys();

                while (iterator2.hasNext()) {
                    String key2 = (String) iterator2.next();
                    resultadoJSON.put(eventos.get(key2));
                    ids.add(key2);
                }

                for (int i = 0; i < resultadoJSON.length(); i++) {
                    String fechaEvento = key;
                    String idEvento = ids.get(i);
                    eliminarEvento(idEvento, fechaEvento);
                }

            }

        } catch (JSONException e) {
            return e.getMessage();
        }
        return "Eliminaciòn exitosa.";

    }

    /**
     * Método que ayuda a determinar los horarios libres en la agenda, en un dia particular, y
     * durante un periodo de tiempo ya especificado.
     *
     * @param fecha String que representa la fecha de busqueda.
     * @return Coleccion de horarios sin actividades asignadas.
     */
    public ArrayList<String> horasLibres(String fecha) {
        ArrayList<String> horas = new ArrayList<>();

        for (int i = 10; i < 19; i++) {
            String horaIni = i + ":00";
            String horaFin = (i + 1) + ":00";
            String libre = validarHorarios(fecha, horaIni, horaFin);
            if (libre.equals("")) {
                horas.add(horaIni + " - " + horaFin);
            }
        }

        return horas;
    }
}
