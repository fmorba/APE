package database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

public class MateriaModelo {
    private String id;
    private String nombre;
    private String tipo;
    private String dificultad;
    private String estado;

    public MateriaModelo(String nombre, String tipo, String dificultad, String estado) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.tipo = tipo;
        this.dificultad = dificultad;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDificultad() {
        return dificultad;
    }

    public String getEstado() {
        return estado;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MateriaContrato.MateriaEntrada.ID,id);
        values.put(MateriaContrato.MateriaEntrada.NOMBRE,nombre);
        values.put(MateriaContrato.MateriaEntrada.TIPO,tipo);
        values.put(MateriaContrato.MateriaEntrada.DIFICULTAD,dificultad);
        values.put(MateriaContrato.MateriaEntrada.ESTADO,estado);
        return values;
    }

    public MateriaModelo(Cursor cursor){
        this.id=cursor.getString(cursor.getColumnIndex(MateriaContrato.MateriaEntrada.ID));
        this.nombre=cursor.getString(cursor.getColumnIndex(MateriaContrato.MateriaEntrada.NOMBRE));
        this.tipo=cursor.getString(cursor.getColumnIndex(MateriaContrato.MateriaEntrada.TIPO));
        this.dificultad=cursor.getString(cursor.getColumnIndex(MateriaContrato.MateriaEntrada.DIFICULTAD));
        this.estado=cursor.getString(cursor.getColumnIndex(MateriaContrato.MateriaEntrada.ESTADO));
    }
}
