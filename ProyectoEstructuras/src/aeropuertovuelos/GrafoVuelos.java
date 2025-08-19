package aeropuertovuelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrafoVuelos {
    private Map<Aeropuerto, List<Vuelo>> adyacencia;

    public GrafoVuelos() {
        adyacencia = new HashMap<>();
    }

    // Agregar un aeropuerto (nodo)
    public void agregarAeropuerto(Aeropuerto aeropuerto) {
        adyacencia.putIfAbsent(aeropuerto, new ArrayList<>());
    }

    // Eliminar un aeropuerto y sus vuelos asociados
    public void eliminarAeropuerto(Aeropuerto aeropuerto) {
        adyacencia.remove(aeropuerto);
        for (List<Vuelo> vuelos : adyacencia.values()) {
            vuelos.removeIf(v -> v.getDestino().equals(aeropuerto));
        }
    }

    // Agregar un vuelo (arista)
    public void agregarVuelo(Aeropuerto origen, Aeropuerto destino, double peso, String aerolinea) {
        agregarAeropuerto(origen);
        agregarAeropuerto(destino);
        adyacencia.get(origen).add(new Vuelo(origen, destino, peso, aerolinea));
    }

    // Eliminar un vuelo
    public void eliminarVuelo(Aeropuerto origen, Aeropuerto destino) {
        List<Vuelo> vuelos = adyacencia.get(origen);
        if (vuelos != null) {
            vuelos.removeIf(v -> v.getDestino().equals(destino));
        }
    }

    // Obtener todos los aeropuertos
    public Set<Aeropuerto> getAeropuertos() {
        return adyacencia.keySet();
    }

    // Obtener vuelos desde un aeropuerto
    public List<Vuelo> getVuelosDesde(Aeropuerto aeropuerto) {
        return adyacencia.getOrDefault(aeropuerto, new ArrayList<>());
    }

    // Verificar si un aeropuerto existe
    public boolean contieneAeropuerto(Aeropuerto aeropuerto) {
        return adyacencia.containsKey(aeropuerto);
    }
}

