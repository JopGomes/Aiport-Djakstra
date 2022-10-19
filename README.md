# Trabalho Airport Dijkstra


# Introdução

- O objetivo final desse código é salvar o caminho de viagem a partir de um aeroporto de origem e um de destino em um banco de dados, tendo como restrição para esse trajeto ter ao menos uma conexão.

Para tal foi criado um sistema de pastas e classes:

Dentro da pasta “src” foram adicionadas 4 packages cujo nomes são o mesmo da classe:

- Airports → Um package com uma classe que contém uma abstração sobre aeroportos.
- MySQL → Um package com uma classe que irá ter todas as funcionalidades relacionadas ao banco de dados, desde a retirada de dados até o seu salvamento.
- Djakstra → Um package com uma classe que busca calcular o menor caminho entre dois aeroportos, respeitando a condição imposta usando o algoritmo de **Dijkstra.**
- Menu → Um package com uma classe que irá implementar a relação do usuário com o código. Ou seja, fazendo com que no console todas as informações necessárias para as buscas estejam de fácil acesso.


O arquivo main irá ser onde a aplicação terá sua sessão propriamente dita sendo iniciada, e o arquivo airport_data.csv possui todas as informações necessárias sobre os aeroportos internacionais  no Brasil.

# Explicação dos Packages

## Airports

A classe “Airports” é uma abstração para representar um aeroporto do país, tendo como parâmetros informações relativas do aeroporto que o identificam de maneira unica e/ou facilitam na busca por ele, como: a sigla, latitude, longitude, cidade e estado. Podendo recuperar eles ou alterá-los de apenas via métodos.

```java
public class Airports {
	//Variavel que ira armazenar a sigla do aeroporto
    private String sigla;
	//Variavel que irá armazenar a latitude do aeroporto
    private double latitude;
	//Variavel que irá armazenar a longitudee do aeroporto
    private double longitude;
	//Variavel que irá armazenar a cidade do aeroporto
    private String cidade;
	//Variavel que irá armazenar o estado do aeroporto
    private String  estado;

    public Airports(String sigla, double latitude, double longitude, String cidade, String estado) {
        this.sigla = sigla;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cidade = cidade;
        this.estado = estado;
    }
// Metodos para conseguir recuperar e alterar os parâmetros
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
```

Foi também incluído um método para conseguir a relação de distância entre dois aeroportos, necessária para a resolução do problema.

```java
// Calcula a distância entre dois aeroportos
    public double getDistance(Airports Second){
	//Variavel que irá armazenar a latitude do objeto aeroporto atual
        double firstLatitude = latitude;
	//Variavel que irá armazenar a latitude do parametro aeroporto passado
        double secondLatitude = Second.getLatitude();
	//Variavel que irá armazenar a longitude do objeto aeroporto atual
        double firstLongitude = longitude;
	//Variavel que irá armazenar a latitude do parametro aeroporto passado
        double secondLongitude = Second.getLongitude();

//Raio da terra
        double EARTH_RADIUS_KM= 6371;
	//Variavel que irá armazenar a latitude do objeto aeroporto atual em radiano
        double firstLatToRad = Math.toRadians(firstLatitude);
	//Variavel que irá armazenar a latitude do parametro aeroporto em radiano
        double secondLatToRad = Math.toRadians(secondLatitude);
	//Diferença entre as longitudes em radiano
        double deltaLongitudeInRad = Math.toRadians(secondLongitude - firstLongitude);
// Calculo necessário para chegar a distância entre as duas
        return   Math.acos(Math.cos(firstLatToRad)
                * Math.cos(secondLatToRad)
                * Math.cos(deltaLongitudeInRad) + Math.sin(firstLatToRad)
                * Math.sin(secondLatToRad))
                * EARTH_RADIUS_KM;
    }
}
```

## MySQL

Essa classe deverá conter todos os métodos relacionados a manutenção do banco de dados MySQL e métodos que forneçam acesso a esses recursos.

```java
public class MySQL {
    //Variavel que irá armazenar a lista de aeroportos atual
    private ArrayList<Airports> ListOfAirports;
    //Variavel que irá armazenar o usuario do banco de dados
    private String user;
    //Variaveis que irão armazenar as informações necessarias do banco de dados
    private String password;
    private String server;
    private String database;
    private MysqlDataSource dataSource;
```

Métodos constructor com diferente número de parâmetros, caso não seja informado nenhum ele irá acessar o default, usury é o root, a senha é password, o server é o [localhost](http://localhost) e o database é o airportdjakstra.

```java
public MySQL(String user,String password,String server,String database){
        ListOfAirports = new ArrayList<>(); // Create an ArrayList object
        //Informações necessarias para acessar o banco de dados
        this.user=user;
        this.password=password;
        this.server=server;
        this.database=database;
        //Seta as informações necessárias para acessar o banco de dados e chama
        // o metodo readTable para salvar a lista de aeroportos lido no Banco de
        // dados em uma collection ListOfAirports		
        dataSource = new MysqlDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setServerName(server);
        dataSource.setDatabaseName(database);
        //chama um metodo para salvar os dados em uma collection
        readTable(dataSource, ListOfAirports);
    }
public MySQL(){
        this("root","password","localhost","airportdjakstra");
    }
```

O método readTable é um método privado que irá salvar as informações obtidas por uma tabela do database em uma collection que é uma abstração para uma lista de aeroportos internacionais do Brasil, sendo estes representados por meio da classe Airports.
A tabela do banco de dados tem como colunas principais:

- “iata”: a sigla do aeroporto;
- “latitude”: a latitude do aeroporto em que o aeroporto se encontra;
- “longitude”: a longitude do aeroporto em que o aeroporto se encontra;
- “cidade”: a cidade em que o aeroporto se encontra;
- “estado”: o estado em que o aeroporto se encontra.

A partir dessas informações irá ser criado a abstração de aeroporto representado na classe Airport e essa abstração será adicionada a lista.

Em caso de erro irá ser printado o erro na tela.

```java
    //Recupera os aeroportos da tabela , e salva em uma collection
    void readTable(MysqlDataSource dataSource, ArrayList<Airports> list)
    {
        try{
						//Estabelece uma conexão com o database usando as variaveis salvas no dataSource
            Connection conn = dataSource.getConnection();
						// Cria um Statement para poder realizar alguma ação no MySQL
            Statement stmt = conn.createStatement();
						//Recebe o resultado da ação executada pelo statement
            ResultSet rs = stmt.executeQuery("SELECT iata, latitude, longitude, cidade,estado FROM airport_data");
            while (rs.next()){//enquanto tiver linha
                String sigla  = rs.getString("iata"); // recebe a sigla
                double latitude= rs.getDouble("latitude"); // recebe a latitude
                double longitude = rs.getDouble("longitude");//  recebe a longitude
                String cidade = rs.getString("cidade"); // recebe a cidade
                String  estado = rs.getString("estado");// recebe o estado

								//Cria um novo aeroporot com as informações recebeidas do banco de dados
                Airports newAirport = new Airports(sigla,latitude,longitude,cidade,estado);
                //Adiciona esse aeroporto a lista de aerorportos
								list.add(newAirport);
            }
						//Para cortar a conexão com o banco de dados
            rs.close();
            stmt.close();
            conn.close();
        }catch (Exception e){
            //Imprimir erro na tela
            System.out.println("Error>"+e);
            e.printStackTrace();
        }
    }
//Metodo para conseguir recuperar a lista de aeroportos
		public ArrayList<Airports> getListOfAirports() {
        return ListOfAirports;
	    }
```

O método “saveSearch“ usa uma tabela criada no banco de dados para salvar a origem e destino selecionados pelo usuário e o caminho decidido pelo algoritmo.

```java
public void saveSearch(String origem, String destino,String caminho){
        //No MySQL usei o comando:
        //create table Section(
        //        Origem varchar(30),
        //        Destino varchar(30),
        //        Caminho varchar(100)
        //);
        try {
						//Faz a conecção com o BD e cria uma declaração que irá ser rodada no MySQL
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
						//String que irá ter o comando em MySQL para inserir na tabela
						//a origem, o destino e o caminho
            String accessDatabase = "INSERT INTO Section" + " VALUES('"+ origem +"','"+destino+"','"+caminho+"') ";
            int result = stmt.executeUpdate(accessDatabase);
        } catch (Exception e) {
						//Imprimir o erro
            System.out.println("Error>"+e);
            e.printStackTrace();
        }
    }
}
```

## Djakstra

A classe Djakstra é uma classe que irá implementar o algoritmo de Dijkstra e irá obter o menor caminho de um aeroporto origem escolhido previamente para todos os outros aeroportos.

Os atributos irão ser a Lista de aeroportos que obtemos do banco de dados, uma matriz que irá representar a matriz de adjacência dos aeroportos sendo montada dentro do for loop, tendo como peso a distância entre o aeroporto origem e o aeroporto de destino. Distância a qual será obtida usando o método “getDistance()” da classe de Airports. 

```java
public class Djakstra {
		// Lista de aeroportos 
    private ArrayList<Airports> ListOfAirports;
		// Variavel que irá armazenar a matriz de adjacencia
    private Hashtable<String, Hashtable<String,Double> > AdjacenceMatrix;
		//Varaivel com o numero de aeroportos
    private int NumAirports;
    public Djakstra(){
				//Colocando os devidos valores em cada variavel
        ListOfAirports= new MySQL().getListOfAirports();
        AdjacenceMatrix =new Hashtable<String, Hashtable<String,Double> >();
        NumAirports=0;

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

```

Os métodos “minDistance” retornam baseados nos parâmetros a sigla ou o index de um aeroporto, tendo uma função parecida com o que é implementado em um priority queue ao retornar o índice do aeroporto mais próximo.

```java
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
```

O método “printMatrix” é um método que irá printar a matriz de adjacência no console ao ser chamado.

```java
void printMatrix(){//Printar a matriz de adjacencia
        for(Airports El : ListOfAirports){
            System.out.println(El.getSigla());
            for(Airports Te: ListOfAirports){
                System.out.print(AdjacenceMatrix.get(El.getSigla()).get(Te.getSigla())+", ");
            }
            System.out.println("---------------------");
        }
    }
```

O método “printSolution” mostra no console o caminho que deverá ser percorrido pelo avião, e tem como retorno uma string com esse caminho.

```java
// Função para printar o caminho e a distancia
    String printSolution(String sigla, Hashtable<String, String> path, Hashtable<String,Double> dist)
    {
        System.out.println("O caminho sera: "+path.get(sigla)+" e a distancia percorrida sera: " + dist.get(sigla) +" Km");
        return path.get(sigla);
    }
```

Para o algoritmo de Dijakstra propriamente dito, foi utilizado um método guloso para encontrar a solução, avaliando todos os aeroportos e pegando a minima distância para cada um deles, e depois juntando e avaliando o menor caminho geral.

```java
//Algoritmo de dijakstra propriamente dito
    String AlgorithDjakstra(String sigla1, String sigla2){ 

        Hashtable<String,Double> dist = new Hashtable<String,Double>();  // Saída com a menor distancia
        Hashtable<String,Boolean> sptSet = new Hashtable<String,Boolean>();  // Para saber se um aeroporto já foi visitado
        Hashtable<String, String> path = new Hashtable<String,String>();

        // Inicializa as distancias como infinito e que nenhum aeroporto foi visitado
        for(Airports El : ListOfAirports){
            dist.put(El.getSigla(), Double.MAX_VALUE);
            sptSet.put(El.getSigla(), false);
        }

        // Distancia para o proprio aeroporto é zero e ele começa nele mesmo
        dist.put(sigla1,0.0);
        path.put(sigla1,sigla1);

        // Algoritmo para encontrar o menor caminho para todos os vertices
        for (int count=0; count< NumAirports-1; count++) {// Tenho q colocar para ir até o anterior

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

                // Alterar o aeroporto só se ele já não foi visitado
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
        return printSolution(sigla2, path, dist);
    }
```

Unico método público da classe, servirá como interface com o usuário final, sendo chamado pela Classe Menu e retornará o caminho.

```java
//Metodo publico para rodar o algoritmo e retornar o caminho
    public String search(String origem,String destino){

        return AlgorithDjakstra(origem,destino);
    }
```

## Menu

A classe menu irá ser o meio de interação com o usuário do console, printando todas as informações necessárias para o usuário conseguir usar o software. 

```java
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
```

Metodo que irá ser inicializado, de forma a imprimir no console as mensagem necessárias para o dialogo do usuário com o software

```java
public void start(){
        //Chama o metodo para mostrar a mensagem inicial
        firstMessage();
        while(true){
            //Continuamente chama o metodo que ira mostrar a mensagem com opcoes
            continueMessage();
        }
    }
```

O método “firstMessage” que contêm a mensagem inicial do sistema, ao ser chamado irá imprimir ela, receber um parâmetro no input, e avaliar qual ação irá ser tomada com base nesse input. Se irá ser o pedido para digitar um valor novo, ou algum método da classe como “listarEstados” e “Consultar”, por exemplo.

```java
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
```

O método “continueMessage” que contêm a mensagem que sempre será impressa pelo sistema, ao ser chamado irá imprimir essa mensagem, receber um parâmetro no input, e avaliar qual ação irá ser tomada com base nesse input. Se irá ser o pedido para digitar um valor novo, ou algum método da classe como “listarEstados” e “Consultar”, por exemplo.

```java
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
```

O método “ListarAeroportos” irá imprimir uma linha para cada aeroporto de um estado contendo a sigla, o município e o estado desse aeroporto. Facilitando a consulta do usuário.

```java
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
```

O método “listarEstados” é um método que irá consultar a lista de nome de estados enquanto o usuario não escrever um correto. No momento, em que for escrito um correto, ele irá chamar o metodo “ListarAeroportos” para listar todos os aeroportos desse estado.

```java
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
```

O método consultar é onde a consulta será efetivamente feita. Irá ser requisitado do usuário os aeroportos de origem e destino, utilizando o algoritmo de Dijkstra irá ser recebido o caminho, e ele será salvo na tabela do banco de dados. Caso o usuário digite um aeroporto válido nos dois casos irá ser feito uma consulta pelo algoritmo de Dijkstra para saber qual o menor caminho de acordo com as retrições.
Porém, se o usuário em algum momento digite incorretamente alguma sigla, a lista de estados possíveis será printada e a partir da escolha de um estado esse usuário saberá a sigla e o município de todos os aeroportos desse estado, facilitando a consulta do usuário. 

```java
private void Consultar(){
        //Variaveis que irao armazenar o aeroporto de origem e de destino
        String origin,destiny;
        while(true){//Preso em loop ate escrever um aeroporto corretamente
            System.out.println("\nDigite o nome do aeroporto de origem");
            //recebe o input do usuario
            origin = input.next().toUpperCase();
            //Se tiver o aeroporto digitado acaba o loop
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
        //Chama o algoritmo de dijkstra para encontrar o caminho
        String caminho = TestDjakstra.search(origin,destiny);
        //Salva consulta no banco de dados
        mySQL.saveSearch(origin,destiny,caminho);

    }
```

Metodo que irá ser chamado para finalizar o programa.

```java
private void Finalizar(){// Finaliza o programa
        System.exit(0);
    }
}
```

# Main

Classe principal, onde o método main sera chamado e o software irá começar a ser executado.

```java
public class
Main {
    public static void main(String[] args) {
        //Variavel que ira armazenar um objeto Menu
        Menu Section = new Menu();
        //Chama o metodo start da classe menu para inicializar uma sessao
        Section.start();
    }
}
```

# Github do projeto

[GitHub - JopGomes/Aiport-Djakstra: Finds the shortest path between two airports passing through another](https://github.com/JopGomes/Aiport-Djakstra)
