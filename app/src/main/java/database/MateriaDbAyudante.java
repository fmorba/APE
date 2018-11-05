package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MateriaDbAyudante extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Materias.db";

    public MateriaDbAyudante(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + MateriaContrato.MateriaEntrada.TABLE_NAME + " ("
                + MateriaContrato.MateriaEntrada._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MateriaContrato.MateriaEntrada.ID + " TEXT NOT NULL,"
                + MateriaContrato.MateriaEntrada.NOMBRE + " TEXT NOT NULL,"
                + MateriaContrato.MateriaEntrada.TIPO + " TEXT NOT NULL,"
                + MateriaContrato.MateriaEntrada.DIFICULTAD + " TEXT NOT NULL,"
                + MateriaContrato.MateriaEntrada.ESTADO + " TEXT NOT NULL,"
                + "UNIQUE (" + MateriaContrato.MateriaEntrada.ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones, pero es necesario declararlo.
    }

    public long guardarMateria(MateriaModelo materia) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                MateriaContrato.MateriaEntrada.TABLE_NAME,
                null,
                materia.toContentValues());

    }

    public Cursor getTodasLasMaterias() {
        return getReadableDatabase()
                .query(
                        MateriaContrato.MateriaEntrada.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getMateriaPorId(String materiaId) {
        Cursor c = getReadableDatabase().query(
                MateriaContrato.MateriaEntrada.TABLE_NAME,
                null,
                MateriaContrato.MateriaEntrada.ID + " LIKE ?",
                new String[]{materiaId},
                null,
                null,
                null);
        return c;
    }

    public int deleteMateria(String materiaId) {
        return getWritableDatabase().delete(
                MateriaContrato.MateriaEntrada.TABLE_NAME,
                MateriaContrato.MateriaEntrada.ID + " LIKE ?",
                new String[]{materiaId});
    }

    public int updateMateria(MateriaModelo modelo, String materiaId) {
        return getWritableDatabase().update(
                MateriaContrato.MateriaEntrada.TABLE_NAME,
                modelo.toContentValues(),
                MateriaContrato.MateriaEntrada.ID + " LIKE ?",
                new String[]{materiaId}
        );
    }

}


