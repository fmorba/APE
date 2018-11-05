package servicios;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import modelos.ModeloUsuario;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GestorUsuarioTest {

    @After
    public void tearDown() throws Exception {

    }

    @Before
    public void setUp() throws Exception {

    }

    GestorUsuario gestorUsuario = new GestorUsuario();

    @Test
    public void TestContraseñaEquivocada(){
        assertThat(gestorUsuario.ComprobarContraseña("admin","sdfdfs"), is(false));
    }

    @Test
    public void TestContraseñaCorrecta(){
        assertThat(gestorUsuario.ComprobarContraseña("admin","admin"), is(true));
    }

    @Test
    public void TestObtenerUsuarioIdConNombre(){
        assertThat(gestorUsuario.ObtenerIDUsuario("admin"), is(1));
    }

    @Test
    public void TestObtenerUsuarioIdConId(){
        assertNotNull(gestorUsuario.ObtenerDatosUsuario("1"));
    }

    @Test
    public void TestObtenerListadoUsuario(){
        assertNotNull(gestorUsuario.ObtenerListadoUsuarios());
    }




}