package modelos;

import java.util.ArrayList;

public class ModeloMateria {
    String nombre;
    String tipo;
    String dificultad;
    String estado;
    private String idMateria;
    private ArrayList<ModeloHorarios> horarios;

    public ModeloMateria() {
    }

    public ModeloMateria(String nombre, String tipo, String dificultad, String estado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.dificultad = dificultad;
        this.estado = estado;
        this.horarios= new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(String idMateria) {
        this.idMateria = idMateria;
    }

    public ArrayList<ModeloHorarios> getHorarios() {
        return horarios;
    }

    public void setHorarios(ArrayList<ModeloHorarios> horarios) {
        this.horarios = horarios;
    }
}
