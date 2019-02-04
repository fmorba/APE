package modelos;

import java.util.ArrayList;

/**
 * Clase que representa a una materia, y contiene la información necesaria para el registro de la
 * misma y para las actividades de la aplicación.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class ModeloMateria {
    String nombre;
    String tipo;
    String dificultad;
    String estado;
    private String idMateria;
    private ArrayList<ModeloHorarios> horarios;

    /**
     * Constructor vacio.
     */
    public ModeloMateria() {
    }

    /**
     * Constructor
     *
     * @param nombre String que representa el nombre de la materia.
     * @param tipo String que define el tipo de materia, los mismos están definidos por el sistema.
     * @param dificultad String que indica la dificultad asignada por el usuario.
     * @param estado String que indica el estado académico de la materia.
     */
    public ModeloMateria(String nombre, String tipo, String dificultad, String estado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.dificultad = dificultad;
        this.estado = estado;
        this.horarios= new ArrayList<>();
    }

    /**
     * Getter
     *
     * @return Regresa el nombre de la materia.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter
     *
     * @param nombre String que representa el nombre de la materia.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter
     *
     * @return Retorna el tipo de materia que fue asignado.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Setter
     *
     * @param tipo String que define el tipo de materia, los mismos están definidos por el sistema.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Getter
     *
     * @return Regresa al usuario la dificultad asignada a la materia.
     */
    public String getDificultad() {
        return dificultad;
    }

    /**
     * Setter
     *
     * @param dificultad String que indica la dificultad asignada por el usuario.
     */
    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    /**
     * Getter
     *
     * @return Retorna el estado actual de la materia.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Setter
     *
     * @param estado String que indica el estado académico de la materia.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Getter
     *
     * @return Retorna el identificador de la materia.
     */
    public String getIdMateria() {
        return idMateria;
    }

    /**
     * Setter
     *
     * @param idMateria String que representa el identificador de la materia.
     */
    public void setIdMateria(String idMateria) {
        this.idMateria = idMateria;
    }

    /**
     * Getter
     *
     * @return Retorna los horarios vinculados a la materia.
     */
    public ArrayList<ModeloHorarios> getHorarios() {
        return horarios;
    }

    /**
     * Setter
     *
     * @param horarios Conjunto de horarios pertenecientes a la materia.
     */
    public void setHorarios(ArrayList<ModeloHorarios> horarios) {
        this.horarios = horarios;
    }
}
