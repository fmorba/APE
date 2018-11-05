package database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

public class PlanEstudioModelo {
    private String id;
    private int estado;
    private int planificacion_idPlanificacion;
    private int evento_idEvento;

    public PlanEstudioModelo(boolean estadoBooleano, int planificacion_idPlanificacion, int evento_idEvento) {
        this.id = UUID.randomUUID().toString();
        if (estadoBooleano=true){
            this.estado = 1;
        }else{
            this.estado= 0;
        }
        this.planificacion_idPlanificacion = planificacion_idPlanificacion;
        this.evento_idEvento = evento_idEvento;
    }

    public String getId() {
        return id;
    }

    public int getEstado() {
        return estado;
    }

    public int getPlanificacion_idPlanificacion() {
        return planificacion_idPlanificacion;
    }

    public int getEvento_idEvento() {
        return evento_idEvento;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PlanEstudioContrato.PlanEstudioEntrada.ID,id);
        values.put(PlanEstudioContrato.PlanEstudioEntrada.ESTADO,estado);
        values.put(PlanEstudioContrato.PlanEstudioEntrada.PLANIFICACION_IDPLANIFICACION,planificacion_idPlanificacion);
        values.put(PlanEstudioContrato.PlanEstudioEntrada.EVENTO_IDEVENTO,evento_idEvento);
        return values;
    }

    public PlanEstudioModelo(Cursor cursor){
        this.id=cursor.getString(cursor.getColumnIndex(PlanEstudioContrato.PlanEstudioEntrada.ID));
        this.estado=cursor.getInt(cursor.getColumnIndex(PlanEstudioContrato.PlanEstudioEntrada.ESTADO));
        this.planificacion_idPlanificacion=cursor.getInt(cursor.getColumnIndex(PlanEstudioContrato.PlanEstudioEntrada.PLANIFICACION_IDPLANIFICACION));
        this.evento_idEvento=cursor.getInt(cursor.getColumnIndex(PlanEstudioContrato.PlanEstudioEntrada.EVENTO_IDEVENTO));
    }
}
