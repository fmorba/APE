package servicios;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.ActivityCompat;

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
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import modelos.ModeloEvento;
import modelos.ModeloHorarios;

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
    String timezone = "America/Argentina/Buenos_Aires";
    String calID = "";
    Context context;
    ContentResolver cr;
    public static final String[] EVENT_PROJECTION = new String[]{
            Calendars._ID,
            Calendars.ACCOUNT_NAME,
            Calendars.CALENDAR_DISPLAY_NAME,
            Calendars.OWNER_ACCOUNT
    };
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    public GestorEvento(Context c) {
        Cursor cur = null;
        context = c;
        cr = context.getContentResolver();
        Uri uri = Calendars.CONTENT_URI;
        String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
                + Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[]{user.getEmail(), "com.google",
                user.getEmail()};
        int ds = ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_CALENDAR);
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        }
        if (cur != null) {
            while (cur.moveToNext()) {
                String displayName = null;
                String accountName = null;
                String ownerName = null;

                calID = cur.getLong(PROJECTION_ID_INDEX) + "";
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
            }
        }

    }

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
                modelo.setIdEventoCalendario(resultadoJSON.getJSONObject(i).getLong("idEventoCalendario"));
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
        String IDregistro;
        long eventID = 0;
        long remainderID = 0;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EVENTO).child(fecha);

        String resultado = validarHorarios(fecha, evento.getHoraInicio(), evento.getHoraFin());

        if (resultado.equals("")) {
            long startMillis = 0;
            long endMillis = 0;
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(Integer.valueOf(fecha.split("-")[0]), Integer.valueOf(fecha.split("-")[1]) - 1, Integer.valueOf(fecha.split("-")[2]), Integer.valueOf(evento.getHoraInicio().split(":")[0]), Integer.valueOf(evento.getHoraInicio().split(":")[1]));
            startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.set(Integer.valueOf(fecha.split("-")[0]), Integer.valueOf(fecha.split("-")[1]) - 1, Integer.valueOf(fecha.split("-")[2]), Integer.valueOf(evento.getHoraFin().split(":")[0]), Integer.valueOf(evento.getHoraFin().split(":")[1]));
            endMillis = endTime.getTimeInMillis();

            ContentValues values = new ContentValues();
            values.put(Events.DTSTART, startMillis);
            values.put(Events.DTEND, endMillis);
            values.put(Events.TITLE, evento.getNombre());
            values.put(Events.DESCRIPTION, evento.getDescripcion());
            values.put(Events.CALENDAR_ID, calID);
            values.put(Events.EVENT_TIMEZONE, timezone);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                Uri uri = cr.insert(Events.CONTENT_URI, values);
                eventID = Long.parseLong(uri.getLastPathSegment());

                if (evento.isRecordatorio() == true) {
                    ContentValues values2 = new ContentValues();
                    values2.put(Reminders.MINUTES, 60 * 24);
                    values2.put(Reminders.EVENT_ID, eventID);
                    values2.put(Reminders.METHOD, Reminders.METHOD_ALERT);
                    Uri uri2 = cr.insert(Reminders.CONTENT_URI, values2);
                    remainderID = Long.parseLong(uri2.getLastPathSegment());
                    evento.setIdRecordatorio(remainderID);
                }

                String key = agendaReferencia.child(FirebaseReferencias.REFERENCIA_EVENTO).child(fecha).push().getKey();
                evento.setIdEventoCalendario(eventID);
                agendaReferencia.child(key).setValue(evento);
                IDregistro=key;
            } else {
                return resultado;
            }
            return "Completado. - " + IDregistro;

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

            long startMillis = 0;
            long endMillis = 0;

            Calendar beginTime = Calendar.getInstance();
            beginTime.set(Integer.valueOf(fecha.split("-")[0]), Integer.valueOf(fecha.split("-")[1]) - 1, Integer.valueOf(fecha.split("-")[2]), Integer.valueOf(evento.getHoraInicio().split(":")[0]), Integer.valueOf(evento.getHoraInicio().split(":")[1]));
            startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.set(Integer.valueOf(fecha.split("-")[0]), Integer.valueOf(fecha.split("-")[1]) - 1, Integer.valueOf(fecha.split("-")[2]), Integer.valueOf(evento.getHoraFin().split(":")[0]), Integer.valueOf(evento.getHoraFin().split(":")[1]));
            endMillis = endTime.getTimeInMillis();

            ContentValues values = new ContentValues();
            ContentValues values2 = new ContentValues();
            values.put(Events.DTSTART, startMillis);
            values.put(Events.DTEND, endMillis);
            values.put(Events.TITLE, evento.getNombre());
            values.put(Events.DESCRIPTION, evento.getDescripcion());
            values.put(Events.CALENDAR_ID, calID);
            values.put(Events.EVENT_TIMEZONE, timezone);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {

                if (evento.isRecordatorio() == true){
                    if(evento.getIdRecordatorio() == null) {
                        values2.put(Reminders.MINUTES, 60 * 24);
                        values2.put(Reminders.EVENT_ID, evento.getIdEventoCalendario());
                        values2.put(Reminders.METHOD, Reminders.METHOD_ALERT);
                        Uri uri2 = cr.insert(Reminders.CONTENT_URI, values2);
                        long remainderID = Long.parseLong(uri2.getLastPathSegment());
                        evento.setIdRecordatorio(remainderID);
                    }else {
                        values2.put(Reminders.MINUTES, 60 * 24);
                        values2.put(Reminders.EVENT_ID, evento.getIdEventoCalendario());
                        values2.put(Reminders.METHOD, Reminders.METHOD_ALERT);
                        Uri uri2  = ContentUris.withAppendedId(Reminders.CONTENT_URI, evento.getIdEventoCalendario());
                        int rows = cr.update(uri2, values2, null, null);
                        long remainderID = Long.parseLong(uri2.getLastPathSegment());
                        evento.setIdRecordatorio(remainderID);
                    }
                }else{
                    if(evento.getIdRecordatorio() != null) {
                        Uri uri2  = ContentUris.withAppendedId(Reminders.CONTENT_URI, evento.getIdRecordatorio());
                        int rows = cr.delete(uri2, null, null);
                        evento.setIdRecordatorio(null);
                    }
                }
                Uri updateUri = null;
                updateUri = ContentUris.withAppendedId(Events.CONTENT_URI, evento.getIdEventoCalendario());
                int rows = cr.update(updateUri, values, null, null);
            }

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

        Long idCalendario = obtenerIdGoogleCalendarEvento(idEvento,fecha);

        agendaReferencia.removeValue();
        if(idCalendario!=null) {
            ContentValues values = new ContentValues();
            Uri deleteUri = null;
            deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, idCalendario);
            int rows = cr.delete(deleteUri, null, null);
        }
    }

    /**
     * Método que permite eliminar todos los eventos pertenecientes al usuario.
     */
    public void eliminarTodosLosEventoSegunUsuario() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EVENTO);

        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos.json");

        try {

            Iterator iterator = resultadoObtenido.keys();

            while (iterator.hasNext()) {
                JSONArray resultadoJSON = new JSONArray();
                String key = (String) iterator.next();

                Calendar dateEvento = Calendar.getInstance();

                JSONObject eventos = resultadoObtenido.getJSONObject(key);
                Iterator iterator2 = eventos.keys();

                while (iterator2.hasNext()) {
                        String key2 = (String) iterator2.next();
                        resultadoJSON.put(eventos.get(key2));
                }

                for (int i = 0; i < resultadoJSON.length(); i++) {
                    Long idEvento = Long.valueOf(key);
                    ContentValues values = new ContentValues();
                    Uri deleteUri = null;
                    deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, Long.valueOf(idEvento));
                    int rows = cr.delete(deleteUri, null, null);
                }

            }
        } catch (JSONException e) {

        }
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
            return resultado;
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
            DateFormat formatFecha = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date testFecha = formatFecha.parse(fecha);
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
            return resultado;
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
            Long idEventoCalendario = resultadoObtenido.getLong("idEventoCalendario");
            boolean recordatorio = resultadoObtenido.getBoolean("recordatorio");

            eventoBuscado = new ModeloEvento(nombre, horaInicio, horaFin, descripcion, recordatorio);
            eventoBuscado.setIdEventoCalendario(idEventoCalendario);

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
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            int año = Integer.valueOf(fecha.split("-")[0]);
            int mes = Integer.valueOf(fecha.split("-")[1]);
            int dia = Integer.valueOf(fecha.split("-")[2]);
            Calendar fechaHoy = Calendar.getInstance();
            fechaHoy.set(año, mes, dia);

            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos.json");

            try {

                Iterator iterator = resultadoObtenido.keys();

                while (iterator.hasNext()) {
                    JSONArray resultadoJSON = new JSONArray();
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
        } catch (NumberFormatException e) {
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
                    ContentValues values = new ContentValues();
                    Uri deleteUri = null;
                    deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, Long.valueOf(idEvento));
                    int rows = cr.delete(deleteUri, null, null);
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

        GestorMateria gestorMateria = new GestorMateria();
        ArrayList<String> horas = new ArrayList<>();
        ArrayList<ModeloHorarios> cursado = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatDate.parse(fecha);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            String dia = sdf.format(date);

            cursado = gestorMateria.obtenerListadoModelosHorariosPorDia(dia);

            for (int i = 10; i < 19; i++) {
                boolean aux = false;
                String horaIni = i + ":00";
                String horaFin = (i + 1) + ":00";
                String libre = validarHorarios(fecha, horaIni, horaFin);

                java.util.Date inicioEvento = format.parse(horaIni);
                java.util.Date finEvento = format.parse(horaFin);

                if (cursado != null) {
                    for (ModeloHorarios hora : cursado) {

                        java.util.Date inicioEventoRegistrado = format.parse(hora.getHoraInicio());
                        java.util.Date finEventoRegistrado = format.parse(hora.getHoraFin());

                        if ((inicioEvento.before(inicioEventoRegistrado) && finEvento.after(inicioEventoRegistrado)) || (inicioEvento.after(inicioEventoRegistrado) && inicioEvento.before(finEventoRegistrado)) || inicioEvento.toString().equals(inicioEventoRegistrado.toString()) || inicioEvento.toString().equals(finEventoRegistrado.toString()) || finEvento.toString().equals(inicioEventoRegistrado.toString()) || finEvento.toString().equals(finEventoRegistrado.toString())) {
                            aux = true;
                        }
                    }
                }
                if (libre.equals("") && aux == false) {
                    horas.add(horaIni + " - " + horaFin);
                }

            }
        } catch (ParseException e) {
            horas = null;
        }

        return horas;
    }

    /**
     * Método que ayuda a determinar los horarios libres en la agenda, en un dia particular, y
     * durante un periodo de tiempo ya especificado.
     *
     * @param idEvento String que representa al identificador del evento.
     * @param fecha String que representa la fecha de busqueda.
     * @return identificador asignado por la app del calendario al correspondiente evento.
     */
    private Long obtenerIdGoogleCalendarEvento(String idEvento, String fecha){
        Long idCalendario;
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos/" + fecha + "/" + idEvento + ".json");

        try {
            idCalendario = resultadoObtenido.getLong("idEventoCalendario");

        } catch (JSONException e) {
            idCalendario=null;
        }
        return idCalendario;
    }
}
