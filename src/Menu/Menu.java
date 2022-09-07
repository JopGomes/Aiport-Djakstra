package Menu;

import Airports.*;
import Djakstra.Djakstra;
import MySQL.MySQL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Menu {
    private Djakstra TestDjakstra;
    private ArrayList<Airports> ListOfAirports;
    private ArrayList<String> NameOfAirports;
    private Scanner input;
    private HashSet<String> NameStates;
    private MySQL mySQL;
    public Menu(){
        mySQL = new MySQL();
        TestDjakstra = new Djakstra();
        ListOfAirports = mySQL.getListOfAirports();
        NameOfAirports = new ArrayList<>();
        NameStates = new HashSet<>();
        for(Airports El: ListOfAirports){
            NameOfAirports.add(El.getSigla());
            NameStates.add(El.getEstado().toLowerCase());
        }
        input = new Scanner(System.in);
    }
    public void start(){

        firstMessage();
        while(true){
            continueMessage();
        }
    }
    void firstMessage(){

        System.out.println("Bem-Vindo ao Sistema de Caminhos Baseado em Distancia(SCBD)! Selecione uma opcao:\n [1]Ver os aeroportos  [2] Realizar uma pesquisa");
        String PerguntaUm = input.next();
        switch (PerguntaUm) {
            case "1" -> listarEstados();
            case "2" -> Consultar();
            default -> System.out.println("Digite um valor valido");
        }
    }
    void continueMessage(){
        System.out.println("Deseja realizar outra acao?:\n [1]Ver os aeroportos    [2] Realizar uma pesquisa    [3] Finalizar");
        String PerguntaContinua = input.next();
        switch (PerguntaContinua) {
            case "1" -> listarEstados();
            case "2" -> Consultar();
            case "3" -> Finalizar();
            default -> System.out.println("Digite um valor valido");
        }
    }
    void ListarAeroportos(String State){
        int index=0;
        for(Airports El: ListOfAirports){
           if(El.getEstado().toLowerCase().equals(State)){
               index++;
               if(index <10){
                   System.out.println("0"+index+". O Aeroporto de sigla:" + El.getSigla() + " fica na cidade " + El.getCidade() + " do estado " + El.getEstado());
               }
               else{
                   System.out.println(index+". O Aeroporto de sigla:" + El.getSigla() + " fica na cidade " + El.getCidade() + " do estado " + El.getEstado());
               }
           }
        }
    }

    void listarEstados(){
        int index=0;
        Scanner input = new Scanner(System.in);
        for(String El: NameStates){
            index++;
            if(index <10){
                System.out.println("0"+index+". " + El.toUpperCase());
            }
            else{
                System.out.println(index+". " + El.toUpperCase());
            }
        }
        while(true){
            System.out.println("Digite o nome do estado");
            String State = input.next().toLowerCase();
            if(NameStates.contains(State)){ListarAeroportos(State);break;}
        }


    }
    private void Consultar(){
        String origin,destiny;
        while(true){
            System.out.println("\nDigite o nome do aeroporto de origem");
            origin = input.next().toUpperCase();
            if(NameOfAirports.contains(origin)){break;}
            System.out.println("\nEscolha o estado de origem e escreva uma sigla valida");
            listarEstados();
        }
        while(true){
            System.out.println("\nDigite o nome do aeroporto de destino");
            destiny = input.next().toUpperCase();
            if(NameOfAirports.contains(destiny)){break;}
            listarEstados();
            System.out.println("\nEscolha o estado de destino e escreva uma sigla valida");
        }
        String caminho = TestDjakstra.search(origin,destiny);
        mySQL.saveSearch(origin,destiny,caminho);

    }
    private void Finalizar(){//Aqui vai salvar no banco de dados as pesquisas feitas
        System.exit(0);
    }
}
