package aeropuertovuelos;

import java.util.Objects;

public class Aeropuerto {
    private String codigo;   // Ej: "PKX"
    private String nombre;   // Ej: "Aeropuerto Internacional de Daxing"
    private String ciudad;   // Ej: "Pekín"
    private String pais;     // Ej: "China"
    private double latitud;  // Opcional para visualización
    private double longitud; // Opcional para visualización

    public Aeropuerto(String codigo, String nombre, String ciudad, String pais) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    public Aeropuerto(String codigo, String nombre, String ciudad, String pais, double latitud, double longitud) {
        this(codigo, nombre, ciudad, pais);
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

    @Override
    public String toString() {
        return nombre + " (" + codigo + ") - " + ciudad + ", " + pais;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aeropuerto)) return false;
        Aeropuerto that = (Aeropuerto) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}