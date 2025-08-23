package aeropuertovuelos;

import java.io.Serializable;
import java.util.Objects;

public class Vuelo implements Serializable {

    private Aeropuerto origen;
    private Aeropuerto destino;
    private double peso; // Puede ser distancia en km, costo o tiempo en horas
    private String aerolinea;
    private int horaSalida;  // en minutos desde 00:00 (ej: 480 = 8:00 am)
    private int horaLlegada;

    public Vuelo(Aeropuerto origen, Aeropuerto destino, double peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    public Vuelo(Aeropuerto origen, Aeropuerto destino, double peso, String aerolinea) {
        this.origen = Objects.requireNonNull(origen);
        this.destino = Objects.requireNonNull(destino);
        this.peso = peso;
        this.aerolinea = Objects.requireNonNull(aerolinea);
        this.horaSalida = 0;
        this.horaLlegada = 0;
    }

    public Vuelo(Aeropuerto origen, Aeropuerto destino, double peso, String aerolinea, int horaSalida, int horaLlegada) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.aerolinea = aerolinea;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
    }

    // Getters y setters
    public Aeropuerto getOrigen() {
        return origen;
    }

    public void setOrigen(Aeropuerto origen) {
        this.origen = origen;
    }

    public Aeropuerto getDestino() {
        return destino;
    }

    public void setDestino(Aeropuerto destino) {
        this.destino = destino;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(String aerolinea) {
        this.aerolinea = aerolinea;
    }

    public int getHoraSalida() {
        return horaSalida;
    }

    public int getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraSalida(int horaSalida) {
        this.horaSalida = horaSalida;
    }

    public void setHoraLlegada(int horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

//    @Override
//    public String toString() {
//        return origen.getCodigo() + " → " + destino.getCodigo() + " | " + peso + " | " + aerolinea;
//    }
    @Override
    public String toString() {
        return origen.getCodigo() + " -> " + destino.getCodigo() + " | " + aerolinea + " | Salida: " + formatearHora(horaSalida) + " | Llegada: " + formatearHora(horaLlegada);
    }

    private String formatearHora(int minutos) {
        int h = minutos / 60;
        int m = minutos % 60;
        return String.format("%02d:%02d", h, m);
    }
}
