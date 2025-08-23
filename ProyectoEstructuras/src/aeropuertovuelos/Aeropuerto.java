package aeropuertovuelos;

import java.io.Serializable;
import java.util.Objects;

public class Aeropuerto implements Serializable{
    private String codigo;   // Ej: "PKX"
    private String nombre;   // Ej: "Aeropuerto Internacional de Daxing"
    private String ciudad;   // Ej: "Pekín"
    private String pais;     // Ej: "China"
    private double latitud;  // Opcional para visualización
    private double longitud; // Opcional para visualización
    // Coordenadas para dibujar en el grafo
    private double x;
    private double y;

    public Aeropuerto(String codigo, String nombre, String ciudad, String pais) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
        this.x=0.0;
        this.y=0.0;
    }

    public Aeropuerto(String codigo, String nombre, String ciudad, String pais, double latitud, double longitud) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y setters
    public String getCodigo() { 
        return codigo; 
    }
    public void setCodigo(String codigo) { 
        this.codigo = codigo; 
    }

    public String getNombre() { 
        return nombre; 
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getCiudad() { 
        return ciudad; 
    }
    public void setCiudad(String ciudad) { 
        this.ciudad = ciudad; 
    }

    public String getPais() { 
        return pais; 
    }
    public void setPais(String pais) { 
        this.pais = pais; 
    }

    public double getLatitud() { 
        return latitud; 
    }
    public void setLatitud(double latitud) { 
        this.latitud = latitud; 
    }

    public double getLongitud() { 
        return longitud; 
    }
    public void setLongitud(double longitud) { 
        this.longitud = longitud; 
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    @Override
    public String toString() {
        return nombre + " (" + codigo + ") - " + ciudad + ", " + pais;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aeropuerto)) return false;
        Aeropuerto other = (Aeropuerto) o;
        return codigo.equals(other.codigo);        
    }   
    
    @Override
    public int hashCode() {
        return codigo.hashCode();
    }
}