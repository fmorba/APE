package database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

public class ExamenModelo {
    private String id;
    private float resultado;
    private int materias_idMaterias;
    private int evento_idEvento;

    public ExamenModelo(float resultado, int materias_idmaterias, int evento_idevento) {
        this.id= UUID.randomUUID().toString();
        this.resultado = resultado;
        this.materias_idMaterias = materias_idmaterias;
        this.evento_idEvento = evento_idevento;
    }

    public String getId() {
        return id;
    }

    public float getResultado() {
        return resultado;
    }

    public int getMaterias_idMaterias() {
        return materias_idMaterias;
    }

    public int getEvento_idEvento() {
        return evento_idEvento;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ExamenContrato.ExamenEntrada.ID,id);
        values.put(ExamenContrato.ExamenEntrada.RESULTADO,resultado);
        values.put(ExamenContrato.ExamenEntrada.MATERIA_IDMATERIA,materias_idMaterias);
        values.put(ExamenContrato.ExamenEntrada.EVENTO_IDEVENTO,evento_idEvento);
        return values;
    }

    public ExamenModelo(Cursor cursor){
        this.id=cursor.getString(cursor.getColumnIndex(ExamenContrato.ExamenEntrada.ID));
        this.resultado=cursor.getFloat(cursor.getColumnIndex(ExamenContrato.ExamenEntrada.RESULTADO));
        this.materias_idMaterias=cursor.getInt(cursor.getColumnIndex(ExamenContrato.ExamenEntrada.MATERIA_IDMATERIA));
        this.evento_idEvento=cursor.getInt(cursor.getColumnIndex(ExamenContrato.ExamenEntrada.EVENTO_IDEVENTO));
    }

}
