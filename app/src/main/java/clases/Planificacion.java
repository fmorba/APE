package clases;

public class Planificacion {
    private int cantidadHoras;
    private Examen examen;

    public Planificacion(int cantidadHoras, Examen examen) {
        this.cantidadHoras = cantidadHoras;
        this.examen = examen;
    }

    public int getCantidadHoras() {
        return cantidadHoras;
    }

    public Examen getExamen() {
        return examen;
    }
}
