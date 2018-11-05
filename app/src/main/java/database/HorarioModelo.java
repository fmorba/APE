package database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

public class HorarioModelo {
    private String id;
    private String dia;
    private String horaInicio;
    private String horaFin;
    private int Materia_idMateria;

    public HorarioModelo(String dia, String horaInicio, String horaFin, int materia_idMateria) {
        this.id= UUID.randomUUID().toString();
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        Materia_idMateria = materia_idMateria;
    }

    public String getId() {
        return id;
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

    public int getMateria_idMateria() {
        return Materia_idMateria;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(HorarioContrato.HorarioEntrada.ID,id);
        values.put(HorarioContrato.HorarioEntrada.DIA,dia);
        values.put(HorarioContrato.HorarioEntrada.HORARIO_INICIO,horaInicio);
        values.put(HorarioContrato.HorarioEntrada.HORARIO_FIN,horaFin);
        values.put(HorarioContrato.HorarioEntrada.MATERIA_IDMATERIA,Materia_idMateria);
        return values;
    }

    public HorarioModelo(Cursor cursor){
        this.id=cursor.getString(cursor.getColumnIndex(HorarioContrato.HorarioEntrada.ID));
        this.dia=cursor.getString(cursor.getColumnIndex(HorarioContrato.HorarioEntrada.DIA));
        this.horaInicio=cursor.getString(cursor.getColumnIndex(HorarioContrato.HorarioEntrada.HORARIO_INICIO));
        this.horaFin=cursor.getString(cursor.getColumnIndex(HorarioContrato.HorarioEntrada.HORARIO_FIN));
        this.Materia_idMateria=cursor.getInt(cursor.getColumnIndex(HorarioContrato.HorarioEntrada.MATERIA_IDMATERIA));
    }
}
