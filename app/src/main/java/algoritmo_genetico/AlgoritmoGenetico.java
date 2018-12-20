package algoritmo_genetico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AlgoritmoGenetico {
    public static int POBLACIONTOTAL= 20;
    int GRADOMUTACION=1;
    int ELEGIDOS =2;
    int CICLOS=100;
    ArrayList<String> poblacion;
    int horasEstimadas=0;

        public AlgoritmoGenetico(ArrayList<String> poblacion, int horasEstimadas) {
        this.poblacion = poblacion;
        this.horasEstimadas = horasEstimadas;
    }

    public int Opimizar(){
        int  resultado;

        for (int i = 0; i <CICLOS ; i++) {
            Clasificar();
            Seleccionar();
            Mutacion();
        }

        resultado=ObtenerMejorValor();

        return resultado;
    }

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

    private void Seleccionar(){
        ArrayList<String> lista = new ArrayList<String>();
        ArrayList<String> nuevaPoblacion = new ArrayList<String>();

        for (int i = 0; i < ELEGIDOS; i++) {
            lista.add(poblacion.get(i));
        }

        for (int i = 0; i < POBLACIONTOTAL; i++) {
            Collections.shuffle(lista);

            String unidad1[] = lista.get(0).split(" - ");
            int horas = Integer.valueOf(unidad1[0]);
            String unidad2[] = lista.get(1).split(" - ");
            int califi = Integer.valueOf(unidad2[1]);

            nuevaPoblacion.add(horas+" - "+califi);

        }

        poblacion=nuevaPoblacion;
    }

    private void Mutacion(){
        Random rdn = new Random();
        int valor;

        for (int i = 0; i < POBLACIONTOTAL; i++) {

            valor = rdn.nextInt(100);

            if (valor < GRADOMUTACION) {
                String unidad[] = poblacion.get(i).split(" - ");
                int horas = Integer.valueOf(unidad[0]);
                int califi = Integer.valueOf(unidad[1]);

                int newCali= rdn.nextInt(3)+8;
                califi=newCali;

                poblacion.set(i,horas+" - "+califi);
            }
        }
    }

    private int ObtenerMejorValor(){
        int producto=0;
        int suma=0;

        for (int i = 0; i < POBLACIONTOTAL; i++) {
            String unidad[] = poblacion.get(i).split(" - ");
            int horas = Integer.valueOf(unidad[0]);
            suma+=horas;
        }

        producto=suma/POBLACIONTOTAL;

        return producto;
    }

}
