package servicios;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GestorEventoTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    GestorEvento gestorEvento = new GestorEvento();

    @Test
    public void TestBusquedaDeHorarios(){
        assertNotNull(gestorEvento.ObtenerHorariosSegunFechas("2125-12-12",1));
    }

    @Test
    public void TestBusquedaDeHorariosDatosErroneos(){
        assertNull(gestorEvento.ObtenerHorariosSegunFechas("345-675123-13",234));
    }

    @Test
    public void TestBusquedaDeIdPorFechas(){
        assertNotNull(gestorEvento.ObtenerIdSegunFechas("2125-12-12",1));
    }

    @Test
    public void TestBusquedaDeIdProFechasDatosErroneos(){
        assertNull(gestorEvento.ObtenerIdSegunFechas("345-675123-13",234));
    }

    @Test
    public void TestValidarHorariosLibre(){
        assertThat(gestorEvento.ValidarHorarios("2125-12-12","12:10", "13:20",1), is(""));
    }

    @Test
    public void TestValidarHorariosOcupados(){
        assertNotEquals("",gestorEvento.ValidarHorarios("2125-12-12","15:10", "16:20",1));
    }

    @Test
    public void TestValidarHorariosDatosErroneos(){
        assertNotEquals("",gestorEvento.ValidarHorarios("2125-12-12","18:fds0", "19:20",1));
    }

    @Test
    public void TestValidarHorariosLibreModificacion(){
        assertThat(gestorEvento.ValidarHorariosModificacion("2125-12-12","12:10", "13:20",1, "1"), is(""));
    }

    @Test
    public void TestValidarHorariosOcupadosModificacionMismoEvento(){
        assertThat(gestorEvento.ValidarHorariosModificacion("2125-12-12","12:10", "14:20",1, "1"),is(""));
    }

    @Test
    public void TestValidarHorariosDatosErroneosModificacion(){
        assertNotEquals("",gestorEvento.ValidarHorariosModificacion("2125-12-12","1df", "19:20",1,"1"));
    }

    @Test
    public void TestObtenerIdEvento(){
        assertNotNull(gestorEvento.ObtenerDatosEventoPorId("1"));
    }

    @Test
    public void TestObtenerRecordatorios(){
        assertNotNull(gestorEvento.ObtenerRecordatorios("2125-12-12",1));
    }
}