package database;

import android.provider.BaseColumns;

public class ExamenContrato {

    public static abstract class ExamenEntrada implements BaseColumns {
        public static final String TABLE_NAME ="examen";

        public static final String ID = "id";
        public static final String RESULTADO = "resultado";
        public static final String MATERIA_IDMATERIA = "materia_idMateria";
        public static final String EVENTO_IDEVENTO = "evento_idEvento";
    }
}
