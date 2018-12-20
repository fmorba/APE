package modelos;

public class ModeloPlanEstudio {
    boolean estado;

    String idEvento;

    public ModeloPlanEstudio() {
    }

    public ModeloPlanEstudio(String idEvento) {
        this.estado = false;
        this.idEvento = idEvento;
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
}

