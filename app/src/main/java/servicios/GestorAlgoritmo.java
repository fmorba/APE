package servicios;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
import java.util.Random;

import algoritmo_genetico.AlgoritmoGenetico;
import modelos.ModeloPlanificacion;

/**
 * Clase que se encarga de las actividades del algoritmo genético, así de como procesar la
 * información enviada y recibida por el mismo.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class GestorAlgoritmo {
    ConexionBDOnline conexion = new ConexionBDOnline();
    JSONObject resultadoObtenido = new JSONObject();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    AlgoritmoGenetico algoritmoGenetico;
    GestorExamen gestorExamen;
    int horasEstimadas;
    String tipoMateria;

    /**
     * Constructor
     *
     * @param horasEstimadas Cantidad de horas semanales dedicadas al estudio, parte del modelado.
     * @param tipo String representado el tipo de materia involucrada.
     */
    public GestorAlgoritmo(int horasEstimadas, String tipo) {
        this.horasEstimadas = horasEstimadas;
        this.tipoMateria = tipo;
        gestorExamen=new GestorExamen();
    }

    /**
     * Método que da inicio al proceso de optimización, enviándole los datos necesarios al
     * algoritmo genético y esperando su respuesta, para pasar el resultado a la clase que
     * lo requiera.
     *
     * @return Retorna la cantidad de horas semanales optimizadas por el algoritmo.
     */
    public int obtenerOptimizacion() {
        int resultado;
        ArrayList<String> poblacionGenerada = new ArrayList<>();
        poblacionGenerada = generarPoblacion();
        algoritmoGenetico = new AlgoritmoGenetico(poblacionGenerada,horasEstimadas);
        resultado = algoritmoGenetico.Opimizar();

        return resultado;
    }

    /**
     * Método cuya función es generar la población con la que se dará inicio al algoritmo genético,
     * dando prioridad a los datos del usuario, y de ser necesario pidiendo información a la base
     * de datos online.
     *
     * @return Retorna una colección que sera la población inicial del algoritmo genético.
     */
    public ArrayList<String> generarPoblacion() {
        ArrayList<String> poblacionGenerada = new ArrayList<>();
        ArrayList<String> poblacionOnline = new ArrayList<>();

        poblacionGenerada = obtenerMuestrasDelUsuario();
        int cantidadFaltante = algoritmoGenetico.POBLACIONTOTAL - poblacionGenerada.size();
        poblacionOnline = obtenerMuestrasOnline(cantidadFaltante);
        poblacionGenerada.addAll(poblacionOnline);

        return poblacionGenerada;
    }

    /**
     * Método que obtienen datos para completar una población inicial del algoritmo genético, en
     * base a las planificaciones finalizadas, y sus datos registrados.
     *
     * @return Retorna una colección que sera parte la población inicial.
     */
    private ArrayList<String> obtenerMuestrasDelUsuario() {
        ArrayList<String> poblacionObtenida = new ArrayList<>();

        try {

            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/planificaciones.json");

            JSONObject planificaciones = resultadoObtenido.getJSONObject(tipoMateria);
            Iterator iterator = planificaciones.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                resultadoJSON.put(planificaciones.get(key));
            }

            for (int i = 0; i < resultadoJSON.length(); i++) {
                if (resultadoJSON.getJSONObject(i).getInt("resultado") != 0) {
                    poblacionObtenida.add(resultadoJSON.getJSONObject(i).getString("horasSemanales") + " - " + resultadoJSON.getJSONObject(i).getString("resultado"));
                }
            }

        } catch (JSONException e) {

        }
        return poblacionObtenida;
    }

    /**
     * Método que obtienen datos para completar una población inicial del algoritmo genético, si
     * los datos del usuario no fueran suficientes, mediante la consulta a un registro online.
     *
     * @param faltantes Número de elementos faltantes para tener una población completa.
     * @return Retorna una colección que sera la población inicial.
     */
    private ArrayList<String> obtenerMuestrasOnline(int faltantes) {
        ArrayList<String> poblacionObtenida = new ArrayList<>();
        Random rdn = new Random();

        try {

            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/repositorio.json");

            JSONObject planificaciones = resultadoObtenido.getJSONObject(tipoMateria);
            Iterator iterator = planificaciones.keys();
            JSONArray resultadoJSON = new JSONArray();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                resultadoJSON.put(planificaciones.get(key));
            }

            for (int i = 0; i < faltantes; i++) {
                int posicion = rdn.nextInt(resultadoJSON.length());
                poblacionObtenida.add(resultadoJSON.getJSONObject(posicion).getString("horasSemanales") + " - " + resultadoJSON.getJSONObject(posicion).getString("nota"));

            }

        } catch (JSONException e) {
            poblacionObtenida = null;
        }
        return poblacionObtenida;
    }


}
