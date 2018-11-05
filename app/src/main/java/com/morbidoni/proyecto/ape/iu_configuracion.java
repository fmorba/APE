package com.morbidoni.proyecto.ape;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import modelos.ModeloUsuario;
import servicios.GestorEvento;
import servicios.GestorUsuario;

public class iu_configuracion extends AppCompatActivity {
    EditText editProvincia, editLocalidad, editFechaNacimiento, editCarrera, editContraseña1, editContraseña2;
    TextView txtIdUsuario, txtNombre;
    Button btnActualizarDatos, btnCambiarClave, btnLimpiarAgenda, btnEliminarEventos;
    CheckBox checkCambioClave;
    String idUsuario, respuesta;
    ModeloUsuario usuario = new ModeloUsuario();
    GestorUsuario gestor = new GestorUsuario();
    GestorEvento gestorEvento = new GestorEvento();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iu_configuracion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarConfiguracion);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();
        idUsuario = ""+intent.getIntExtra("id",0);

        txtNombre = (TextView) findViewById(R.id.txtNombreUsuarioConfiguracion);
        editProvincia = (EditText) findViewById(R.id.editProvinciaConfiguracion);
        editLocalidad = (EditText) findViewById(R.id.editLocalidadConfiguracion);
        editFechaNacimiento = (EditText) findViewById(R.id.editFechaNacimientoConfiguracion);
        editCarrera = (EditText) findViewById(R.id.editCarreraConfiguracion);
        editContraseña1 = (EditText) findViewById(R.id.editContraseñaConfiguracion);
        editContraseña2 = (EditText) findViewById(R.id.editContraseñaRepeticionConfiguracion);
        checkCambioClave = (CheckBox) findViewById(R.id.checkCambioConfiguracion);
        txtIdUsuario = (TextView) findViewById(R.id.txtIdUsuarioConfiguracion);

        btnActualizarDatos = (Button) findViewById(R.id.btnActualizarConfiguracion);
        btnCambiarClave = (Button) findViewById(R.id.btnModificarClave);
        btnLimpiarAgenda = (Button) findViewById(R.id.btnBorrarAgenda);
        btnEliminarEventos = (Button) findViewById(R.id.btnBorrarEventosAntiguos);
        editContraseña1.setEnabled(false);
        editContraseña2.setEnabled(false);
        btnCambiarClave.setEnabled(false);

        txtIdUsuario.setText(getString(R.string.identificador_usuario,idUsuario));
        usuario = gestor.ObtenerDatosUsuario(idUsuario);
        txtNombre.setText(getString(R.string.nombre_identificador_usuario,usuario.getNombre()));
        this.ActualiarVentana(usuario);

        checkCambioClave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkCambioClave.isChecked()){
                    editContraseña1.setEnabled(true);
                    editContraseña2.setEnabled(true);
                    btnCambiarClave.setEnabled(true);
                }else {
                    editContraseña1.setEnabled(false);
                    editContraseña2.setEnabled(false);
                    btnCambiarClave.setEnabled(false);
                }
            }
        });

        btnActualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    if (editFechaNacimiento.getText().toString().equals("null")==false) {
                    String fechaDatos[] = editFechaNacimiento.getText().toString().split("-");
                    int año = Integer.valueOf(fechaDatos[0]);
                    int mes = Integer.valueOf(fechaDatos[1]);
                    int dia = Integer.valueOf(fechaDatos[2]);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setLenient(false);
                    calendar.set(Calendar.YEAR, año);
                    calendar.set(Calendar.MONTH, mes - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, dia);
                    Date date = calendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

                    usuario.setFechaNacimiento(editFechaNacimiento.getText().toString());
                    }

                    if (editProvincia.getText().toString().equals("null")==false) {
                        usuario.setProvincia(editProvincia.getText().toString());
                    }
                    if (editLocalidad.getText().toString().equals("null")==false) {
                        usuario.setLocalidad(editLocalidad.getText().toString());
                    }
                    if (editCarrera.getText().toString().equals("null")==false){
                        usuario.setCarrera(editCarrera.getText().toString());
                    }
                    respuesta=gestor.ActualizarDatosUsuario(usuario);
                    Toast.makeText(iu_configuracion.this, respuesta, Toast.LENGTH_SHORT).show();
                    ActualiarVentana(usuario);

                }
                catch (IllegalArgumentException e){
                    Toast.makeText(iu_configuracion.this, R.string.mensaje_formato_fecha_erroneo, Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnCambiarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editContraseña1.getText().toString().equals(editContraseña2.getText().toString())){
                    usuario.setContraseña(editContraseña1.getText().toString());
                    respuesta=gestor.ActualizarDatosUsuario(usuario);
                    Toast.makeText(iu_configuracion.this, respuesta, Toast.LENGTH_SHORT).show();
                    ActualiarVentana(usuario);
                }else {
                    Toast.makeText(iu_configuracion.this, R.string.contraseña_incorrecta, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLimpiarAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder advertencia = new AlertDialog.Builder(iu_configuracion.this);
                advertencia.setTitle(R.string.borrar_agenda);
                advertencia.setMessage(R.string.mensaje_confirmacion_borrado_agenda);
                advertencia.setCancelable(true);
                advertencia.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        String resultado = gestorEvento.EliminarEventoSegunUsuario(idUsuario);
                        Toast.makeText(iu_configuracion.this, resultado, Toast.LENGTH_SHORT).show();
                    }
                });
                advertencia.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                advertencia.show();
            }
        });

        btnEliminarEventos.setOnClickListener(new View.OnClickListener() {
            String hoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder advertencia = new AlertDialog.Builder(iu_configuracion.this);
                advertencia.setTitle(R.string.borrar_eventos_antiguos);
                advertencia.setMessage(R.string.mensaje_confirmacion_borrado_eventos_viejos);
                advertencia.setCancelable(true);
                advertencia.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        String resultado = gestorEvento.EliminarEventosAntiguos(hoy,idUsuario);
                        Toast.makeText(iu_configuracion.this, resultado, Toast.LENGTH_SHORT).show();
                    }
                });
                advertencia.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                advertencia.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iu_ayuda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Esta parte controla el menu de la barra de tareas de la aplicación.
        int id = item.getItemId();

        if (id == R.id.menu_unico_ayuda) {
            final Intent intentAyuda = new Intent(this,iu_ayuda.class);
            startActivity(intentAyuda);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ActualiarVentana(ModeloUsuario usuario){
        editProvincia.setText(usuario.getProvincia());
        editLocalidad.setText(usuario.getLocalidad());
        editFechaNacimiento.setText(usuario.getFechaNacimiento());
        editCarrera.setText(usuario.getCarrera());
    }

}
