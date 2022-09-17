package MySQL;

import java.sql.*;
import java.util.ArrayList;

import Airports.Airports;
import com.mysql.cj.jdbc.MysqlDataSource;
public class MySQL {
    private ArrayList<Airports> ListOfAirports;
    private String user;
    private String password;
    private String server;
    private String database;
    private MysqlDataSource dataSource;
    public MySQL(String user,String password,String server,String database){
        ListOfAirports = new ArrayList<>(); // Create an ArrayList object
        this.user=user;
        this.password=password;
        this.server=server;
        this.database=database;
        dataSource = new MysqlDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setServerName(server);
        dataSource.setDatabaseName(database);
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
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT iata, latitude, longitude, cidade,estado FROM airport_data");
            while (rs.next()){
                String sigla  = rs.getString("iata");
                double latitude= rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                String cidade = rs.getString("cidade");
                String  estado = rs.getString("estado");
                Airports newAirport = new Airports(sigla,latitude,longitude,cidade,estado);
                list.add(newAirport);
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch (Exception e){
            //Imprimir erro na tela
            System.out.println("Error>");
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
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT iata, latitude, longitude, cidade,estado FROM airport_data");
            String accessDatabase = "INSERT INTO Section" + " VALUES('"+ origem +"','"+destino+"','"+caminho+"') ";
            int result = stmt.executeUpdate(accessDatabase);
        } catch (Exception e) {
            System.out.println("Error>");
            e.printStackTrace();
        }
    }
}
