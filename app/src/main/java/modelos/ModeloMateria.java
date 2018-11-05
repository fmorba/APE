package modelos;

import java.util.ArrayList;

public class ModeloMateria {
    String nombre;
    String tipo;
    String dificultad;
    String estado;
    int idUsuario;
    ArrayList<ModeloHorarios> horarios;

    public ModeloMateria(String nombre, String tipo, String dificultad, String estado, int idUsuario) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.dificultad = dificultad;
        this.estado = estado;
        this.idUsuario = idUsuario;
    }

    public ArrayList<ModeloHorarios> getHorarios() {
        return horarios;
    }

    public void setHorarios(ArrayList<ModeloHorarios> horarios) {
        this.horarios = horarios;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDificultad() {
        return dificultad;
    }

    public String getEstado() {
        return estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
}
