package algoritmo_genetico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Esta es la clase que realiza los cálculos correspondiente al algoritmo genético, se realizan los
 * cálculos internamente, y retorna el mejor valor promedio.
 *
 * @author Franco Gastón Morbidoni
 * @version 1.0
 */
public class AlgoritmoGenetico {
    public static int POBLACIONTOTAL= 100;
    int GRADOMUTACION=3;
    int ELEGIDOS =12;
    int CICLOS=100;
    ArrayList<String> poblacion;
    ArrayList<String> seleccionados;
    int horasEstimadas=0;

    /**
     * Constructor
     * Establece el modelo a seguir, y la población inicial. Es llamado para preparar el algoritmo.
     * @param poblacion Población inicial, compuesta por horas semanales y resultados obtenidos.
     * @param horasEstimadas Mitad del modelos, el resultado es automáticamente asignado como 10.
     */
    public AlgoritmoGenetico(ArrayList<String> poblacion, int horasEstimadas) {
        this.poblacion = poblacion;
        this.horasEstimadas = horasEstimadas;
    }

    /**
     * Es el método principal y público de la clase, y donde se llaman a los métodos privados
     * para realizar las tareas particulares del algoritmo.     *
     */
    public int Opimizar(){
        int  resultado;

        for (int i = 0; i <CICLOS ; i++) {
            Clasificar();
            Seleccionar();
            Cruzar();
            Mutacion();
        }

        resultado=ObtenerMejorValor();

        return resultado;
    }

    /**
     * Este método compara todos los valores de la población, con el modelo y asigna un valor
     * correspondiente, mientras menor sea el valor más se acerca dicho individuo al modelo ideal.
     * Luego ordena la población de mejor a peor, en base a dicho clasificación.
     */
    private void Clasificar(){
        boolean aux = true;
        ArrayList<Integer> orden = new ArrayList<>();

        for (int i = 0; i < poblacion.size(); i++) {
            int valorUnidad=0;
            String unidad[] = poblacion.get(i).split(" - ");
            int horas = Integer.valueOf(unidad[0]);
            int nota = Integer.valueOf(unidad[1]);

            if (nota>=7){
                valorUnidad+=10-nota;
            }
            if (horasEstimadas-horas<0){
                valorUnidad+=horas-horasEstimadas;
            }else {
                valorUnidad+=horasEstimadas-horas;
            }
            orden.add(valorUnidad);
        }


        while(aux) {
            aux=false;
            for (int i = 0; i < poblacion.size()-1; i++) {

                int primerNumero = orden.get(i);
                int segundoNumero = orden.get(i+1);

                if (primerNumero>segundoNumero){
                    aux=true;
                    String holder;
                    int temporal;

                    holder=poblacion.get(i);
                    poblacion.set(i,poblacion.get(i+1));
                    poblacion.set(i+1,holder);

                    temporal= orden.get(i);
                    orden.set(i,orden.get(i+1));
                    orden.set(i+1,temporal);
                }

            }

        }
    }

    /**
     * Este método selecciona los mejores individuos de la población previa, mediante la utilizacion
     * de un lista separada.
     */
    private void Seleccionar(){
        seleccionados = new ArrayList<String>();

        for (int i = 0; i < ELEGIDOS; i++) {
            seleccionados.add(poblacion.get(i));
        }

    }

    /**
     * Este método genera una nueva poblacion mediante la mezcla de los individuos previamente
     * seleccionados como los mejores de la poblacion anterior.
     */
    private void Cruzar(){
        ArrayList<String> nuevaPoblacion = new ArrayList<String>();

        for (int i = 0; i < POBLACIONTOTAL; i++) {
            Collections.shuffle(seleccionados);

            String unidad1[] = seleccionados.get(0).split(" - ");
            int horas = Integer.valueOf(unidad1[0]);
            String unidad2[] = seleccionados.get(1).split(" - ");
            int califi = Integer.valueOf(unidad2[1]);

            nuevaPoblacion.add(horas+" - "+califi);

        }

        poblacion=nuevaPoblacion;
    }

    /**
     * Para que los nuevos individuos no sean extremadamente similares entre sí, hay un pequeño
     * porcentaje que recibirá modificación menores en sus datos, para agregar variedad a la
     * población, pero dicho porcentaje será mínimo para evitar una deriva genética
     */
    private void Mutacion(){
        Random rdn = new Random();
        int valor;

        for (int i = 0; i < POBLACIONTOTAL; i++) {

            valor = rdn.nextInt(100);

            if (valor < GRADOMUTACION) {
                int newHoras=0;
                String unidad[] = poblacion.get(i).split(" - ");
                int horas = Integer.valueOf(unidad[0]);
                int califi = Integer.valueOf(unidad[1]);

                if (rdn.nextInt(1)>0) {
                    newHoras =horas+1;
                }else{
                    newHoras = horas-1;
                }

                int newCali= rdn.nextInt(3)+8;
                califi=newCali;

                poblacion.set(i,newHoras+" - "+califi);
            }
        }
    }

    /**
     * Terminado el proceso del algoritmo genético y obteniendo una población optimizada, se genera
     * un promedio general con sus individuos, y el resultado se considera como el valor óptimo de
     * horas semanales a estudiar para el usuario.
     *
     * @return Promedio general producto de los valores de la población optimizada.
     */
    private int ObtenerMejorValor(){
        float producto=0;
        int suma=0,valorRedondeado=0;

        for (int i = 0; i < POBLACIONTOTAL; i++) {
            String unidad[] = poblacion.get(i).split(" - ");
            int horas = Integer.valueOf(unidad[0]);
            suma+=horas;
        }

        producto=(float) suma/POBLACIONTOTAL;
        valorRedondeado=Math.round(producto);

        return valorRedondeado;
    }

}
