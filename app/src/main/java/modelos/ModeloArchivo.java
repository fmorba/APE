package modelos;

public class ModeloArchivo {
    String nombre;
    String tipo;
    String direccion;
    String clave;
    int idUsuario;

    public ModeloArchivo(String nombre, String tipo, String direccion, String clave, int idUsuario) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.direccion = direccion;
        this.clave = clave;
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getClave() {
        return clave;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
}
