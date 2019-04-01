package modelos;

/**
 * Clase que reúne la información que representa al usuario de la aplicación, y que sirve para el
 * registro e identificación del mismo dentro del sistema.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class ModeloUsuario {
        String email;
        String nombre;
        String provincia;
        String localidad;
        String fechaNacimiento;
        String carrera;
        private String VARIABLE_VACIA = "Sin completar";

    /**
     * Constructor vacio.
      */
    public ModeloUsuario() {
    }

    /**
     * Constructor
     *
     * @param email String que representa el email del usuario, sirve como identificador.
     *
     */
    public ModeloUsuario(String email) {
        this.email = email;
        this.nombre = VARIABLE_VACIA;
        this.provincia = VARIABLE_VACIA;
        this.localidad = VARIABLE_VACIA;
        this.fechaNacimiento = VARIABLE_VACIA;
        this.carrera = VARIABLE_VACIA;
    }

    /**
     * Constructor
     *
     * @param email String que representa el email del usuario, sirve como identificador.
     *
     * @param nombre String que representa el nombre del usuario.
     * @param provincia String que representa la provincia de origen del usuario.
     * @param localidad String que representa la localidad de origen del usuario.
     * @param fechaNacimiento String que representa la fecha de nacimiento del usuario.
     * @param carrera String que indica la carrera académica que cursa el usuario.
     */
    public ModeloUsuario( String email, String nombre, String provincia, String localidad, String fechaNacimiento, String carrera) {
        this.email = email;
        this.nombre = nombre;
        this.provincia = provincia;
        this.localidad = localidad;
        this.fechaNacimiento = fechaNacimiento;
        this.carrera = carrera;
    }

    /**
     * Getter
     *
     * @return Retorna el email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter
     *
     * @return Retorna el nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Getter
     *
     * @return Retorna la provincia.
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Getter
     *
     * @return Retorna la localidad registrada del usuario.
     */
    public String getLocalidad() {
        return localidad;
    }

    /**
     * Getter
     *
     * @return Retorna la fecha de nacimiento registrada.
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Getter
     *
     * @return Retorna la carrera académica del usuario.
     */
    public String getCarrera() {
        return carrera;
    }

    /**
     * Setter
     *
     * @param email String que representa el email del usuario, sirve como identificador.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter
     *
     * @param nombre String que representa el nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Setter
     *
     * @param provincia String que representa la provincia de origen del usuario.
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * Setter
     *
     * @param localidad String que representa la localidad de origen del usuario.
     */
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    /**
     * Setter
     *
     * @param fechaNacimiento String que representa la fecha de nacimiento del usuario.
     */
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Setter
     *
     * @param carrera String que indica la carrera academica que cursa el usuario.
     */
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
}
