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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class ConexionBDOnline {
    private WebServiceGet conexionGET;
    private WebServicePost conexionPOST;
    private JSONObject respuestaJSON = new JSONObject();
    private String respuestaALlamada;

    public JSONObject ObtenerResultados(String pagina) {

        try {
            conexionGET = new WebServiceGet();
            respuestaALlamada = conexionGET.execute(pagina).get();
        }catch (InterruptedException e){

        }catch (ExecutionException e){

        }
        return respuestaJSON;
    }

    public String EnviarDatos(String pagina, String titulos, String datos){

        try {
        conexionPOST = new WebServicePost();
        respuestaALlamada=conexionPOST.execute(pagina,titulos,datos).get();
        }catch (InterruptedException e){

        }catch (ExecutionException e){

        }

        return respuestaALlamada;
    }

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

                int codigoRespuesta = hilo.getResponseCode();
                StringBuilder stringResultado = new StringBuilder();

                if(codigoRespuesta== HttpURLConnection.HTTP_OK) {

                    InputStream in = new BufferedInputStream(hilo.getInputStream());
                    BufferedReader lector = new BufferedReader(new InputStreamReader(in));

                    String aux;
                    while ((aux = lector.readLine()) != null) {
                        stringResultado.append(aux);
                    }

                    String resultadoAdaptado = stringResultado.toString().replaceAll("[^\\x00-\\x7F]", "");
                    resultadoAdaptado = resultadoAdaptado.replaceAll("\\[\\[","[");
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

    private class WebServicePost extends AsyncTask<String, String, String>{

        @Override
        protected void onPostExecute(String s) {
           respuestaALlamada=s;
        }

        @Override
        protected String doInBackground(String... params) {
            String pagina = params[0];
            ArrayList<String> encabezados = new ArrayList<String>(Arrays.asList(params[1].split("-000-")));
            ArrayList<String> datos = new ArrayList<String>(Arrays.asList(params[2].split("-000-")));
            String devuelve = "Error";
            URL url = null;

            try {
                HttpURLConnection urlConn;

                DataOutputStream printout;
                DataInputStream input;
                url = new URL(pagina);
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);
                urlConn.setUseCaches(false);
                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.connect();

                JSONObject jsonParam = new JSONObject();
                for (int i = 0; i < encabezados.size() ; i++) {
                    jsonParam.put(encabezados.get(i),datos.get(i));
                }

                OutputStream os = urlConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();

                int cores = urlConn.getResponseCode();


                StringBuilder result = new StringBuilder();

                if (cores == HttpURLConnection.HTTP_OK) {

                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject respuestaJSON = new JSONObject(result.toString().replaceAll("[^\\x00-\\x7F]", ""));
                    String resultJSON = respuestaJSON.getString("estado");

                    if (resultJSON.equals("1")) {
                        devuelve = respuestaJSON.getString("mensaje");

                    } else if (resultJSON.equals("2")) {
                        devuelve = respuestaJSON.getString("mensaje");
                    }else {devuelve=resultJSON;}
                    urlConn.disconnect();

                    return devuelve;
                }


            } catch (MalformedURLException e){
                devuelve="url mala";

            } catch (IOException e){
                devuelve=e.getLocalizedMessage();

            } catch (JSONException e){
                devuelve=e.getLocalizedMessage();

            }
            return devuelve;
        }
    }

}
