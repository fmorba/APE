package com.morbidoni.proyecto.ape;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Clase encargada de la interfaz que muestra los mensajes de ayuda, que varían según de donde fue
 * llamada.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class iu_ayuda extends AppCompatActivity {
    TextView txtAyuda;
    String origenPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_ayuda);

        Intent intent = getIntent();
        Bundle getuser = getIntent().getExtras();
        origenPedido = getuser.getString("ayuda");

        txtAyuda = (TextView) findViewById(R.id.txtAyudaMensaje);

        setMensajeAyuda(origenPedido);

    }

    /**
     * Método que determina qué tipo de mensaje se mostrara en la interfaz, según el origen del
     * pedido de ayuda.
     *
     * @param origen String que determina la actividad de donde se activó el menú de ayuda.
     */
    private void setMensajeAyuda(String origen){

        switch (origen){
            case "agenda":
                txtAyuda.setText(getResources().getText(R.string.ayuda_agenda));
                break;

            case "archivos":
                txtAyuda.setText(getResources().getText(R.string.ayuda_archivos));
                break;

            case "configuracion":
                txtAyuda.setText(getResources().getText(R.string.ayuda_configuracion));
                break;

            case "examenes":
                txtAyuda.setText(getResources().getText(R.string.ayuda_examenes));
                break;

            case "inicio":
                txtAyuda.setText(getResources().getText(R.string.ayuda_inicio));
                break;

            case "materias":
                txtAyuda.setText(getResources().getText(R.string.ayuda_materias));
                break;

            case "metricas":
                txtAyuda.setText(getResources().getText(R.string.ayuda_metricas));
                break;

            case "planificador":
                txtAyuda.setText(getResources().getText(R.string.ayuda_planificador));
                break;

            case "revisar_plan":
                txtAyuda.setText(getResources().getText(R.string.ayuda_revisar_planes));
                break;

            case "consejos":
                txtAyuda.setText(getResources().getText(R.string.ayuda_consejos));
                break;

            default:
                txtAyuda.setText(getResources().getText(R.string.error_fallo_ventana));
                break;
        }
    }
}
