package Djakstra;

import Airports.*;
import MySQL.MySQL;

import java.util.ArrayList;
import java.util.Hashtable;

public class Djakstra {
    private ArrayList<Airports> ListOfAirports;
    private Hashtable<String, Hashtable<String,Double> > AdjacenceMatrix;
    private int NumAirports;
    private Double result;
    public Djakstra(){
        ListOfAirports= new MySQL().getListOfAirports();
        AdjacenceMatrix =new Hashtable<String, Hashtable<String,Double> >();
        NumAirports=0;
        result=-1.0;
        //Criando a matriz de adjacencia
        for(Airports El : ListOfAirports){
            NumAirports++;
            Hashtable<String, Double > LineOfAdjacenceMatrix =new Hashtable<String, Double>();
            for(Airports Te: ListOfAirports){
                LineOfAdjacenceMatrix.put(Te.getSigla(),El.getDistance(Te));
            }
            AdjacenceMatrix.put(El.getSigla(), LineOfAdjacenceMatrix);
        }

    }
    //Retorna a sigla do aeroporto com a menor distancia
    String minDistance(Hashtable<String,Double> dist, Hashtable<String,Boolean>sptSet)
    {
        // Inicializa o valor minimo
        Double min = Double.MAX_VALUE;
        String string_min_index = "";

        //Verificar qual o aeroporo com a menor distancia
        for (Airports El: ListOfAirports){
            if (sptSet.get(El.getSigla()) == false && dist.get(El.getSigla()) <= min) {
                min = dist.get(El.getSigla());
                string_min_index = El.getSigla();
            }
        }
        return string_min_index;
    }
    //Retorna o index do aeroporto com a sigla passada por parametro.
    int minDistance(String Aux)
    {
        // Inicializa o valor minimo
        Double min = Double.MAX_VALUE;
        int min_index=-1,count=0;
        //Verifica qual o aeroporto tem o mesmo index
        for (Airports El: ListOfAirports){
            if(El.getSigla()==Aux){min_index=count;break;}
            count++;
        }
        return min_index;
    }
    void printMatrix(){//Printar a matriz de adjacencia
        for(Airports El : ListOfAirports){
            System.out.println(El.getSigla());
            for(Airports Te: ListOfAirports){
                System.out.print(AdjacenceMatrix.get(El.getSigla()).get(Te.getSigla())+", ");
            }
            System.out.println("---------------------");
        }
    }
    // Fun????o para printar o caminho e a distancia
    String printSolution(String sigla, Hashtable<String, String> path, Hashtable<String,Double> dist, Boolean print)
    {
        if(print){
            System.out.println("O caminho sera: "+path.get(sigla)+" e a distancia percorrida sera: " + dist.get(sigla) +" Km");
        }
        return path.get(sigla);
    }

    public Double getDistance()
    {
        return result;
    }
    //Algoritmo de djakstra propriamente dito
    String AlgorithDjakstra(String sigla1, String sigla2, Boolean print){ // Tentar quebrar em mais metodos

        // Fonte : https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/
        Hashtable<String,Double> dist = new Hashtable<String,Double>();  // Sa??da com a menor distancia
        Hashtable<String,Boolean> sptSet = new Hashtable<String,Boolean>();  // Para saber se um aeroporto j?? foi visitado
        Hashtable<String, String> path = new Hashtable<String,String>();

        // Inicializa as distancias como infinito e que nenhum aeroporto foi visitado
        for(Airports El : ListOfAirports){
            dist.put(El.getSigla(), Double.MAX_VALUE);
            sptSet.put(El.getSigla(), false);
        }

        // Distancia para o proprio aeroporto ?? zero e ele come??a nele mesmo
        dist.put(sigla1,0.0);
        path.put(sigla1,sigla1);

        // Algoritmo para encontrar o menor caminho para todos os vertices
        for (int count=0; count< NumAirports-1; count++) {// Tenho q colocar para ir at?? o anterior

            //Pega o aeroporto mais proximo, na primeira vez vai ser a origem
            String StringNextAirport = minDistance(dist, sptSet);
            int NextAirportIndex = minDistance(StringNextAirport);
            Airports NextAirport = ListOfAirports.get(NextAirportIndex);

            // Marca que visitou o aeroporto
            sptSet.put(StringNextAirport,true);

        // Update dist value of the adjacent vertices of
            // the picked vertex.
            for (Airports NextNextAirport: ListOfAirports){

                //Impedir que seja voo direto
                if((sigla1.equals(NextAirport.getSigla()) && sigla2.equals(NextNextAirport.getSigla()))){continue;}

                // Alterar o aeroporto s?? se ele j?? n??o foi visitado
                // A distancia do novo caminho tem que ser menor que a do atual

                if (    !sptSet.get(NextNextAirport.getSigla())
                        && AdjacenceMatrix.get(NextAirport.getSigla()).get(NextNextAirport.getSigla())!= 0
                        && dist.get(StringNextAirport) != Double.MAX_VALUE
                        && (dist.get(StringNextAirport) + AdjacenceMatrix.get(NextAirport.getSigla()).get(NextNextAirport.getSigla()) < dist.get(NextNextAirport.getSigla()))
                ) {
                    dist.put(NextNextAirport.getSigla(), dist.get(StringNextAirport) + AdjacenceMatrix.get(NextAirport.getSigla()).get(NextNextAirport.getSigla()));
                    path.put(NextNextAirport.getSigla(),path.get(NextAirport.getSigla()) +" -> "+ NextNextAirport.getSigla());
                }
            }
        }
        // print the constructed distance array and path
        result = dist.get(sigla2);
        return printSolution(sigla2, path, dist,print);
    }
    //Metodo publico para rodar o algoritmo e retornar o caminho
    public String search(String origem,String destino,boolean print){

        return AlgorithDjakstra(origem,destino,print);
    }

}

