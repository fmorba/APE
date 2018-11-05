package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlanificacionDbAyudante extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Planificaciones.db";

    public PlanificacionDbAyudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + PlanificacionContrato.PlanificacionEntrada.TABLE_NAME + " ("
                + PlanificacionContrato.PlanificacionEntrada._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PlanificacionContrato.PlanificacionEntrada.ID + " TEXT NOT NULL,"
                + PlanificacionContrato.PlanificacionEntrada.TOTALHORAS + " INTEGER,"
                + PlanificacionContrato.PlanificacionEntrada.EXAMEN_IDEXAMEN + " INTEGER NOT NULL,"
                +" FOREIGN KEY ("+ PlanificacionContrato.PlanificacionEntrada.EXAMEN_IDEXAMEN+") REFERENCES "+ ExamenContrato.ExamenEntrada.TABLE_NAME+"("+ ExamenContrato.ExamenEntrada._ID+"),"
                + "UNIQUE (" + PlanificacionContrato.PlanificacionEntrada.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones, pero es necesario declararlo.
    }

    public long guardarPlanificacion(PlanificacionModelo modelo) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                PlanificacionContrato.PlanificacionEntrada.TABLE_NAME,
                null,
                modelo.toContentValues());

    }

    public Cursor getTodasLasPlanificaciones() {
        return getReadableDatabase()
                .query(
                        PlanificacionContrato.PlanificacionEntrada.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getPlanificacionPorId(String Id) {
        Cursor c = getReadableDatabase().query(
                PlanificacionContrato.PlanificacionEntrada.TABLE_NAME,
                null,
                PlanificacionContrato.PlanificacionEntrada.ID + " LIKE ?",
                new String[]{Id},
                null,
                null,
                null);
        return c;
    }

    public int deletePlanificacion(String Id) {
        return getWritableDatabase().delete(
                PlanificacionContrato.PlanificacionEntrada.TABLE_NAME,
                PlanificacionContrato.PlanificacionEntrada.ID + " LIKE ?",
                new String[]{Id});
    }

    public int updatePlanificacion(PlanificacionModelo modelo, String Id) {
        return getWritableDatabase().update(
                PlanificacionContrato.PlanificacionEntrada.TABLE_NAME,
                modelo.toContentValues(),
                PlanificacionContrato.PlanificacionEntrada.ID + " LIKE ?",
                new String[]{Id}
        );
    }
}
