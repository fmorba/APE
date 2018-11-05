package clases;

public class Materia {
    private String nombre;
    private String dificultad;
    private String tipo;
    private Boolean estado; //true -> activa, false-> no activa

    public Materia(String nombre, String dificultad, String tipo, Boolean estado) {
        this.nombre = nombre;
        this.dificultad = dificultad;
        this.tipo = tipo;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDificultad() {
        return dificultad;
    }

    public String getTipo() {
        return tipo;
    }

    public Boolean getEstado() {
        return estado;
    }
}
