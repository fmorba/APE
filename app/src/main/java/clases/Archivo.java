package clases;

public class Archivo {
    private String nombre;
    private String fecha;
    private String tipo;
    private String direccion;
    private String claves;

    public Archivo(String nombre, String fecha, String tipo, String direccion, String claves) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.tipo = tipo;
        this.direccion = direccion;
        this.claves = claves;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getClaves() {
        return claves;
    }
}
