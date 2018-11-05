package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlanEstudioDbAyudante extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PlanesDeEstudio.db";

    public PlanEstudioDbAyudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + PlanEstudioContrato.PlanEstudioEntrada.TABLE_NAME + " ("
                + PlanEstudioContrato.PlanEstudioEntrada._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PlanEstudioContrato.PlanEstudioEntrada.ID + " TEXT NOT NULL,"
                + PlanEstudioContrato.PlanEstudioEntrada.ESTADO + " INTEGER NOT NULL,"
                + PlanEstudioContrato.PlanEstudioEntrada.PLANIFICACION_IDPLANIFICACION + " INTEGER NOT NULL,"
                + PlanEstudioContrato.PlanEstudioEntrada.EVENTO_IDEVENTO+ " INTEGER NOT NULL,"
                +" FOREIGN KEY ("+ PlanEstudioContrato.PlanEstudioEntrada.PLANIFICACION_IDPLANIFICACION+") REFERENCES "+ PlanificacionContrato.PlanificacionEntrada.TABLE_NAME+"("+ PlanificacionContrato.PlanificacionEntrada._ID+"),"
                +" FOREIGN KEY ("+ PlanEstudioContrato.PlanEstudioEntrada.EVENTO_IDEVENTO+") REFERENCES "+ EventoContrato.EventoEntrada.TABLE_NAME+"("+ EventoContrato.EventoEntrada._ID+"),"
                + "UNIQUE (" + PlanEstudioContrato.PlanEstudioEntrada.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones, pero es necesario declararlo.
    }

    public long guardarPlanEstudio(PlanEstudioModelo modelo) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                PlanEstudioContrato.PlanEstudioEntrada.TABLE_NAME,
                null,
                modelo.toContentValues());

    }

    public Cursor getTodosLosPlanes() {
        return getReadableDatabase()
                .query(
                        PlanEstudioContrato.PlanEstudioEntrada.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getPlanPorId(String Id) {
        Cursor c = getReadableDatabase().query(
                PlanEstudioContrato.PlanEstudioEntrada.TABLE_NAME,
                null,
                PlanEstudioContrato.PlanEstudioEntrada.ID + " LIKE ?",
                new String[]{Id},
                null,
                null,
                null);
        return c;
    }

    public int deletePlanEstudio(String Id) {
        return getWritableDatabase().delete(
                PlanEstudioContrato.PlanEstudioEntrada.TABLE_NAME,
                PlanEstudioContrato.PlanEstudioEntrada.ID + " LIKE ?",
                new String[]{Id});
    }

    public int updatePlanEstudio(PlanEstudioModelo modelo, String Id) {
        return getWritableDatabase().update(
                PlanEstudioContrato.PlanEstudioEntrada.TABLE_NAME,
                modelo.toContentValues(),
                PlanEstudioContrato.PlanEstudioEntrada.ID + " LIKE ?",
                new String[]{Id}
        );
    }
}
