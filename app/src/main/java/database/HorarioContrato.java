package database;

import android.provider.BaseColumns;

public class HorarioContrato {

    public static abstract class HorarioEntrada implements BaseColumns {
        public static final String TABLE_NAME ="horario";

        public static final String ID = "id";
        public static final String DIA= "dia";
        public static final String HORARIO_INICIO = "horaInicio";
        public static final String HORARIO_FIN = "horaFin";
        public static final String MATERIA_IDMATERIA = "materia_idMateria";
    }
}
