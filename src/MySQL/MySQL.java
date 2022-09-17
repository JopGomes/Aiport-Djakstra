package MySQL;

import java.sql.*;
import java.util.ArrayList;

import Airports.Airports;
import com.mysql.cj.jdbc.MysqlDataSource;
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
        this("root","02136021qQ@","localhost","airportdjakstra");
    }

    public ArrayList<Airports> getListOfAirports() {
        return ListOfAirports;
    }
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
