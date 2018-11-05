package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HorarioDbAyudante extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Horarios.db";

    public HorarioDbAyudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + HorarioContrato.HorarioEntrada.TABLE_NAME + " ("
                + HorarioContrato.HorarioEntrada._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + HorarioContrato.HorarioEntrada.ID + " TEXT NOT NULL,"
                + HorarioContrato.HorarioEntrada.DIA + " TEXT NOT NULL,"
                + HorarioContrato.HorarioEntrada.HORARIO_INICIO + " TEXT NOT NULL,"
                + HorarioContrato.HorarioEntrada.HORARIO_FIN + " TEXT NOT NULL,"
                + HorarioContrato.HorarioEntrada.MATERIA_IDMATERIA+ " INTEGER NOT NULL,"
                +" FOREIGN KEY ("+ HorarioContrato.HorarioEntrada.MATERIA_IDMATERIA+") REFERENCES "+ MateriaContrato.MateriaEntrada.TABLE_NAME+"("+ MateriaContrato.MateriaEntrada._ID+"),"
                + "UNIQUE (" + HorarioContrato.HorarioEntrada.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones, pero es necesario declararlo.
    }

    public long guardarHorario(HorarioModelo modelo) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                HorarioContrato.HorarioEntrada.TABLE_NAME,
                null,
                modelo.toContentValues());

    }

    public Cursor getTodosLosHorarios() {
        return getReadableDatabase()
                .query(
                        HorarioContrato.HorarioEntrada.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getHorarioPorId(String Id) {
        Cursor c = getReadableDatabase().query(
                HorarioContrato.HorarioEntrada.TABLE_NAME,
                null,
                HorarioContrato.HorarioEntrada.ID + " LIKE ?",
                new String[]{Id},
                null,
                null,
                null);
        return c;
    }

    public int deleteHorario(String Id) {
        return getWritableDatabase().delete(
                HorarioContrato.HorarioEntrada.TABLE_NAME,
                HorarioContrato.HorarioEntrada.ID + " LIKE ?",
                new String[]{Id});
    }

    public int updateHorario(HorarioModelo modelo, String Id) {
        return getWritableDatabase().update(
                HorarioContrato.HorarioEntrada.TABLE_NAME,
                modelo.toContentValues(),
                HorarioContrato.HorarioEntrada.ID + " LIKE ?",
                new String[]{Id}
        );
    }

}
