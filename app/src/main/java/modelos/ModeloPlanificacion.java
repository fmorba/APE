package modelos;

public class ModeloPlanificacion {
    int totalHoras;
    float resultado;
    String fechaInicio;
    String idExamen;
    String tipoMateria;
    String idPlanificacion;

    public ModeloPlanificacion() {
    }

    public ModeloPlanificacion(String idExamen, String fechaInicio) {
        this.idExamen = idExamen;
        this.fechaInicio = fechaInicio;
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

    public float getResultado() {
        return resultado;
    }

    public void setResultado(float resultado) {
        this.resultado = resultado;
    }

    public String getTipoMateria() {
        return tipoMateria;
    }

    public void setTipoMateria(String tipoMateria) {
        this.tipoMateria = tipoMateria;
    }

    public String getIdPlanificacion() {
        return idPlanificacion;
    }

    public void setIdPlanificacion(String idPlanificacion) {
        this.idPlanificacion = idPlanificacion;
    }
}
