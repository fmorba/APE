package database;

import android.provider.BaseColumns;

public class PlanificacionContrato {

    public static abstract class PlanificacionEntrada implements BaseColumns {
        public static final String TABLE_NAME ="planificacion";

        public static final String ID = "id";
        public static final String TOTALHORAS = "totalHoras";
        public static final String EXAMEN_IDEXAMEN = "examen_idExamen";
    }
}
