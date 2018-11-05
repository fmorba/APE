package modelos;

public class ModeloExamen {
    String fecha;
    String resultado;
    String temas;
    int idEvento;
    int idMateria;

    public ModeloExamen(String fecha, String resultado, String temas, int idEvento, int idMateria) {
        this.fecha = fecha;
        this.resultado = resultado;
        this.temas = temas;
        this.idEvento = idEvento;
        this.idMateria = idMateria;
    }

    public String getFecha() {
        return fecha;
    }

    public String getResultado() {
        return resultado;
    }

    public String getTemas() {
        return temas;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public int getIdMateria() {
        return idMateria;
    }
}
