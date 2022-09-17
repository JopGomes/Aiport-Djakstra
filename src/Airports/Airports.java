package Airports;
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
