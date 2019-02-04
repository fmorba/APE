package modelos;

/**
 * Clase que sirve como modelo para la información correspondiente a una planificación de horarios
 * de estudios, generada por el sistema y posiblemente modificada por el usuario, cuya información
 * será utilizada en el registro de la planificación, como datos útiles para el algoritmo genético.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class ModeloPlanificacion {
    int horasSemanales;
    float resultado;
    String fechaInicio;
    String idExamen;
    String tipoMateria;
    String idPlanificacion;

    /**
     * Constructor vacio.
     */
    public ModeloPlanificacion() {
    }

    /**
     * Constructor
     *
     * @param idExamen String que representa el identificador del examen vinculado.
     * @param fechaInicio String que representa la fecha de inicio de la planificación.
     */
    public ModeloPlanificacion(String idExamen, String fechaInicio) {
        this.idExamen = idExamen;
        this.fechaInicio = fechaInicio;
    }

    /**
     * Setter
     *
     * @param totalHoras Numero que representa el total de horas semanales dedicadas a la
     *                   planificación.
     */
    public void setHoras(int totalHoras) {
        this.horasSemanales = totalHoras;
    }

    /**
     * Setter
     *
     * @param idExamen String que representa el identificador del examen vinculado.
     */
    public void setIdExamen(String idExamen) {
        this.idExamen = idExamen;
    }

    /**
     * Getter
     *
     * @return Retorna el total de horas semanales.
     */
    public int getHoras() {
        return horasSemanales;
    }

    /**
     * Getter
     *
     * @return Retorna el identificador del examen vinculado a la planificación.
     */
    public String getIdExamen() {
        return idExamen;
    }

    /**
     * Getter
     *
     * @return Retorna el resultado del examen vinculado a la planificación.
     */
    public float getResultado() {
        return resultado;
    }

    /**
     * Setter
     *
     * @param resultado Valor del resultado final, correspondiente al examen relacionado con la
     *                  planificacion.
     */
    public void setResultado(float resultado) {
        this.resultado = resultado;
    }

    /**
     * Getter
     *
     * @return Retorna el tipo de materia a que el examen pertenece.
     */
    public String getTipoMateria() {
        return tipoMateria;
    }

    /**
     * Setter
     *
     * @param tipoMateria String que representa el tipo de materia del examen vinculado.
     */
    public void setTipoMateria(String tipoMateria) {
        this.tipoMateria = tipoMateria;
    }

    /**
     * Getter
     *
     * @return Retorna el identificador de la planificación.
     */
    public String getIdPlanificacion() {
        return idPlanificacion;
    }

    /**
     * Setter
     *
     * @param idPlanificacion String que representa el identificador de la planificación.
     */
    public void setIdPlanificacion(String idPlanificacion) {
        this.idPlanificacion = idPlanificacion;
    }

    /**
     * Getter
     *
     * @return Retorna la fecha del inicio de la planificación.
     */
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Setter
     *
     * @param fechaInicio String que representa la fecha de inicio de la planificación.
     */
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}
