package modelos;

/**
 * Clase que representa a un examen, y contiene los atributos relacionados con los mismos. La
 * información de esta clase determina el registro de los datos en el sistema.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class ModeloExamen {
    String fecha;
    String resultado;
    String temas="";
    String materia;
    String idMateria;
    private String idExamen;
    private String idEvento;

    /**
     * Cnstructor vacio.
     */
    public ModeloExamen() {
    }

    /**
     * Constructor
     *
     * @param fecha String indicando la fecha de realización del examen.
     * @param temas String conteniendo una descripción opcional de los temas del examen.
     * @param materia String que indica la materia al que pertenece el examen.
     * @param idMateria String que representa el identificador de la materia. Facilita la
     *                  transferencia de datos, dentro del sistema.
     */
    public ModeloExamen(String fecha, String temas, String materia, String idMateria) {
        this.fecha = fecha;
        this.temas = temas;
        this.materia=materia;
        this.idMateria = idMateria;
    }

    /**
     * Gettet
     *
     * @return Retorna la fecha del examen.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Setter
     *
     * @param fecha String indicando la fecha de realización del examen.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Getter
     *
     * @return Retorna el resultado del examen.
     */
    public String getResultado() {
        return resultado;
    }

    /**
     * Setter
     *
     * @param resultado Resultado el examen, dato que generalmente se ingresa bastante tiempo
     *                  después del registro del examen.
     */
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    /**
     * Getter
     *
     * @return Retorna los temas registrados para el examen.
     */
    public String getTemas() {
        return temas;
    }

    /**
     * Setter
     *
     * @param temas String conteniendo una descripción opcional de los temas del examen.
     */
    public void setTemas(String temas) {
        this.temas = temas;
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
     * @param idMateria String que representa el identificador de la materia. Facilita la
     *                  transferencia de datos, dentro del sistema.
     */
    public void setIdMateria(String idMateria) {
        this.idMateria = idMateria;
    }

    /**
     * Getter
     *
     * @return Retorna el identificador del examen.
     */
    public String getIdExamen() {
        return idExamen;
    }

    /**
     * Setter
     *
     * @param idExamen String que representa el identificador del examen.
     */
    public void setIdExamen(String idExamen) {
        this.idExamen = idExamen;
    }

    /**
     * Getter
     *
     * @return Retorna el String que indica la materia vinculada al examen.
     */
    public String getMateria() {
        return materia;
    }

    /**
     * Setter
     *
     * @param materia String que indica la materia al que pertenece el examen.
     */
    public void setMateria(String materia) {
        this.materia = materia;
    }

    /**
     * Getter
     *
     * @return Retorna el identificador del evento vinculado con el examen.
     */
    public String getIdEvento() {
        return idEvento;
    }

    /**
     * Setter
     *
     * @param idEvento String que sirve como identificador del evento vinculado con el examen.
     */
    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }
}
