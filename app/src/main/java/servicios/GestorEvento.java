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

public class GestorEvento {
    ConexionBDOnline conexion = new ConexionBDOnline();
    ModeloEvento evento;
    JSONObject resultadoObtenido = new JSONObject();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public ArrayList<ModeloEvento> obtenerHorariosSegunFechas(String fecha) {
        ArrayList<ModeloEvento> arrayEventos = new ArrayList<>();
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid()+ "/eventos.json");

        try {
            JSONObject eventos = resultadoObtenido.getJSONObject(fecha);
            Iterator iterator = eventos.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()){
                String key = (String) iterator.next();
                resultadoJSON.put(eventos.get(key));
            }

            for (int i = 0; i < resultadoJSON.length(); i++) {
                ModeloEvento modelo = new ModeloEvento(resultadoJSON.getJSONObject(i).getString("nombre"),
                        resultadoJSON.getJSONObject(i).getString("horaInicio"),
                        resultadoJSON.getJSONObject(i).getString("horaFin"),
                        resultadoJSON.getJSONObject(i).getString("descripcion"),
                        resultadoJSON.getJSONObject(i).getBoolean("recordatorio"));
                arrayEventos.add(modelo);
            }
        } catch (JSONException e) {
            arrayEventos = null;
        }
        return arrayEventos;
    }

    public ArrayList<String> obtenerIdSegunFechas(String fecha) {
        ArrayList<String> arrayID = new ArrayList<>();
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos.json");

        try {
            JSONObject eventos = resultadoObtenido.getJSONObject(fecha);
            Iterator iterator = eventos.keys();

            while (iterator.hasNext()){
                String key = (String) iterator.next();
                arrayID.add(key);
            }

        } catch (JSONException e) {
            arrayID = null;
        }
        return arrayID;
    }

    public String agregarEvento(String fecha,ModeloEvento evento) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO);

        String resultado = validarHorarios(fecha,evento.getHoraInicio(),evento.getHoraFin());

        if (resultado.equals("")) {
            agendaReferencia.child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EVENTO).child(fecha).push().setValue(evento);
            return "Completado.";
        } else return resultado;
    }

    public String modificarEvento(String idEvento, String fecha, ModeloEvento evento) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EVENTO).child(fecha).child(idEvento);

        String resultado = validarHorariosModificacion(fecha,evento.getHoraInicio(), evento.getHoraFin(),idEvento);
        if (resultado.equals("")) {
            agendaReferencia.setValue(evento);
            return "Completado.";
        }else return resultado;
    }

    public void eliminarEvento(String idEvento, String fecha) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_EVENTO).child(fecha).child(idEvento);

        agendaReferencia.removeValue();
    }

    public void eliminarTodosLosEventoSegunUsuario(String usuario) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(usuario).child(FirebaseReferencias.REFERENCIA_EVENTO);

        agendaReferencia.removeValue();
    }

    public String validarHorarios(String fecha, String horaIni, String horaFin) {
        String resultado="Error";
        try {
            DateFormat format = new SimpleDateFormat("HH:mm");
            java.util.Date inicioEvento = format.parse(horaIni);
            java.util.Date finEvento = format.parse(horaFin);


            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos.json");

            try {

                JSONArray resultadoJSON = resultadoObtenido.getJSONArray("eventos");

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
                if (e.getMessage().equals("No value for eventos")){
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

    public String validarHorariosModificacion(String fecha, String horaIni, String horaFin, String idEvento) {
        String resultado="Error";
        try {
            DateFormat format = new SimpleDateFormat("HH:mm");
            java.util.Date inicioEvento = format.parse(horaIni);
            java.util.Date finEvento = format.parse(horaFin);


            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/eventos.json");

            try {
                JSONObject eventos = resultadoObtenido.getJSONObject(fecha);
                Iterator iterator = eventos.keys();
                JSONArray resultadoJSON = new JSONArray();

                while (iterator.hasNext()){
                    String key = (String) iterator.next();
                    if (key.equals(idEvento)==false) {
                        resultadoJSON.put(eventos.get(key));
                    }
                }
                if (resultadoJSON.length()>0) {
                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        java.util.Date inicioEventoRegistrado = format.parse(resultadoJSON.getJSONObject(i).getString("horaInicio"));
                        java.util.Date finEventoRegistrado = format.parse(resultadoJSON.getJSONObject(i).getString("horaFin"));

                        if ((inicioEvento.before(inicioEventoRegistrado) && finEvento.after(inicioEventoRegistrado)) || (inicioEvento.after(inicioEventoRegistrado) && inicioEvento.before(finEventoRegistrado)) || inicioEvento.toString().equals(inicioEventoRegistrado.toString()) || inicioEvento.toString().equals(finEventoRegistrado.toString()) || finEvento.toString().equals(inicioEventoRegistrado.toString()) || finEvento.toString().equals(finEventoRegistrado.toString())) {
                            throw new InstantiationException("Ya hay un evento registrado a esa hora.");
                        } else {
                            resultado = "";
                        }
                    }
                }else {
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

    public ModeloEvento obtenerDatosEventoPorId(String idEvento, String fecha) {
        ModeloEvento eventoBuscado = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/"+user.getUid()+"/eventos/"+fecha+"/"+ idEvento+".json");

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

    public ArrayList<String> obtenerRecordatorios(String fecha, String usuario) {
        ArrayList<String> arrayRecor = new ArrayList<>();
        int año = Integer.valueOf(fecha.split("-")[0]);
        int mes = Integer.valueOf(fecha.split("-")[1]);
        int dia = Integer.valueOf(fecha.split("-")[2]);
        Calendar fechaHoy = Calendar.getInstance();
        fechaHoy.set(año, mes, dia);

        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + usuario + "/eventos.json");

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

                if (TimeUnit.MILLISECONDS.toDays(diferencia) < 7 && TimeUnit.MILLISECONDS.toDays(diferencia)>0) {
                    JSONObject eventos = resultadoObtenido.getJSONObject(key);
                    Iterator iterator2 = eventos.keys();

                    while (iterator2.hasNext()) {
                        String key2 = (String) iterator2.next();
                        resultadoJSON.put(eventos.get(key2));
                    }

                    for (int i = 0; i < resultadoJSON.length(); i++) {
                        if (resultadoJSON.getJSONObject(i).getBoolean("recordatorio")==true) {
                            String resultado = key +" - "+ resultadoJSON.getJSONObject(i).getString("nombre") +" - " +resultadoJSON.getJSONObject(i).getString("horaInicio") +" - "+resultadoJSON.getJSONObject(i).getString("horaFin");
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

    public String eliminarEventosAntiguos(String fecha, String usuario) {
        int año = Integer.valueOf(fecha.split("-")[0]);
        int mes = Integer.valueOf(fecha.split("-")[1]);
        int dia = Integer.valueOf(fecha.split("-")[2]);
        Calendar fechaHoy = Calendar.getInstance();
        fechaHoy.set(año, mes, dia);

        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + usuario + "/eventos.json?&endAt="+fecha);

        try {
            JSONArray resultadoJSON = resultadoObtenido.getJSONArray("eventos");
            JSONArray resultadoJSON2 = resultadoJSON.getJSONArray(1);

            for (int i = 0; i <resultadoJSON.length() ; i++) {
                String fechaEvento= resultadoJSON.getString(i);
                String idEvento = resultadoJSON2.getString(i);
                eliminarEvento(idEvento,fechaEvento);
            }
            return "Eliminaciòn exitosa.";
        } catch (JSONException e) {
            return e.getMessage();
        }
    }

    public ArrayList<String> horasLibres(String fecha){
        ArrayList<String> horas = new ArrayList<>();

        for (int i = 10; i <19 ; i++) {
            String horaIni = i+":00";
            String horaFin = (i+1)+":00";
            String libre = validarHorarios(fecha,horaIni,horaFin);
            if (libre.equals("")){
               horas.add(horaIni+" - "+horaFin);
            }
        }

        return horas;
    }
}
