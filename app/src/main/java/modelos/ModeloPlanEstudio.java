package modelos;

/**
 * Clase que representa los planes de estudio que forman una planificación en particular, están muy
 * vinculados con los modelos de eventos y de planificaciones.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class ModeloPlanEstudio {
    boolean estado;
    String fecha;
    String idEvento;
    String idPlanEstudio;

    /**
     * Constructor vacio.
     */
    public ModeloPlanEstudio() {
    }

    /**
     * Contructor
     *
     * @param fecha String que representa la fecha asignada al plan de estudio.
     * @param idEvento String que representa el identificador del evento vinculado.
     */
    public ModeloPlanEstudio(String fecha, String idEvento) {
        this.fecha = fecha;
        this.estado = false;
        this.idEvento = idEvento;
    }

    /**
     * Constructor
     *
     * @param fecha String que representa la fecha asignada al plan de estudio.
     * @param estado boolean true - plan completado, false - plan no completado.
     * @param idEvento String que representa el identificador del evento vinculado.
     * @param idPlanEstudio String que representa al identificador del plan.
     */
    public ModeloPlanEstudio(String fecha, boolean estado, String idEvento, String idPlanEstudio) {
        this.fecha = fecha;
        this.estado = estado;
        this.idEvento = idEvento;
        this.idPlanEstudio=idPlanEstudio;
    }

    /**
     * Getter
     *
     * @return Retorna si el plan fue completado o no.
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * Setter
     *
     * @param estado Representa si el plan en particular fue completado por el usuario o si todavia
     *               no lo realizo.
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    /**
     * Getter
     *
     * @return Retorna el identificador del evento vinculado.
     */
    public String getIdEvento() {
        return idEvento;
    }

    /**
     * Setter
     *
     * @param idEvento String que representa el identificador del evento vinculado.
     */
    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    /**
     * Getter
     *
     * @return Retorna la fecha asignada al plan.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Setter
     *
     * @param fecha String que representa la fecha asignada al plan de estudio.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Getter
     *
     * @return Retorna el identificador individual del plan.
     */
    public String getIdPlanEstudio() {
        return idPlanEstudio;
    }

    /**
     * Setter
     *
     * @param idPlanEstudio String que representa el identificador del plan.
     */
    public void setIdPlanEstudio(String idPlanEstudio) {
        this.idPlanEstudio = idPlanEstudio;
    }
}

