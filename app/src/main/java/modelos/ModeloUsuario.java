package modelos;

public class ModeloUsuario {
        int id;
        String nombre;
        String contraseña;
        String provincia;
        String localidad;
        String fechaNacimiento;
        String carrera;

    public ModeloUsuario(int id, String nombre, String contraseña, String provincia, String localidad, String fechaNacimiento, String carrera) {
        this.id=id;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.provincia = provincia;
        this.localidad = localidad;
        this.fechaNacimiento = fechaNacimiento;
        this.carrera = carrera;
    }

    public ModeloUsuario(){
    }

    public int getId() {
        return id;
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
}
