package Menu;

import Airports.*;
import Djakstra.Djakstra;
import MySQL.MySQL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Menu {
    //Varaivel para acessar o algoritmo de Dijakstra
    private Djakstra TestDjakstra;
    //ArrayList com todos os aeroportos
    private ArrayList<Airports> ListOfAirports;
    //ArrayList com o nome de todos os aeroportos
    private ArrayList<String> NameOfAirports;
    //Variavel com scanner ( para não ter que ficar criando varios scanner)
    private Scanner input;
    //Variavel que ira salvar o nome de cada estado unicamente
    private HashSet<String> NameStates;
    //Variavel para acessar a classe MySQL
    private MySQL mySQL;
    public Menu(){
        //Instanciando as variaveis
        mySQL = new MySQL();
        TestDjakstra = new Djakstra();
        ListOfAirports = mySQL.getListOfAirports();
        NameOfAirports = new ArrayList<>();
        NameStates = new HashSet<>();
        //Adicionando as informações
        for(Airports El: ListOfAirports){
            // de siglas na lista de nomes
            NameOfAirports.add(El.getSigla());
            // de estados na lista de estados
            NameStates.add(El.getEstado().toLowerCase());
        }
        input = new Scanner(System.in);
    }
    //Metodo que irá ser chamado na main
    public void start(){
        //Chama o metodo para mostrar a mensagem inicial
        firstMessage();
        while(true){
            //Continuamente chama o metodo que ira mostrar a mensagem com opcoes
            continueMessage();
        }
    }
    void firstMessage(){
        //Escreve no console a mensagem de inicio
        System.out.println("Bem-Vindo ao Sistema de Caminhos Baseado em Distancia(SCBD)! Selecione uma opcao:\n [1] Ver os aeroportos  [2] Realizar uma pesquisa");
        //String para armazenar a resposta
        String PerguntaUm = input.next();
        switch (PerguntaUm) { //Metodo selecionado com base na escolha do usuario
            case "1" -> listarEstados(); // Para chamar um metodo para listar os estados
            case "2" -> Consultar(); // Para chamar um metodo para realizar uma consulta com dijkstra
            default -> System.out.println("Digite um valor valido"); // Caso o valor selecionado seja invalido
        }
    }
    void continueMessage(){
        System.out.println("Deseja realizar outra acao?:\n [1]Ver os aeroportos    [2] Realizar uma pesquisa    [3] Finalizar");
        //String para armazenar a resposta
        String PerguntaContinua = input.next();
        switch (PerguntaContinua) {
            case "1" -> listarEstados(); // Para chamar um metodo para listar os estados
            case "2" -> Consultar(); // Para chamar um metodo para realizar uma consulta com dijkstra
            case "3" -> Finalizar(); // Para chamar um metodo para finalizar o software
            default -> System.out.println("Digite um valor valido");// Caso o valor selecionado seja invalido
        }
    }
    void ListarAeroportos(String State){
        //Variavel que ira armazenar o indice do aeroporto
        int index=0;
        for(Airports El: ListOfAirports){//For loop para acessar todos os aeroportos
           if(El.getEstado().toLowerCase().equals(State)){
               index++;
               if(index <10){//If para manter a identação
                   //Printa as info dos aeroportos
                   System.out.println("0"+index+". O Aeroporto de sigla:" + El.getSigla() + " fica na cidade " + El.getCidade() + " do estado " + El.getEstado());
               }
               else{
                   //Printa as info dos aeroportos
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
            if(index <10){//If para manter a identação
                //Printa as info dos aeroportos
                System.out.println("0"+index+". " + El.toUpperCase());
            }
            else{
                //Printa as info dos aeroportos
                System.out.println(index+". " + El.toUpperCase());
            }
        }
        while(true){
            System.out.println("Digite um nome de estado valido para saber os aeroportos desse estado");
            //Variavel que armazena o que o usuario digitou
            String State = input.nextLine().toLowerCase();
            //Faz a pesquisa se existe o estado digitado, se existir
            // escreve todos os aeroportos desse estado
            if(NameStates.contains(State)){ListarAeroportos(State);break;}
        }
    }
    private void Consultar(){
        //Variaveis que irao armazenar o aeroporto de origem e de destino
        String origin,destiny;
        while(true){//Preso em loop ate escrever um aeroporto corretamente
            System.out.println("\nDigite o nome do aeroporto de origem");
            //recebe o input do usuario
            origin = input.next().toUpperCase();
            //Se tiver o estado digitado acaba o loop
            if(NameOfAirports.contains(origin)){break;}
            //Se não lista os estados para saber quais são os aeroportos
            listarEstados();

        }
        while(true){
            System.out.println("\nDigite o nome do aeroporto de destino");
            //recebe o input do usuario
            destiny = input.next().toUpperCase();
            //Se tiver o estado digitado acaba o loop
            if(NameOfAirports.contains(destiny)){break;}
            //Se não lista os estados para saber quais são os aeroportos
            listarEstados();
        }
        //Chama o algoritmo de djakstra para encontrar o caminho
        String caminho = TestDjakstra.search(origin,destiny);
        //Salva consulta no banco de dados
        mySQL.saveSearch(origin,destiny,caminho);

    }
    private void Finalizar(){//Aqui vai salvar no banco de dados as pesquisas feitas
        System.exit(0);
    }
}
