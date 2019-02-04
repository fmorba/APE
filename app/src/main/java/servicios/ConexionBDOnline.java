package servicios;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Esta clase realiza la conexión con la base de datos online y su trabajo consiste con abrir la
 * página que se le es pasada y obtener los datos que se encuentren en ella, para luego enviarlos a
 * las otras clases.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class ConexionBDOnline {
    private WebServiceGet conexionGET;
    private JSONObject respuestaJSON = new JSONObject();
    private String respuestaALlamada;

    /**
     * Método que recibe una dirección web por parte de otra clase, y realiza la conexión para
     * obtener la información pedida, luego envía a la clase que realizo el pedido, la información
     * recolectada en forma de un objeto JSON.
     *
     * @param pagina String que corresponde a una dirección web.
     * @return JSONObject con la informacion encontrada.
     */
    public JSONObject ObtenerResultados(String pagina) {

        try {
            conexionGET = new WebServiceGet();
            respuestaALlamada = conexionGET.execute(pagina).get();
        }catch (InterruptedException e){

        }catch (ExecutionException e){

        }
        return respuestaJSON;
    }

    /**
     * Método que realiza las tareas de conexión y captura de datos, así como la formación del
     * objeto JSON.
     */
    private class WebServiceGet extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected String doInBackground(String... params) {
            String pagina = params[0];
            URL url = null;
            String salida ="";

            try{
                url=new URL(pagina);
                HttpsURLConnection hilo =(HttpsURLConnection) url.openConnection();
                hilo.setRequestProperty("User-Agent", "Mozilla/5.0"+ "(Linux; Android 1.5; es-ES)APE");
                hilo.setRequestProperty("Accept-Charset", "UTF-8");

                int codigoRespuesta = hilo.getResponseCode();
                StringBuilder stringResultado = new StringBuilder();

                if(codigoRespuesta== HttpURLConnection.HTTP_OK) {

                    InputStream in = new BufferedInputStream(hilo.getInputStream());
                    BufferedReader lector = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

                    String aux;
                    while ((aux = lector.readLine()) != null) {
                        stringResultado.append(aux);
                    }

                    String resultadoAdaptado = stringResultado.toString().replaceAll("\\[\\[","[");
                    resultadoAdaptado = resultadoAdaptado.replaceAll("]]","]");

                    respuestaJSON = new JSONObject(resultadoAdaptado);

                }

            }catch (MalformedURLException e){
                salida= e.getMessage();
            } catch (IOException e){
                salida= e.getMessage();
            } catch (JSONException e){
                salida= e.getMessage();
            }

            return salida;
        }
    }


}
