package modelos;

public class ModeloPlanificacion {
    int totalHoras;
    int idExamen;

    public ModeloPlanificacion(int totalHoras, int idExamen) {
        this.totalHoras = totalHoras;
        this.idExamen = idExamen;
    }

    public int getTotalHoras() {
        return totalHoras;
    }

    public int getIdExamen() {
        return idExamen;
    }
}
