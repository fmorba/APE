package modelos;

public class ModeloHorarios {
    String dia;
    String horaInicio;
    String horaFin;
    int idMateria;

    public ModeloHorarios(String dia, String horaInicio, String horaFin, int idMateria) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.idMateria = idMateria;
    }

    public ModeloHorarios(String dia, String horaInicio, String horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getDia() {
        return dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public int getIdMateria() {
        return idMateria;
    }
}
