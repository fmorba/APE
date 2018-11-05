package clases;

import java.sql.Time;
import java.util.Date;

public class Evento {
    private String nombre;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String descripcion;
    private boolean recordatorio; //true->asignado flase->no asignado.

    public Evento(String nombre, String fecha, String horaInicio, String horaFin, String descripcion, boolean recordatorio) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.descripcion = descripcion;
        this.recordatorio = recordatorio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isRecordatorio() {
        return recordatorio;
    }
}
