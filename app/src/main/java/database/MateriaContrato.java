package database;

import android.provider.BaseColumns;

public class MateriaContrato {
    public static abstract class MateriaEntrada implements BaseColumns {
        public static final String TABLE_NAME ="materia";

        public static final String ID = "id";
        public static final String NOMBRE = "nombre";
        public static final String TIPO = "tipo";
        public static final String DIFICULTAD = "dificultad";
        public static final String ESTADO = "estado";

    }

}
