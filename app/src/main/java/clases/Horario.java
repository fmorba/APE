package clases;

public class Horario {
    private String Dia;
    private String horaInicio;
    private String horaFin;
    private Materia materia;

    public Horario(String dia, String horaInicio, String horaFin, Materia materia) {
        Dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.materia = materia;
    }

    public String getDia() {
        return Dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public Materia getMateria() {
        return materia;
    }
}
