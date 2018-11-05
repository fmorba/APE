package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExamenDbAyudante extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Examenes.db";

    public ExamenDbAyudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + ExamenContrato.ExamenEntrada.TABLE_NAME + " ("
                + ExamenContrato.ExamenEntrada._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ExamenContrato.ExamenEntrada.ID + " TEXT NOT NULL,"
                + ExamenContrato.ExamenEntrada.RESULTADO + " FLOAT,"
                + ExamenContrato.ExamenEntrada.MATERIA_IDMATERIA + " INTEGER NOT NULL,"
                + ExamenContrato.ExamenEntrada.EVENTO_IDEVENTO+ " INTEGER NOT NULL,"
                +" FOREIGN KEY ("+ ExamenContrato.ExamenEntrada.MATERIA_IDMATERIA+") REFERENCES "+ MateriaContrato.MateriaEntrada.TABLE_NAME+"("+ MateriaContrato.MateriaEntrada._ID+"),"
                +" FOREIGN KEY ("+ ExamenContrato.ExamenEntrada.EVENTO_IDEVENTO+") REFERENCES "+ EventoContrato.EventoEntrada.TABLE_NAME+"("+ EventoContrato.EventoEntrada._ID+"),"
                + "UNIQUE (" + ExamenContrato.ExamenEntrada.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones, pero es necesario declararlo.
    }

    public long guardarExamen(ExamenModelo modelo) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                ExamenContrato.ExamenEntrada.TABLE_NAME,
                null,
                modelo.toContentValues());

    }

    public Cursor getTodosLosExamenes() {
        return getReadableDatabase()
                .query(
                        ExamenContrato.ExamenEntrada.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getExamenPorId(String Id) {
        Cursor c = getReadableDatabase().query(
                ExamenContrato.ExamenEntrada.TABLE_NAME,
                null,
                ExamenContrato.ExamenEntrada.ID + " LIKE ?",
                new String[]{Id},
                null,
                null,
                null);
        return c;
    }

    public int deleteExamen(String Id) {
        return getWritableDatabase().delete(
                ExamenContrato.ExamenEntrada.TABLE_NAME,
        ExamenContrato.ExamenEntrada.ID + " LIKE ?",
                new String[]{Id});
    }

    public int updateExamen(ExamenModelo modelo, String Id) {
        return getWritableDatabase().update(
                ExamenContrato.ExamenEntrada.TABLE_NAME,
                modelo.toContentValues(),
                ExamenContrato.ExamenEntrada.ID + " LIKE ?",
                new String[]{Id}
        );
    }
}
