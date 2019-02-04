package modelos;

/**
 * Clase que sirve de representación de los objetos Eventos, conteniendo la información necesaria
 * para su correcta gestión y utilización.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class ModeloEvento {
    String nombre;
    String horaInicio;
    String horaFin;
    String descripcion;
    boolean recordatorio;
    String tipo;

    /**
     * Constructor vacio.
     */
    public ModeloEvento() {
    }

    /**
     * Constructor
     *
     * @param nombre String que representa el nombre asignado al evento.
     * @param horaInicio String que indica la hora de inicio del evento.
     * @param horaFin String que indica la hora de finalización del evento.
     * @param descripcion String que contiene una descripción de lo que implica dicho evento.
     * @param recordatorio boolean true- el evento debe ser recordado,  false  - el evento no
     *                     necesita ser recordado.
     */
    public ModeloEvento(String nombre, String horaInicio, String horaFin, String descripcion, boolean recordatorio) {
        this.nombre = nombre;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.descripcion = descripcion;
        this.recordatorio = recordatorio;
    }

    /**
     * Getter
     *
     * @return Retorna el registro del nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter
     *
     * @param nombre String que representa el nombre asignado al evento.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter
     *
     * @return Retorna la hora de inicio.
     */
    public String getHoraInicio() {
        return horaInicio;
    }

    /**
     * Setter
     *
     * @param horaInicio String que indica la hora de inicio del evento.
     */
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * Getter
     *
     * @return Retorna la hora de finalización.
     */
    public String getHoraFin() {
        return horaFin;
    }

    /**
     * Setter
     *
     * @param horaFin String que indica la hora de finalización del evento.
     */
    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * Getter
     *
     * @return Retorna la descripción del archivo.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Setter
     *
     * @param descripcion String que contiene una descripción de lo que implica dicho evento.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Getter
     *
     * @return Indica al usuario si el evento debe ser recordado o no.
     */
    public boolean isRecordatorio() {
        return recordatorio;
    }

    /**
     * Setter
     *
     * @param recordatorio true- necesita ser recordado,  false  - no necesita ser recordado.
     */
    public void setRecordatorio(boolean recordatorio) {
        this.recordatorio = recordatorio;
    }

    /**
     * Getter
     *
     * @return Retorna el tipo de evento.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Setter
     *
     * @param tipo String que indica el tipo de evento, es definido por el sistema.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
