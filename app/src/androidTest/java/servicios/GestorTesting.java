package servicios;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import static org.junit.Assert.assertThat;


public class GestorTesting {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }


    GestorEvento gestorEvento = new GestorEvento();
    GestorArchivos gestorArchivos = new GestorArchivos();
    GestorMateria gestorMateria = new GestorMateria();
    GestorExamen gestorExamen = new GestorExamen();
    GestorPlanificador gestorPlanificador = new GestorPlanificador();
    GestorUsuario gestorUsuario = new GestorUsuario();

    /**
     * Tests correspondientes al gestor de eventos.
     */

    @Test
    public void Test_GestorEventos_obtenerEventosSegunFechas_FechaErrornea(){
        assertNull(gestorEvento.obtenerEventosSegunFechas("234234"));
    }

    @Test
    public void Test_GestorEventos_obtenerIdSegunFechas_FechaErrornea(){
        assertNull(gestorEvento.obtenerEventosSegunFechas("234234"));
    }

    @Test
    public void Test_GestorEventos_validarHorarios_FechaErrornea(){
        assertNotEquals("",gestorEvento.validarHorarios("dsfsdf","11:00", "12:00"));
    }

    @Test
    public void Test_GestorEventos_validarHorarios_HorariosErroneos(){
        assertNotEquals("",gestorEvento.validarHorarios("2019-05-12","14500", "12f00"));
    }

    @Test
    public void Test_GestorEventos_validarHorarios_DatosCorrectos(){
        assertNotNull(gestorEvento.validarHorarios("2119-05-12","14:00", "15:00"));
    }

    @Test
    public void Test_GestorEventos_obtenerDatosEventoPorId_IDErronea(){
        assertNotEquals("",gestorEvento.obtenerDatosEventoPorId("234234","2019-05-20"));
    }

    @Test
    public void Test_GestorEventos_obtenerDatosEventoPorId_FechaIncompatible(){
        assertNotEquals("",gestorEvento.obtenerDatosEventoPorId("1","2"));
    }

    @Test
    public void Test_GestorEventos_obtenerDatosEventoPorId_DatosCorrectos(){
       assertNotNull(gestorEvento.obtenerDatosEventoPorId("1","2119-05-20"));
    }

    @Test
    public void Test_GestorEventos_obtenerRecordatorios_FechaIncorrecta(){
        assertNull(gestorEvento.obtenerRecordatorios("2119gf-12"));
    }

    /**
     * Test correspondientes al gestor de archivos
     */

    @Test
    public void Test_GestorArchivos_obtenerListadoArchivosPorTipos_TipoErroneo(){
        assertNull(gestorArchivos.obtenerListadoArchivosPorTipos("sdfsdfs"));
    }

    @Test
    public void Test_GestorArchivos_obtenerDatosArchivo_IDErroneo(){
        assertNull(gestorArchivos.obtenerDatosArchivo("sdfsdfs","Fotos"));
    }

    @Test
    public void Test_GestorArchivos_obtenerListadoArchivosPorClaves_ClavesNulas(){
        assertNull(gestorArchivos.obtenerListadoArchivosPorClaves(null));
    }

    /**
     * Test correspondientes al gestor de exámenes
     */

    @Test
    public void Test_GestorExamenes_obtenerDatosExamenPorId_IDIncorrecta(){
        assertNull(gestorExamen.obtenerDatosExamenPorId("iasdfg"));
    }

    @Test
    public void Test_GestorExamenes_obtenerListadoExamenesPorMateria_MateriaErronea(){
        assertNull(gestorExamen.obtenerListadoExamenesPorMateria("spigsfspidfsp"));
    }

    /**
     * Test correspondientes al gestor de materias
     */

    @Test
    public void Test_GestorMaterias_obtenerDatosMateria_IDErroneo(){
        assertNull(gestorMateria.obtenerDatosMateria("1245aqs"));
    }

    @Test
    public void Test_GestorMaterias_obtenerHorariosPorMateria_IDErroneo(){
        assertNull(gestorMateria.obtenerHorariosPorMateria("1245aqs"));
    }

    @Test
    public void Test_GestorMaterias_obtenerListadoMateriasHorariosPorDia_DiaNoExistente(){
        assertNull(gestorMateria.obtenerListadoMateriasHorariosPorDia("susterd"));
    }

    /**
     * Test correspondientes al gestor del planificador
     */

    @Test
    public void Test_GestorPlanificador_obtenerPlanesDeEstudioRegistrados_PlanificacionNula(){
        assertNull(gestorPlanificador.obtenerPlanesDeEstudioRegistrados(null));
    }

    @Test
    public void Test_GestorPlanificador_obtenerListadoPlanificacionesPorTipo_TipoMateriaErroneo(){
        assertNull(gestorPlanificador.obtenerListadoPlanificacionesPorTipo("dsuifgsi","Historia"));
    }

    @Test
    public void Test_GestorPlanificador_obtenerListadoPlanificacionesPorTipo_NombreMateriaNoExistente(){
        assertNull(gestorPlanificador.obtenerListadoPlanificacionesPorTipo("Administración","Hiadsasda"));
    }

    /**
     * Test correspondientes al gestor del usuario
     */

    @Test
    public void Test_GestorUsuario_obtenerDatosUsuario_IDIncorrecta(){
        assertNull(gestorUsuario.obtenerDatosUsuario("934762394"));
    }





}
