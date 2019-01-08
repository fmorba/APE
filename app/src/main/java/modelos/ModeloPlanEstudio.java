package modelos;

public class ModeloPlanEstudio {
    boolean estado;
    String fecha;
    String idEvento;
    String idPlanEstudio;

    public ModeloPlanEstudio() {
    }

    public ModeloPlanEstudio(String fecha, String idEvento) {
        this.fecha = fecha;
        this.estado = false;
        this.idEvento = idEvento;
    }

    public ModeloPlanEstudio(String fecha, boolean estado, String idEvento, String idPlanEstudio) {
        this.fecha = fecha;
        this.estado = estado;
        this.idEvento = idEvento;
        this.idPlanEstudio=idPlanEstudio;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdPlanEstudio() {
        return idPlanEstudio;
    }

    public void setIdPlanEstudio(String idPlanEstudio) {
        this.idPlanEstudio = idPlanEstudio;
    }
}

