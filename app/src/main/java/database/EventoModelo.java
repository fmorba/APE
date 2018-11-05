package database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

public class EventoModelo {
    private String id;
    private String nombre;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String descripcion;
    private int recordatorio;

    public EventoModelo(String nombre, String fecha, String horaInicio, String horaFin, String descripcion, boolean recordatorioBooleano) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.descripcion = descripcion;

        if (recordatorioBooleano==true) {
            this.recordatorio = 1; //Hay que recordar el evento.
        }else{
            this.recordatorio = 0; //No es necesario recordar el evento.
        }
    }

    public String getId() {
        return id;
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

    public int getRecordatorio() {
        return recordatorio;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(EventoContrato.EventoEntrada.ID,id);
        values.put(EventoContrato.EventoEntrada.NOMBRE,nombre);
        values.put(EventoContrato.EventoEntrada.FECHA,fecha);
        values.put(EventoContrato.EventoEntrada.HORARIO_INICIO,horaInicio);
        values.put(EventoContrato.EventoEntrada.HORARIO_FIN,horaFin);
        values.put(EventoContrato.EventoEntrada.DESCRIPCION,descripcion);
        values.put(EventoContrato.EventoEntrada.RECORDATORIO,recordatorio);
        return values;
    }

    public EventoModelo(Cursor cursor){
        this.id=cursor.getString(cursor.getColumnIndex(EventoContrato.EventoEntrada.ID));
        this.nombre=cursor.getString(cursor.getColumnIndex(EventoContrato.EventoEntrada.NOMBRE));
        this.fecha=cursor.getString(cursor.getColumnIndex(EventoContrato.EventoEntrada.FECHA));
        this.horaInicio=cursor.getString(cursor.getColumnIndex(EventoContrato.EventoEntrada.HORARIO_INICIO));
        this.horaFin=cursor.getString(cursor.getColumnIndex(EventoContrato.EventoEntrada.HORARIO_FIN));
        this.descripcion=cursor.getString(cursor.getColumnIndex(EventoContrato.EventoEntrada.DESCRIPCION));
        this.recordatorio=cursor.getInt(cursor.getColumnIndex(EventoContrato.EventoEntrada.RECORDATORIO));
    }
}
