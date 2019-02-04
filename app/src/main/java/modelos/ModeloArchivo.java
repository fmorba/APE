package modelos;

/**
 * Clase que representa a los archivos y la información útil o necesaria que deberían manejar, y
 * con la cual se realiza el registro de los mismos.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class ModeloArchivo {
    String nombre;
    String tipo;
    String fechaCreacion;
    String direccion;
    String clave;
    String idArchivo;

    /**
     * Constructor
     *
     * @param nombre String que representa al nombre asignado al archivo.
     * @param tipo String que indica el tipo de archivo, es designado por el sistema.
     * @param fechaCreacion String que indica la fecha de creación.
     * @param direccion String que referencia la dirección de almacenamiento físico del archivo.
     * @param clave String que reúne las distintas palabras claves asignadas al archivo.
     */
    public ModeloArchivo(String nombre, String tipo, String fechaCreacion, String direccion, String clave) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.fechaCreacion = fechaCreacion;
        this.direccion = direccion;
        this.clave = clave;
    }

    /**
     * Getter
     *
     * @return Regresa el String nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter
     *
     * @param nombre String que representa al nombre asignado al archivo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter
     *
     * @return Retorna el String tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Setter
     *
     * @param tipo String que indica el tipo de archivo, es designado por el sistema.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Getter
     *
     * @return Retorna el String que representa la fecha de creación.
     */
    public String getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Setter
     *
     * @param fechaCreacion String que indica la fecha de creación.
     */
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Getter
     *
     * @return Retorna el String con la dirección de almacenamiento.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Setter
     *
     * @param direccion String que referencia la dirección de almacenamiento físico del archivo.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Getter.
     *
     * @return Regresa al usuario las palabras claves registradas.
     */
    public String getClave() {
        return clave;
    }

    /**
     * Setter
     *
     * @param clave String que reúne las distintas palabras claves asignadas al archivo.
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * Getter
     *
     * @return Retorna al usuario el identificador del archivo.
     */
    public String getIdArchivo() {
        return idArchivo;
    }

    /**
     * Setter
     *
     * @param idArchivo String generado por el sistema, que sirve como identificador del archivo.
     */
    public void setIdArchivo(String idArchivo) {
        this.idArchivo = idArchivo;
    }
}
