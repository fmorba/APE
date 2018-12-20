package modelos;

public class ModeloPlanificacion {
    int totalHoras;
    int resultado;
    String idExamen;

    public ModeloPlanificacion() {
    }

    public ModeloPlanificacion(String idExamen) {
        this.idExamen = idExamen;
    }

    public void setTotalHoras(int totalHoras) {
        this.totalHoras = totalHoras;
    }

    public void setIdExamen(String idExamen) {
        this.idExamen = idExamen;
    }

    public int getTotalHoras() {
        return totalHoras;
    }

    public String getIdExamen() {
        return idExamen;
    }

    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }
}
