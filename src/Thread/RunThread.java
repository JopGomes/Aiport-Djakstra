package Thread;

import Djakstra.Djakstra;

import java.util.HashMap;
import java.util.Vector;

public class RunThread extends Thread {
    private String origem;
    private String destino;
    int posicao;
    public static int quantidade;
    public static Vector<Double> solution;
    public static Djakstra test;
    public static Vector<String[]> answer;

    public RunThread(String origem, String destino, int posicao){
        this.origem=origem;
        this.destino=destino;
        this.posicao=posicao;
    }
    public void run(){
        test.search(origem,destino,false);
        solution.set(posicao, test.getDistance() );
        String[] par ={origem,destino,test.getDistance().toString()};
        answer.add(par);
        RunThread.quantidade++;
        System.out.println(quantidade);
    }

}
