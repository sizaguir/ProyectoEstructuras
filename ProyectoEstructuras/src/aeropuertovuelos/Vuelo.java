package aeropuertovuelos;

public class Vuelo {
    private Aeropuerto origen;
    private Aeropuerto destino;
    private double peso; // Puede ser distancia en km, costo o tiempo en horas
    private String aerolinea;

    public Vuelo(Aeropuerto origen, Aeropuerto destino, double peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    public Vuelo(Aeropuerto origen, Aeropuerto destino, double peso, String aerolinea) {
        this(origen, destino, peso);
        this.aerolinea = aerolinea;
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

    @Override
    public String toString() {
        return origen.getCodigo() + " → " + destino.getCodigo() + " | " + peso + " | " + aerolinea;
    }
}
