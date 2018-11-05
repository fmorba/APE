package database;

import android.provider.BaseColumns;

public class EventoContrato {

    public static abstract class EventoEntrada implements BaseColumns{
        public static final String TABLE_NAME ="evento";

        public static final String ID = "id";
        public static final String NOMBRE = "nombre";
        public static final String FECHA = "fecha";
        public static final String HORARIO_INICIO = "horaInicio";
        public static final String HORARIO_FIN = "horaFin";
        public static final String DESCRIPCION = "descripcion";
        public static final String RECORDATORIO = "recordatorio";

    }
}
