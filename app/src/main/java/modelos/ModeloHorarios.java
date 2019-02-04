package modelos;

/**
 * Clase que representa los datos correspondientes a los horarios, generalmente usados al definir los tiempos de una materia.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class ModeloHorarios {
    String dia;
    String horaInicio;
    String horaFin;

    /**
     * Constructor vacio
     */
    public ModeloHorarios() {
    }

    /**
     * Constructor
     *
     * @param dia String que representa el día de cursado de la materia.
     * @param horaInicio String que represena la hora de inicio.
     * @param horaFin String que representa la hora de finalización.
     */
    public ModeloHorarios(String dia, String horaInicio, String horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    /**
     * Getter
     *
     * @return Retorna el String correspondiente al día.
     */
    public String getDia() {
        return dia;
    }

    /**
     * Setter
     *
     * @param dia String que representa el día de cursado de la materia.
     */
    public void setDia(String dia) {
        this.dia = dia;
    }

    /**
     * Getter
     *
     * @return Retorna la hora de inicio.
     */
    public String getHoraInicio() {
        return horaInicio;
    }

    /**
     * Setter
     *
     * @param horaInicio String que represena la hora de inicio.
     */
    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * Getter
     *
     * @return Regresa al usuario la hora de finalización.
     */
    public String getHoraFin() {
        return horaFin;
    }

    /**
     * Setter
     *
     * @param horaFin String que represena la hora de finalización.
     */
    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
}
