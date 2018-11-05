package database;

import android.provider.BaseColumns;

public class PlanEstudioContrato {
    public static abstract class PlanEstudioEntrada implements BaseColumns {
        public static final String TABLE_NAME ="planEstudio";

        public static final String ID = "id";
        public static final String ESTADO = "estado";
        public static final String PLANIFICACION_IDPLANIFICACION = "planificacion_idPlanificacion";
        public static final String EVENTO_IDEVENTO = "evento_idEvento";

    }
}
