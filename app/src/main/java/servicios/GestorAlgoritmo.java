package servicios;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import algoritmo_genetico.AlgoritmoGenetico;
import modelos.ModeloPlanificacion;

public class GestorAlgoritmo {
    ConexionBDOnline conexion = new ConexionBDOnline();
    JSONObject resultadoObtenido = new JSONObject();
    AlgoritmoGenetico algoritmoGenetico;
    int horasEstimadas;
    String tipoMateria;

    public GestorAlgoritmo(int horasEstimadas, String tipo) {
        this.horasEstimadas = horasEstimadas;
        this.tipoMateria = tipo;
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
                if (resultadoJSON.getJSONObject(i).getString("resultado") != null) {
                    poblacionObtenida.add(resultadoJSON.getJSONObject(i).getString("totalHoras") + " - " + resultadoJSON.getJSONObject(i).getString("resultado"));
                }
            }

        } catch (JSONException e) {
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
