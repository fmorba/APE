package database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

public class ArchivoModelo {
    private String id;
    private String nombre;
    private String fecha;
    private String tipo;
    private String claves;
    private String direccion;

    public ArchivoModelo(String nombre, String fecha, String tipo, String claves, String direccion) {
        this.id= UUID.randomUUID().toString();
        this.nombre = nombre;
        this.fecha = fecha;
        this.tipo = tipo;
        this.claves = claves;
        this.direccion = direccion;
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

    public String getTipo() {
        return tipo;
    }

    public String getClaves() {
        return claves;
    }

    public String getDireccion() {
        return direccion;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ArchivoContrato.ArchivoEntrada.ID,id);
        values.put(ArchivoContrato.ArchivoEntrada.NOMBRE,nombre);
        values.put(ArchivoContrato.ArchivoEntrada.FECHA,fecha);
        values.put(ArchivoContrato.ArchivoEntrada.TIPO,tipo);
        values.put(ArchivoContrato.ArchivoEntrada.DIRECCION,direccion);
        values.put(ArchivoContrato.ArchivoEntrada.CLAVES,claves);
        return values;
    }

    public ArchivoModelo(Cursor cursor){
        this.id=cursor.getString(cursor.getColumnIndex(ArchivoContrato.ArchivoEntrada.ID));
        this.nombre=cursor.getString(cursor.getColumnIndex(ArchivoContrato.ArchivoEntrada.NOMBRE));
        this.fecha=cursor.getString(cursor.getColumnIndex(ArchivoContrato.ArchivoEntrada.FECHA));
        this.tipo=cursor.getString(cursor.getColumnIndex(ArchivoContrato.ArchivoEntrada.TIPO));
        this.direccion=cursor.getString(cursor.getColumnIndex(ArchivoContrato.ArchivoEntrada.DIRECCION));
        this.claves=cursor.getString(cursor.getColumnIndex(ArchivoContrato.ArchivoEntrada.CLAVES));
    }
}
