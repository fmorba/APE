package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventoDbAyudante extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Eventos.db";

    public EventoDbAyudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + EventoContrato.EventoEntrada.TABLE_NAME + " ("
                + EventoContrato.EventoEntrada._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EventoContrato.EventoEntrada.ID + " TEXT NOT NULL,"
                + EventoContrato.EventoEntrada.NOMBRE + " TEXT NOT NULL,"
                + EventoContrato.EventoEntrada.FECHA + " TEXT NOT NULL,"
                + EventoContrato.EventoEntrada.HORARIO_INICIO + " TEXT NOT NULL,"
                + EventoContrato.EventoEntrada.HORARIO_FIN + " TEXT NOT NULL,"
                + EventoContrato.EventoEntrada.DESCRIPCION + " TEXT,"
                + EventoContrato.EventoEntrada.RECORDATORIO + " INT NOT NULL,"
                + "UNIQUE (" + EventoContrato.EventoEntrada.ID + "))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones, pero es necesario declararlo.
    }

    public long guardarEvento(EventoModelo evento) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                EventoContrato.EventoEntrada.TABLE_NAME,
                null,
                evento.toContentValues());

    }

    public Cursor getTodosLosEventos() {
        return getReadableDatabase()
                .query(
                        EventoContrato.EventoEntrada.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getEventoPorId(String eventoId) {
        Cursor c = getReadableDatabase().query(
                EventoContrato.EventoEntrada.TABLE_NAME,
                null,
                EventoContrato.EventoEntrada.ID + " LIKE ?",
                new String[]{eventoId},
                null,
                null,
                null);
        return c;
    }

    public int deleteEvento(String eventoId) {
        return getWritableDatabase().delete(
                EventoContrato.EventoEntrada.TABLE_NAME,
                EventoContrato.EventoEntrada.ID + " LIKE ?",
                new String[]{eventoId});
    }

    public int updateArchivo(EventoModelo evento, String eventoId) {
        return getWritableDatabase().update(
                EventoContrato.EventoEntrada.TABLE_NAME,
                evento.toContentValues(),
                EventoContrato.EventoEntrada.ID + " LIKE ?",
                new String[]{eventoId}
        );
    }

}
