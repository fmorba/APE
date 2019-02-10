package servicios;

import android.view.Display;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import modelos.ModeloArchivo;

/**
 * Clase que se encarga de las actividades de gestión de la información relacionada a los archivos
 * del usuario, principalmente el registro de datos, y su posterior recuperación.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class GestorArchivos {
    ConexionBDOnline conexion = new ConexionBDOnline();
    JSONObject resultadoObtenido = new JSONObject();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    /**
     * Método que consulta la base de datos, para obtener un listado con todos los registros de
     * archivos en la misma.
     *
     * @return Colección de archivos encontrados.
     */
    public ArrayList<ModeloArchivo> obtenerListadoArchivos() {
        ArrayList<ModeloArchivo> arrayModelos = new ArrayList<>();
        ArrayList<String> listadoID = new ArrayList<>();

        try {
            resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/archivos.json");

            Iterator iterator = resultadoObtenido.keys();

            while (iterator.hasNext()) {
                listadoID.clear();
                JSONArray resultadoJSON = new JSONArray();
                String tipo = (String) iterator.next();
                JSONObject archivos = resultadoObtenido.getJSONObject(tipo);
                Iterator iterator2 = archivos.keys();

                while (iterator2.hasNext()) {
                    String id = (String) iterator2.next();
                    resultadoJSON.put(archivos.get(id));
                    listadoID.add(id);
                }

                for (int i = 0; i < archivos.length(); i++) {
                    ModeloArchivo modelo = new ModeloArchivo(resultadoJSON.getJSONObject(i).getString("nombre"),
                            resultadoJSON.getJSONObject(i).getString("tipo"),
                            resultadoJSON.getJSONObject(i).getString("fechaCreacion"),
                            resultadoJSON.getJSONObject(i).getString("direccion"),
                            resultadoJSON.getJSONObject(i).getString("clave"));
                    modelo.setIdArchivo(listadoID.get(i));
                    arrayModelos.add(modelo);
                }
            }

        } catch (JSONException e) {
            arrayModelos = null;
        }
        return arrayModelos;
    }

    /**
     * Método que consulta la base de datos, para obtener un listado con todos los registros de
     * archivos que pertenezcan a un tipo de materia especifico.
     *
     * @param tipo String ue representa el tipo de archivo buscado.
     * @return Colección de archivos encontrados.
     */
    public ArrayList<ModeloArchivo> obtenerListadoArchivosPorTipos(String tipo) {
        ArrayList<ModeloArchivo> arrayModelos = new ArrayList<>();
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/archivos/" + tipo + ".json");

        try {
            Iterator iterator = resultadoObtenido.keys();
            JSONObject resultadoJSON = new JSONObject();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                resultadoJSON = resultadoObtenido.getJSONObject(key);

                ModeloArchivo modelo = new ModeloArchivo(resultadoJSON.getString("nombre"),
                        resultadoJSON.getString("tipo"),
                        resultadoJSON.getString("fechaCreacion"),
                        resultadoJSON.getString("direccion"),
                        resultadoJSON.getString("clave"));
                modelo.setIdArchivo(key);
                arrayModelos.add(modelo);

            }
            if (arrayModelos.isEmpty()){arrayModelos=null;}

        } catch (JSONException e) {
            arrayModelos = null;
        }
        return arrayModelos;
    }

    /**
     * Método que permite el registro de un archivo en la base de datos.
     *
     * @param archivo Archivo a registrar.
     */
    public void agregarArchivo(ModeloArchivo archivo) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_ARCHIVO).child(archivo.getTipo()).child(archivo.getIdArchivo());

        agendaReferencia.setValue(archivo);
    }

    /**
     * Método que permite el borrado de un archivo, de la base de datos.
     *
     * @param idArchivo String que representa el identificador del archivo.
     * @param tipo String que representa el tipo del archivo.
     */
    public void eliminarArchivo(String idArchivo, String tipo) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_ARCHIVO).child(tipo).child(idArchivo);

        agendaReferencia.removeValue();
    }

    /**
     * Método que permite la actualización de un archivo en la base de datos.
     *
     * @param archivo Archivo a modificar.
     */
    public void modificarArchivo(ModeloArchivo archivo) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference agendaReferencia = database.getReference(FirebaseReferencias.REFERENCIA_USUARIO).child(user.getUid()).child(FirebaseReferencias.REFERENCIA_ARCHIVO).child(archivo.getTipo()).child(archivo.getIdArchivo());

        agendaReferencia.setValue(archivo);
    }

    /**
     * Método que permite la recuperación de la información perteneciente a un archivo especifico.
     *
     * @param idArchivo String que representa el identificador del archivo.
     * @param tipo String que representa el tipo del archivo.
     * @return Retorna un ModeloArchivo con la información requerida.
     */
    public ModeloArchivo obtenerDatosArchivo(String idArchivo, String tipo) {
        resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/archivos/" + tipo + "/" + idArchivo + ".json");
        ModeloArchivo archivo;

        try {
            archivo = new ModeloArchivo(resultadoObtenido.getString("nombre"),
                    tipo, resultadoObtenido.getString("fechaCreacion"),
                    resultadoObtenido.getString("direccion"),
                    resultadoObtenido.getString("clave"));
            archivo.setIdArchivo(idArchivo);

        } catch (JSONException e) {
            archivo = null;
        }
        return archivo;

    }

    /**
     * Método que obtiene los registros de archivos cuyas palabras claves se similar a las palabras
     * ingresadas por el usuario.
     *
     * @param claves String que corresponde a palabras claves ingresadas por el usuario.
     * @return Retorna un listado de archivos.
     */
    public ArrayList<String> obtenerListadoArchivosPorClaves(ArrayList<String> claves) {
        ArrayList<String> array = new ArrayList<>();
        if (claves!=null) {
            try {
                resultadoObtenido = conexion.ObtenerResultados("https://agendayplanificador.firebaseio.com/usuarios/" + user.getUid() + "/archivos.json");

                Iterator iterator = resultadoObtenido.keys();

                while (iterator.hasNext()) {
                    JSONArray resultadoJSON = new JSONArray();
                    String tipo = (String) iterator.next();
                    JSONObject archivos = resultadoObtenido.getJSONObject(tipo);
                    Iterator iterator2 = archivos.keys();

                    while (iterator2.hasNext()) {
                        String id = (String) iterator2.next();
                        resultadoJSON.put(archivos.get(id));
                    }

                    for (int i = 0; i < archivos.length(); i++) {
                        boolean claveEncontrada = false;
                        for (String s : claves) {
                            String aux = resultadoJSON.getJSONObject(i).getString("clave");
                            if (claveEncontrada == false && s.toLowerCase().contains(aux.toLowerCase())){
                                claveEncontrada = true;
                                array.add(resultadoJSON.getJSONObject(i).getString("tipo") + ": " + resultadoJSON.getJSONObject(i).getString("nombre"));
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                array = null;
            }
        }else{
            array = null;
        }
        return array;
    }
}
