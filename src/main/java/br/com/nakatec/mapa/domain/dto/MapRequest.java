package br.com.nakatec.mapa.domain.dto;

public class MapRequest {
    private double latitude;
    private double longitude;
    private String name;
    private int zoom;

    public MapRequest(double latitude, double longitude, String name, int zoom) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.zoom = zoom;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }


}
