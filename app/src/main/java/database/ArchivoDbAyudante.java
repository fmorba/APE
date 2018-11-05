package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ArchivoDbAyudante extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Archivos.db";

    public ArchivoDbAyudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + ArchivoContrato.ArchivoEntrada.TABLE_NAME + " ("
                + ArchivoContrato.ArchivoEntrada._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ArchivoContrato.ArchivoEntrada.ID + " TEXT NOT NULL,"
                + ArchivoContrato.ArchivoEntrada.NOMBRE + " TEXT NOT NULL,"
                + ArchivoContrato.ArchivoEntrada.FECHA + " TEXT NOT NULL,"
                + ArchivoContrato.ArchivoEntrada.TIPO + " TEXT NOT NULL,"
                + ArchivoContrato.ArchivoEntrada.DIRECCION + " TEXT NOT NULL,"
                + ArchivoContrato.ArchivoEntrada.CLAVES+ " TEXT,"
                + "UNIQUE (" + ArchivoContrato.ArchivoEntrada.ID + "))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones, pero es necesario declararlo.
    }

    public long guardarArchivo(ArchivoModelo modelo) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                ArchivoContrato.ArchivoEntrada.TABLE_NAME,
                null,
                modelo.toContentValues());

    }

    public Cursor getTodosLosArchivos() {
        return getReadableDatabase()
                .query(
                        ArchivoContrato.ArchivoEntrada.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getArchivoPorId(String archivoId) {
        Cursor c = getReadableDatabase().query(
                ArchivoContrato.ArchivoEntrada.TABLE_NAME,
                null,
                ArchivoContrato.ArchivoEntrada.ID + " LIKE ?",
                new String[]{archivoId},
                null,
                null,
                null);
        return c;
    }

    public int deleteArchivo(String archivoId) {
        return getWritableDatabase().delete(
                ArchivoContrato.ArchivoEntrada.TABLE_NAME,
                ArchivoContrato.ArchivoEntrada.ID + " LIKE ?",
                new String[]{archivoId});
    }

    public int updateArchivo(ArchivoModelo archivo, String archivoId) {
        return getWritableDatabase().update(
                ArchivoContrato.ArchivoEntrada.TABLE_NAME,
                archivo.toContentValues(),
                ArchivoContrato.ArchivoEntrada.ID + " LIKE ?",
                new String[]{archivoId}
        );
    }

}
