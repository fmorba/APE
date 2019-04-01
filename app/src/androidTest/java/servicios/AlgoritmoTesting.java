package servicios;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import algoritmo_genetico.AlgoritmoGenetico;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import static org.junit.Assert.assertThat;


public class AlgoritmoTesting {

    String tipoMateriaTesting = "Programación";
    int cantidadHorasTesting = 4;
    int cantidadFaltanteTest = 5;


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Tests correspondientes al gestor del algoritmo genético.
     */

    GestorAlgoritmo gestorAlgoritmo = new GestorAlgoritmo(cantidadHorasTesting,tipoMateriaTesting);

    @Test
    public void Test_GestorAlgoritmo_obtenerOptimizacion_Rango(){
        int num = gestorAlgoritmo.obtenerOptimizacion();
        assertTrue(cantidadHorasTesting-2<=num && cantidadHorasTesting+2>=num);
    }

    @Test
    public void Test_GestorAlgoritmo_GenerarPoblacion_CantidadObtenida(){
        int num = gestorAlgoritmo.generarPoblacion().size();
        assertEquals(AlgoritmoGenetico.POBLACIONTOTAL,num);
    }

    @Test
    public void Test_AlgoritmoGenetico_Optimizacion_Rango(){
        AlgoritmoGenetico algoritmoGenetico= new AlgoritmoGenetico(gestorAlgoritmo.generarPoblacion(), cantidadHorasTesting);
        int num = algoritmoGenetico.Opimizar();
        assertTrue(cantidadHorasTesting-2<=num && cantidadHorasTesting+2>=num);
    }
}
