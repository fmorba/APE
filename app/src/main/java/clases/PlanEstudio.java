package clases;

public class PlanEstudio {
    private boolean estado; //true->completado, flase->no completado.
    private Planificacion planificacion;
    private Evento evento;

    public PlanEstudio(boolean estado, Planificacion planificacion, Evento evento) {
        this.estado = estado;
        this.planificacion = planificacion;
        this.evento = evento;
    }

    public boolean isEstado() {
        return estado;
    }

    public Planificacion getPlanificacion() {
        return planificacion;
    }

    public Evento getEvento() {
        return evento;
    }
}

