package servicios;

import android.database.Cursor;

import com.morbidoni.proyecto.ape.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import database.EventoContrato;
import database.EventoModelo;
import modelos.ModeloEvento;

public class GestorEvento {
    ConexionBDOnline conexion = new ConexionBDOnline();
    ModeloEvento evento;
    JSONObject resultadoObtenido = new JSONObject();
    String respuestaObtenida;

    public ArrayList<ModeloEvento> ObtenerHorariosSegunFechas(String fecha, String dia, int usuario) {
        ArrayList<ModeloEvento> arrayEventos = new ArrayList<>();
        resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaEventoFecha.php?fecha="+fecha+"&dia="+dia+"&usuario="+usuario);

        try {
            JSONArray resultadoJSON = resultadoObtenido.getJSONArray("evento");

            for (int i = 0; i < resultadoJSON.length(); i++) {
                ModeloEvento modelo = new ModeloEvento(resultadoJSON.getJSONObject(i).getString("nombre"),fecha,
                        resultadoJSON.getJSONObject(i).getString("horaInicio"),
                        resultadoJSON.getJSONObject(i).getString("horaFin"),
                        resultadoJSON.getJSONObject(i).getString("descripcion"),
                        resultadoJSON.getJSONObject(i).getInt("recordatorio"),usuario);
                arrayEventos.add(modelo);
            }
        } catch (JSONException e) {
            arrayEventos = null;
        }

        return arrayEventos;
    }

    public ArrayList<String> ObtenerIdSegunFechas(String fecha, int usuario) {
        ArrayList<String> arrayID = new ArrayList<>();
        resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaEventoFecha.php?fecha=" + fecha+ "&usuario="+usuario);

        try {
            JSONArray resultadoJSON = resultadoObtenido.getJSONArray("evento");

            for (int i = 0; i < resultadoJSON.length(); i++) {
                arrayID.add(resultadoJSON.getJSONObject(i).getString("idEvento"));
            }
        } catch (JSONException e) {
            arrayID = null;
        }
        return arrayID;
    }

    public String AgregarEvento(String nombre, String fecha, String horaInicio, String horaFin, String diaSemana,String descripcion, boolean recordatorio, int idUsu) {
        evento = new ModeloEvento(nombre, fecha, horaInicio, horaFin, descripcion, recordatorio, idUsu);
        String respuesta = "";

        String atributos = "nombre" + "-000-" + "fecha" + "-000-" + "horaInicio" + "-000-" + "horaFin" +"-000-" + "dia" + "-000-" + "descripcion" + "-000-" + "recordatorio" + "-000-" + "usuario";
        String datos = evento.getNombreEvento() + "-000-" + evento.getFechaEvento() + "-000-" + evento.getHoraInicioEvento() + "-000-" + evento.getHoraFinEvento() + "-000-" + diaSemana + "-000-" + evento.getDescripcionEvento() + "-000-" + evento.getRecordatorioEvento() + "-000-" + evento.getIdUsuario();

        respuesta = conexion.EnviarDatos("https://morprog.000webhostapp.com/insertEvento.php", atributos, datos);

        return respuesta;

    }

    public String ModificarEveto(String idEvento, String nombre, String fecha, String horaInicio, String horaFin, String diaSemana,String descripcion, boolean recordatorio, int idUsu) {
        evento = new ModeloEvento(nombre, fecha, horaInicio, horaFin, descripcion, recordatorio, idUsu);
        String respuesta = "";

        String atributos = "idEvento"+"-000-"+"nombre" + "-000-" + "fecha" + "-000-" + "horaInicio" + "-000-" + "horaFin" + "-000-" + "dia" + "-000-" + "descripcion" + "-000-" + "recordatorio" + "-000-" + "usuario";
        String datos = idEvento+"-000-"+evento.getNombreEvento() + "-000-" + evento.getFechaEvento() + "-000-" + evento.getHoraInicioEvento() + "-000-" + evento.getHoraFinEvento() + "-000-" + diaSemana +  "-000-" + evento.getDescripcionEvento() + "-000-" + evento.getRecordatorioEvento() + "-000-" + evento.getIdUsuario();

        respuesta = conexion.EnviarDatos("https://morprog.000webhostapp.com/updateEvento.php", atributos, datos);

        return respuesta;
    }

    public String EliminarEvento(int id) {
        String respuesta = "";

        String atributos = "idEvento";
        String datos = id + "";

        respuesta = conexion.EnviarDatos("https://morprog.000webhostapp.com/deleteEvento.php", atributos, datos);

        return respuesta;
    }

    public String EliminarEventoSegunUsuario(String id) {
        String respuesta = "";

        String atributos = "usuario";
        String datos = id;

        respuesta = conexion.EnviarDatos("https://morprog.000webhostapp.com/deleteEventoByUsuario.php", atributos, datos);

        return respuesta;
    }

    public String ObtenerUltimoIDEvento(String idUsuario){
        String id;

        try {
            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaUltimaMateriaID.php?idUsuario="+idUsuario);
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("evento");
            id = informacionRequerida.getJSONObject(0).getString("idEvento");

        }catch (JSONException e){
            id="";
        }

        return id;
    }

    public String ValidarHorarios(String fecha, String horaIni, String horaFin, int usuario) {
        String resultado="Error";
        try {
            DateFormat format = new SimpleDateFormat("HH:mm");
            java.util.Date inicioEvento = format.parse(horaIni);
            java.util.Date finEvento = format.parse(horaFin);


            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaEventoFecha.php?fecha=" + fecha+"&usuario="+usuario);

            try {

                if (resultadoObtenido.getInt("estado")==2){
                    resultado="";
                }else{

                JSONArray resultadoJSON = resultadoObtenido.getJSONArray("evento");

                for (int i = 0; i < resultadoJSON.length(); i++) {

                    java.util.Date inicioEventoRegistrado = format.parse(resultadoJSON.getJSONObject(i).getString("horaInicio"));
                    java.util.Date finEventoRegistrado = format.parse(resultadoJSON.getJSONObject(i).getString("horaFin"));

                    if ((inicioEvento.before(inicioEventoRegistrado) && finEvento.after(inicioEventoRegistrado)) || (inicioEvento.after(inicioEventoRegistrado) && inicioEvento.before(finEventoRegistrado)) || inicioEvento.toString().equals(inicioEventoRegistrado.toString()) || inicioEvento.toString().equals(finEventoRegistrado.toString()) || finEvento.toString().equals(inicioEventoRegistrado.toString()) || finEvento.toString().equals(finEventoRegistrado.toString())) {
                        throw new InstantiationException("Ya hay un evento registrado a esa hora.");
                    } else {
                        resultado = "";
                    }

                }
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

    public String ValidarHorariosModificacion(String fecha, String horaIni, String horaFin, int usuario, String idEvento) {
        String resultado="Error";
        try {
            DateFormat format = new SimpleDateFormat("HH:mm");
            java.util.Date inicioEvento = format.parse(horaIni);
            java.util.Date finEvento = format.parse(horaFin);


            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaEventoFecha.php?fecha=" + fecha+"&usuario="+usuario);

            try {
                JSONArray resultadoJSON = resultadoObtenido.getJSONArray("evento");

                for (int i = 0; i < resultadoJSON.length(); i++) {
                    if (resultadoJSON.getJSONObject(i).getString("idEvento").equals(idEvento)==false) {
                        java.util.Date inicioEventoRegistrado = format.parse(resultadoJSON.getJSONObject(i).getString("horaInicio"));
                        java.util.Date finEventoRegistrado = format.parse(resultadoJSON.getJSONObject(i).getString("horaFin"));

                        if ((inicioEvento.before(inicioEventoRegistrado) && finEvento.after(inicioEventoRegistrado)) || (inicioEvento.after(inicioEventoRegistrado) && inicioEvento.before(finEventoRegistrado)) || inicioEvento.toString().equals(inicioEventoRegistrado.toString()) || inicioEvento.toString().equals(finEventoRegistrado.toString()) || finEvento.toString().equals(inicioEventoRegistrado.toString()) || finEvento.toString().equals(finEventoRegistrado.toString())) {
                            throw new InstantiationException("Ya hay un evento registrado a esa hora.");
                        } else {
                            resultado = "";
                        }
                    }else {resultado = "";}
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

    public ModeloEvento ObtenerDatosEventoPorId(String id) {
        ModeloEvento eventoBuscado = null;
        resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaEventoID.php?idEvento=" + id);

        try {
            JSONArray resultadoJSON = resultadoObtenido.getJSONArray("evento");

            for (int i = 0; i < resultadoJSON.length(); i++) {
                String nombre = resultadoJSON.getJSONObject(i).getString("nombre");
                String fecha = resultadoJSON.getJSONObject(i).getString("fecha");
                String horaInicio = resultadoJSON.getJSONObject(i).getString("horaInicio");
                String horaFin = resultadoJSON.getJSONObject(i).getString("horaFin");
                String descripcion = resultadoJSON.getJSONObject(i).getString("descripcion");
                int recordatorio = resultadoJSON.getJSONObject(i).getInt("recordatorio");
                boolean auxliliar;
                if (recordatorio == 1) {
                    auxliliar = true;
                } else {
                    auxliliar = false;
                }
                int idUsuario = resultadoJSON.getJSONObject(i).getInt("usuario");

                eventoBuscado = new ModeloEvento(nombre, fecha, horaInicio, horaFin, descripcion, auxliliar, idUsuario);
            }
        } catch (JSONException e) {
            eventoBuscado = null;
        }
        return eventoBuscado;
    }

    public ArrayList<ModeloEvento> ObtenerRecordatorios(String fecha, int usuario) {
        ArrayList<ModeloEvento> arrayRecor = new ArrayList<>();
        int año = Integer.valueOf(fecha.split("-")[0]);
        int mes = Integer.valueOf(fecha.split("-")[1]);
        int dia = Integer.valueOf(fecha.split("-")[2]);
        Calendar fechaHoy = Calendar.getInstance();
        fechaHoy.set(año, mes, dia);

        resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaEventoRecordatorio.php?recordatorio=" + 1+ "&usuario="+usuario);

        try {
            JSONArray resultadoJSON = resultadoObtenido.getJSONArray("evento");

            for (int i = 0; i < resultadoJSON.length(); i++) {

                String fechaEvento = resultadoJSON.getJSONObject(i).getString("fecha");
                int añoEvento = Integer.valueOf(fecha.split("-")[0]);
                int mesEvento = Integer.valueOf(fecha.split("-")[1]);
                int diaEvento = Integer.valueOf(fecha.split("-")[2]);
                Calendar dateEvento = Calendar.getInstance();
                dateEvento.set(año, mes, dia);
                long diferencia = dateEvento.getTimeInMillis() - fechaHoy.getTimeInMillis();
                if (TimeUnit.MILLISECONDS.toDays(diferencia) < 8) {
                    ModeloEvento modelo = new ModeloEvento(resultadoJSON.getJSONObject(i).getString("nombre"),
                            resultadoJSON.getJSONObject(i).getString("fecha"),
                            resultadoJSON.getJSONObject(i).getString("horaInicio"),
                            resultadoJSON.getJSONObject(i).getString("horaFin"),
                            resultadoJSON.getJSONObject(i).getString("descripcion"), 1, usuario);
                    arrayRecor.add(modelo);
                }
            }
        } catch (JSONException e) {
            arrayRecor = null;
        }

        return arrayRecor;
    }

    public String EliminarEventosAntiguos(String fecha, String usuario) {
        int año = Integer.valueOf(fecha.split("-")[0]);
        int mes = Integer.valueOf(fecha.split("-")[1]);
        int dia = Integer.valueOf(fecha.split("-")[2]);
        Calendar fechaHoy = Calendar.getInstance();
        fechaHoy.set(año, mes, dia);

        resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaEventoUsuario.php?usuario="+usuario);

        try {
            JSONArray resultadoJSON = resultadoObtenido.getJSONArray("evento");

            for (int i = 0; i <resultadoJSON.length() ; i++) {
                String fechaEvento = resultadoJSON.getJSONObject(i).getString("fecha");
                int añoEvento = Integer.valueOf(fechaEvento.split("-")[0]);
                int mesEvento = Integer.valueOf(fechaEvento.split("-")[1]);
                int diaEvento = Integer.valueOf(fechaEvento.split("-")[2]);
                Calendar dateEvento = Calendar.getInstance();
                dateEvento.set(añoEvento, mesEvento, diaEvento);
                if (fechaHoy.after(dateEvento)) {
                   int idAEliminar= resultadoJSON.getJSONObject(i).getInt("idEvento");
                   this.EliminarEvento(idAEliminar);
                }
            }
            return "Eliminaciòn exitosa.";
        } catch (JSONException e) {
            return e.getMessage();
        }
    }
}
