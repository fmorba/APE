package database;

import android.provider.BaseColumns;

public class ArchivoContrato {

    public static abstract class ArchivoEntrada implements BaseColumns {
        public static final String TABLE_NAME ="archivo";

        public static final String ID = "id";
        public static final String NOMBRE = "nombre";
        public static final String FECHA = "fecha";
        public static final String TIPO= "tipo";
        public static final String DIRECCION = "direccion";
        public static final String CLAVES = "claves";
    }
}
