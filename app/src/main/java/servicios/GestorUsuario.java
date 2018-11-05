package servicios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import modelos.ModeloUsuario;

public class GestorUsuario {
    ConexionBDOnline conexion = new ConexionBDOnline();
    ModeloUsuario modelo;
    ArrayList<String> array = new ArrayList<String>();
    JSONObject resultadoObtenido = new JSONObject();

    public boolean ComprobarContraseña(String usuario, String clave){
        boolean auxiliar = false;

        try {

            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaUsuarioNombre.php?nombre=" + usuario);
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("usuario");

            if (informacionRequerida.getJSONObject(0).getString("clave").equals(clave)){
                auxiliar=true;
            }

        }catch (JSONException e){
                auxiliar=false;
        }
        return auxiliar;
    }

    public int ObtenerIDUsuario(String usuario){
        int id=0;

        try {

            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaUsuarioNombre.php?nombre=" + usuario);
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("usuario");
            id = informacionRequerida.getJSONObject(0).getInt("idUsuario");

        }catch (JSONException e){
            id=0;
        }

        return id;
    }

    public ModeloUsuario ObtenerDatosUsuario(String ID){
        String nombre,clave, provincia, localidad, fechaNacimiento, carrera;
        int id;
        try {

            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaUsuarioID.php?idUsuario=" + ID);
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("usuario");
            id=Integer.valueOf(ID);
            nombre = informacionRequerida.getJSONObject(0).getString("nombre");
            clave = informacionRequerida.getJSONObject(0).getString("clave");
            provincia = informacionRequerida.getJSONObject(0).getString("provincia");
            localidad = informacionRequerida.getJSONObject(0).getString("localidad");
            fechaNacimiento = informacionRequerida.getJSONObject(0).getString("fechaNacimiento");
            carrera = informacionRequerida.getJSONObject(0).getString("carrera");

            modelo=new ModeloUsuario(id,nombre,clave,provincia,localidad,fechaNacimiento,carrera);

        }catch (JSONException e){
            modelo=null;
        }
        return modelo;
    }

    public ArrayList<String> ObtenerListadoUsuarios(){
        array.clear();
        try {

            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaUsuarioAll.php?");
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("usuario");

            for (int i = 0; i < informacionRequerida.length() ; i++) {
                array.add(informacionRequerida.getJSONObject(i).getString("idUsuario") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("nombre") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("clave") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("provincia") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("localidad") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("fechaNacimiento") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("carrera"));
            }


        }catch (JSONException e){
            array=null;
        }
        return array;
    }

    public String ActualizarDatosUsuario(ModeloUsuario usuario){
        String respuesta ="";

        String atributos = "idUsuario"+"-000-"+"nombre"+"-000-"+"clave"+"-000-"+"provincia"+"-000-"+"localidad"+"-000-"+"fechaNacimiento"+"-000-"+"carrera";
        String datos = usuario.getId()+"-000-"+usuario.getNombre()+"-000-"+usuario.getContraseña()+"-000-"+usuario.getProvincia()+"-000-"+usuario.getLocalidad()+"-000-"+usuario.getFechaNacimiento()+"-000-"+usuario.getCarrera();

        respuesta=conexion.EnviarDatos("https://morprog.000webhostapp.com/updateUsuario.php",atributos,datos);

        return respuesta;
    }

    public String RegistarUsuario(String nombreUsuario, String claveUsuario){
        String respuesta ="";

        String atributos = "nombre"+"-000-"+"clave";
        String datos = nombreUsuario+"-000-"+claveUsuario;

        respuesta=conexion.EnviarDatos("https://morprog.000webhostapp.com/insertUsuario.php",atributos,datos);

        return respuesta;
    }



}
