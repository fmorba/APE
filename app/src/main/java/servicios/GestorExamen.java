package servicios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import modelos.ModeloEvento;
import modelos.ModeloExamen;

public class GestorExamen {
    ConexionBDOnline conexion = new ConexionBDOnline();
    ModeloExamen examen;
    JSONObject resultadoObtenido = new JSONObject();
    String respuestaObtenida;

    public ArrayList<String> ObtenerListadoExamenes(String idUsuario){
        ArrayList<String> array = new ArrayList<>();
        try {

            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaExamenAll.php?materia.idUsuario="+idUsuario);
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("examen");

            for (int i = 0; i < informacionRequerida.length() ; i++) {
                array.add(informacionRequerida.getJSONObject(i).getString("examen.idExamen") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("examen.fecha") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("materia.nombre") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("evento.horaInicio"));
            }

        }catch (JSONException e){
            array=null;
        }
        return array;
    }

    public ArrayList<String> ObtenerIdsExamenes(String idUsuario){
        ArrayList<String> array = new ArrayList<>();
        try {

            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaExamenAll.php?materia.idUsuario="+idUsuario);
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("examen");

            for (int i = 0; i < informacionRequerida.length() ; i++) {
                array.add(informacionRequerida.getJSONObject(i).getString("idExamen") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("evento") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("materia"));
            }

        }catch (JSONException e){
            array=null;
        }
        return array;
    }

    public String AgregarExamen(ModeloExamen examen) {
        String respuesta = "";

        String atributos = "fecha" + "-000-" + "resultado" + "-000-" + "evento" +"-000-" + "materia";
        String datos =  examen.getFecha() + "-000-" + examen.getResultado()  + "-000-" + examen.getIdEvento()  + "-000-" + examen.getIdMateria();

        respuesta = conexion.EnviarDatos("https://morprog.000webhostapp.com/insertEvento.php", atributos, datos);

        return respuesta;

    }

    public String ModificarExamen(String idExamen, String fecha, String resultado, String temas, int idEve, int idMate) {
        examen = new ModeloExamen(fecha, resultado,temas, idEve, idMate);
        String respuesta = "";

        String atributos = "fecha" + "-000-" + "resultado" + "-000-" + "evento" +"-000-" + "materia";
        String datos = examen.getFecha() + "-000-" + examen.getResultado() + "-000-" + examen.getIdEvento() + "-000-" + examen.getIdMateria();

        respuesta = conexion.EnviarDatos("https://morprog.000webhostapp.com/updateExamen.php", atributos, datos);

        return respuesta;
    }

    public String EliminarExamen(int id) {
        String respuesta = "";

        respuesta = conexion.EnviarDatos("https://morprog.000webhostapp.com/deleteEvento.php", "idExamen", id+"");

        return respuesta;
    }

    public ModeloExamen ObtenerDatosExamenPorId(String id) {
        ModeloExamen examenBuscado = null;
        resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaExamenID.php?idExamen=" + id);

        try {
            JSONArray resultadoJSON = resultadoObtenido.getJSONArray("examen");

            for (int i = 0; i < resultadoJSON.length(); i++) {
                String fecha = resultadoJSON.getJSONObject(i).getString("fecha");
                String resultado = resultadoJSON.getJSONObject(i).getString("resultado");
                String temas = resultadoJSON.getJSONObject(i).getString("temas");
                int idEvento = resultadoJSON.getJSONObject(i).getInt("evento");
                int idMateria = resultadoJSON.getJSONObject(i).getInt("materia");

                examenBuscado = new ModeloExamen(fecha,resultado,temas,idEvento,idMateria);
            }
        } catch (JSONException e) {
            examenBuscado = null;
        }
        return examenBuscado;
    }


}
