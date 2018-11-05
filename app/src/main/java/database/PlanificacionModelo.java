package database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

public class PlanificacionModelo {
    private String id;
    private int totalHoras;
    private int examen_idExamen;

    public PlanificacionModelo(int totalHoras, int examen_idExamen) {
        this.id = UUID.randomUUID().toString();
        this.totalHoras = totalHoras;
        this.examen_idExamen = examen_idExamen;
    }

    public String getId() {
        return id;
    }

    public int getTotalHoras() {
        return totalHoras;
    }

    public int getExamen_idExamen() {
        return examen_idExamen;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PlanificacionContrato.PlanificacionEntrada.ID,id);
        values.put(PlanificacionContrato.PlanificacionEntrada.TOTALHORAS,totalHoras);
        values.put(PlanificacionContrato.PlanificacionEntrada.EXAMEN_IDEXAMEN,examen_idExamen);
        return values;
    }

    public PlanificacionModelo(Cursor cursor){
        this.id=cursor.getString(cursor.getColumnIndex(PlanificacionContrato.PlanificacionEntrada.ID));
        this.totalHoras=cursor.getInt(cursor.getColumnIndex(PlanificacionContrato.PlanificacionEntrada.TOTALHORAS));
        this.examen_idExamen=cursor.getInt(cursor.getColumnIndex(PlanificacionContrato.PlanificacionEntrada.EXAMEN_IDEXAMEN));
    }

}
