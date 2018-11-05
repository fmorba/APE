package clases;

public class Examen {
    private float resultado;
    private Materia materia;
    private Evento evento;

    public Examen(float resultado, Materia materia, Evento evento) {
        this.resultado = resultado;
        this.materia = materia;
        this.evento = evento;
    }

    public float getResultado() {
        return resultado;
    }

    public Materia getMateria() {
        return materia;
    }

    public Evento getEvento() {
        return evento;
    }
}
