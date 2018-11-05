package servicios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import modelos.ModeloHorarios;
import modelos.ModeloMateria;

public class GestorMateria {
    ConexionBDOnline conexion = new ConexionBDOnline();
    JSONObject resultadoObtenido = new JSONObject();
    String respuestaObtenida;

    public String ActualizarDatosMateria(String idMateria, ModeloMateria materia, ArrayList<ModeloHorarios> horarios){
        String respuesta ="";

        EliminarHorariosDeMateria(idMateria);

        for (ModeloHorarios horario: horarios) {
            ActualizarHorarios(horario, idMateria);
        }

        String atributos = "idMateria"+"-000-"+"nombre"+"-000-"+"tipo"+"-000-"+"dificultad"+"-000-"+"estado"+"-000-"+"usuario";
        String datos= idMateria+"-000-"+materia.getNombre()+"-000-"+materia.getTipo()+"-000-"+materia.getDificultad()+"-000-"+materia.getEstado()+"-000-"+materia.getIdUsuario();

        respuesta=conexion.EnviarDatos("https://morprog.000webhostapp.com/updateMateria.php",atributos,datos);

        return respuesta;
    }

    public String RegistrarMateria(ModeloMateria materia, ArrayList<ModeloHorarios> horarios, int idUsuario){
        String respuesta ="";

        String atributos = "nombre"+"-000-"+"tipo"+"-000-"+"dificultad"+"-000-"+"estado";
        String datos= materia.getNombre()+"-000-"+materia.getTipo()+"-000-"+materia.getDificultad()+"-000-"+materia.getEstado();

        respuesta=conexion.EnviarDatos("https://morprog.000webhostapp.com/insertMateria.php",atributos,datos);

        String idMateria = ObtenerUltimoIDMateria(idUsuario+"");

        for (ModeloHorarios horario: horarios) {
            ActualizarHorarios(horario, idMateria);
        }

        return respuesta;
    }

    public void ActualizarHorarios(ModeloHorarios horario, String idMateria){

        String atributos = "dia"+"-000-"+"horaInicio"+"-000-"+"horaFin"+"-000-"+"materia";
        String datos = horario.getDia()+"-000-"+horario.getHoraInicio()+"-000-"+horario.getHoraFin()+"-000-"+horario.getIdMateria();

        conexion.EnviarDatos("https://morprog.000webhostapp.com/insertHorario.php",atributos,datos);
    }

    public String EliminarMateria(int id) {
        String respuesta = "";

        respuesta = conexion.EnviarDatos("https://morprog.000webhostapp.com/deleteMateria.php","idMateria", id+"");

        return respuesta;
    }

    public String EliminarHorariosDeMateria(String id) {
        String respuesta = "";

        respuesta = conexion.EnviarDatos("https://morprog.000webhostapp.com/deleteHorarios.php","idMateria", id);

        return respuesta;
    }

    public String ObtenerUltimoIDMateria(String idUsuario){
        String id;

        try {
            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaUltimaMateriaID.php?idUsuario="+idUsuario);
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("materia");
            id = informacionRequerida.getJSONObject(0).getString("idMateria");

        }catch (JSONException e){
            id="";
        }

        return id;
    }

    public ModeloMateria ObtenerDatosMateria(String ID){
        ModeloMateria modelo;
        String nombre,tipo, dificultad, estado;
        int usuario;
        int id;
        try {

            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaMateriaID.php?idMateria=" + ID);
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("materia");
            id=Integer.valueOf(ID);
            nombre = informacionRequerida.getJSONObject(0).getString("nombre");
            tipo = informacionRequerida.getJSONObject(0).getString("tipo");
            dificultad = informacionRequerida.getJSONObject(0).getString("dificultad");
            estado = informacionRequerida.getJSONObject(0).getString("estado");
            usuario = informacionRequerida.getJSONObject(0).getInt("usuario");

            ArrayList<ModeloHorarios> horarios = ObtenerHorariosPorMateria(ID);

            modelo=new ModeloMateria(nombre,tipo,dificultad,estado,usuario);
            modelo.setHorarios(horarios);

        }catch (JSONException e){
            modelo=null;
        }
        return modelo;
    }

    public ArrayList<String> ObtenerListadoMaterias(int idUsuario){
        ArrayList<String> array = new ArrayList<>();
        try {

            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaMateriaAll.php?idUsuario="+idUsuario);
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("materia");

            for (int i = 0; i < informacionRequerida.length() ; i++) {
                array.add(informacionRequerida.getJSONObject(i).getString("idMateria") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("nombre"));
            }

        }catch (JSONException e){
            array=null;
        }
        return array;
    }

    public ArrayList<String> ObtenerListadoMateriasPorNombre(String nombre){
        ArrayList<String> array = new ArrayList<>();
        try {

            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaMateriaNombre.php?nombre="+nombre);
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("materia");

            for (int i = 0; i < informacionRequerida.length() ; i++) {
                array.add(informacionRequerida.getJSONObject(i).getString("idMateria") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("nombre") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("tipo") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("dificultad") + "-000-" +
                        informacionRequerida.getJSONObject(i).getString("estado"));
            }


        }catch (JSONException e){
            array=null;
        }
        return array;
    }

    public ArrayList<ModeloHorarios> ObtenerHorariosPorMateria(String ID){
        ArrayList<ModeloHorarios> array = new ArrayList<>();
        String dia,horaInicio, horaFin;
        int idMateria;
        try {

            resultadoObtenido = conexion.ObtenerResultados("https://morprog.000webhostapp.com/consultaHorarioMateria.php?idMateria=" + ID);
            JSONArray informacionRequerida = resultadoObtenido.getJSONArray("horario");

            for (int i = 0; i < informacionRequerida.length() ; i++) {
                dia = informacionRequerida.getJSONObject(i).getString("dia");
                horaInicio = informacionRequerida.getJSONObject(i).getString("horaInicio");
                horaFin = informacionRequerida.getJSONObject(i).getString("horaFin");
                idMateria = Integer.valueOf(ID);
                array.add(new ModeloHorarios(dia,horaInicio,horaFin,idMateria));
            }

        }catch (JSONException e){
            array=null;
        }
        return array;
    }

}
