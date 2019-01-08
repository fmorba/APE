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

public class GestorAlgoritmo {
    ConexionBDOnline conexion = new ConexionBDOnline();
    JSONObject resultadoObtenido = new JSONObject();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    AlgoritmoGenetico algoritmoGenetico;
    GestorExamen gestorExamen;
    int horasEstimadas;
    String tipoMateria;

    public GestorAlgoritmo(int horasEstimadas, String tipo) {
        this.horasEstimadas = horasEstimadas;
        this.tipoMateria = tipo;
        gestorExamen=new GestorExamen();
    }

    public int obtenerOptimizacion() {
        int resultado;
        ArrayList<String> poblacionGenerada = new ArrayList<>();
        poblacionGenerada = generarPoblacion();
        algoritmoGenetico = new AlgoritmoGenetico(poblacionGenerada,horasEstimadas);
        resultado = algoritmoGenetico.Opimizar();

        return resultado;
    }

    public ArrayList<String> generarPoblacion() {
        ArrayList<String> poblacionGenerada = new ArrayList<>();
        ArrayList<String> poblacionOnline = new ArrayList<>();

        poblacionGenerada = obtenerMuestrasDelUsuario();
        int cantidadFaltante = algoritmoGenetico.POBLACIONTOTAL -poblacionGenerada.size();
        poblacionOnline = obtenerMuestrasOnline(cantidadFaltante);
        poblacionGenerada.addAll(poblacionOnline);

        return poblacionGenerada;
    }

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
                    DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    Date fechaInicial = formato.parse(resultadoJSON.getJSONObject(i).getString("fechaInicio"));
                    String fechaExamen = gestorExamen.obtenerDatosExamenPorId(resultadoJSON.getJSONObject(i).getString("idExamen")).getFecha();
                    Date fechaFinal = formato.parse(fechaExamen);
                    Calendar c = Calendar.getInstance();
                    c.setTime(fechaInicial);

                    int dias=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000); //milisegundos en un dia;
                    int cantidadHoras = resultadoJSON.getJSONObject(i).getInt("totalHoras");
                    int horasSemanales = cantidadHoras/(dias/7);

                    poblacionObtenida.add(horasSemanales + " - " + resultadoJSON.getJSONObject(i).getString("resultado"));
                }
            }

        } catch (JSONException e) {

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return poblacionObtenida;
    }

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
