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
    
    public List<Vuelo> getTodosLosVuelos() {
    List<Vuelo> todos = new ArrayList<>();
    for (Aeropuerto origen : adyacencia.keySet()) {
        todos.addAll(adyacencia.get(origen));
    }
    return todos;
}

        // Verificar si existe un vuelo entre dos aeropuertos
    public boolean existeVuelo(Aeropuerto origen, Aeropuerto destino) {
        List<Vuelo> vuelos = adyacencia.get(origen);
        if (vuelos != null) {
            for (Vuelo v : vuelos) {
                if (v.getDestino().equals(destino)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Editar un vuelo (modifica peso y aerolínea)
    public boolean editarVuelo(Aeropuerto origen, Aeropuerto destino, double nuevoPeso, String nuevaAerolinea) {
        List<Vuelo> vuelos = adyacencia.get(origen);
        if (vuelos != null) {
            for (Vuelo v : vuelos) {
                if (v.getDestino().equals(destino)) {
                    v.setPeso(nuevoPeso);
                    v.setAerolinea(nuevaAerolinea);
                    return true; // Editado con éxito
                }
            }
        }
        return false; // No se encontró el vuelo
    }
    
    

}

