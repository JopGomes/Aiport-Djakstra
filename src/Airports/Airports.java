package Airports;

public class Airports {
    private String sigla;
    private double latitude;
    private double longitude;
    private String cidade;
    private String  estado;

    public Airports(String sigla, double latitude, double longitude, String cidade, String estado) {
        this.sigla = sigla;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cidade = cidade;
        this.estado = estado;
    }

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
        double firstLatitude = latitude;
        double secondLatitude = Second.getLatitude();

        double firstLongitude = longitude;
        double secondLongitude = Second.getLongitude();

        double EARTH_RADIUS_KM= 6371;
        double firstLatToRad = Math.toRadians(firstLatitude);
        double secondLatToRad = Math.toRadians(secondLatitude);
        double deltaLongitudeInRad = Math.toRadians(secondLongitude - firstLongitude);
        return   Math.acos(Math.cos(firstLatToRad)
                * Math.cos(secondLatToRad)
                * Math.cos(deltaLongitudeInRad) + Math.sin(firstLatToRad)
                * Math.sin(secondLatToRad))
                * EARTH_RADIUS_KM;
    }
}
