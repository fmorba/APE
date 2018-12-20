package modelos;

public class ModeloUsuario {
        String email;
        String contraseña;
        String nombre;
        String provincia;
        String localidad;
        String fechaNacimiento;
        String carrera;
        private String VARIABLE_VACIA = "Sin completar";

    public ModeloUsuario() {
    }

    public ModeloUsuario(String email, String contraseña) {
        this.email = email;
        this.contraseña = contraseña;
        this.nombre = VARIABLE_VACIA;
        this.provincia = VARIABLE_VACIA;
        this.localidad = VARIABLE_VACIA;
        this.fechaNacimiento = VARIABLE_VACIA;
        this.carrera = VARIABLE_VACIA;
    }

    public ModeloUsuario( String email, String contraseña, String nombre, String provincia, String localidad, String fechaNacimiento, String carrera) {
        this.email = email;
        this.contraseña = contraseña;
        this.nombre = nombre;
        this.provincia = provincia;
        this.localidad = localidad;
        this.fechaNacimiento = fechaNacimiento;
        this.carrera = carrera;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
}
