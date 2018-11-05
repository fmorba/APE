package modelos;

public class ModeloPlanEstudio {
    boolean estado;
    int idPlanificacion;
    int idEvento;

    public ModeloPlanEstudio(boolean estado, int idPlanificacion, int idEvento) {
        this.estado = estado;
        this.idPlanificacion = idPlanificacion;
        this.idEvento = idEvento;
    }

    public boolean isEstado() {
        return estado;
    }

    public int getIdPlanificacion() {
        return idPlanificacion;
    }

    public int getIdEvento() {
        return idEvento;
    }
}
