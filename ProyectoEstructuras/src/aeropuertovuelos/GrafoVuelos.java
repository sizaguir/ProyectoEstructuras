package aeropuertovuelos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

public class GrafoVuelos implements Serializable {

    private Map<Aeropuerto, List<Vuelo>> vuelos;
    private Set<Aeropuerto> aeropuertos;

    // Árbol de búsqueda por aerolínea
    private TreeMap<String, List<Vuelo>> vuelosPorAerolinea;

    public GrafoVuelos() {
        this.aeropuertos = new HashSet<>();
        this.vuelos = new HashMap<>();
        this.vuelosPorAerolinea = new TreeMap<>();
    }

    // Agregar un aeropuerto (nodo)         
    public void agregarAeropuerto(Aeropuerto a) {
        if (!aeropuertos.contains(a)) {
            aeropuertos.add(a);

            // inicializar lista vacía en el mapa
            if (!vuelos.containsKey(a)) {
                vuelos.put(a, new ArrayList<>());
            }
        }
    }

    // Eliminar un aeropuerto y sus vuelos asociados      
    public void eliminarAeropuerto(Aeropuerto a) {
        aeropuertos.remove(a);
        vuelos.remove(a);

        // eliminar también vuelos que lo usaban como destino
        for (List<Vuelo> lista : vuelos.values()) {
            Iterator<Vuelo> it = lista.iterator();
            while (it.hasNext()) {
                Vuelo v = it.next();
                if (v.getDestino().equals(a)) {
                    it.remove();
                }
            }
        }
        // limpiar vuelos por aerolínea
        for (List<Vuelo> lista : vuelosPorAerolinea.values()) {
            Iterator<Vuelo> it = lista.iterator();
            while (it.hasNext()) {
                Vuelo v = it.next();
                if (v.getOrigen().equals(a) || v.getDestino().equals(a)) {
                    it.remove();
                }
            }
        }
    }

    // Agregar un vuelo (arista)    
    public void agregarVuelo(Aeropuerto origen, Aeropuerto destino, double peso, String aerolinea) {
        Vuelo nuevo = new Vuelo(origen, destino, peso, aerolinea);
        // agregar al mapa de vuelos por origen
        if (!vuelos.containsKey(origen)) {
            vuelos.put(origen, new ArrayList<>());
        }
        vuelos.get(origen).add(nuevo);

        // agregar al mapa de vuelos por aerolínea
        if (!vuelosPorAerolinea.containsKey(aerolinea)) {
            vuelosPorAerolinea.put(aerolinea, new ArrayList<>());
        }
        vuelosPorAerolinea.get(aerolinea).add(nuevo);
    }

    // Eliminar un vuelo    
    public void eliminarVuelo(Aeropuerto origen, Aeropuerto destino) {
        if (vuelos.containsKey(origen)) {
            List<Vuelo> lista = vuelos.get(origen);

            Iterator<Vuelo> it = lista.iterator();
            while (it.hasNext()) {
                Vuelo v = it.next();
                if (v.getDestino().equals(destino)) {
                    it.remove();

                    // eliminar también de vuelosPorAerolinea
                    List<Vuelo> listaAerolinea = vuelosPorAerolinea.get(v.getAerolinea());
                    if (listaAerolinea != null) {
                        listaAerolinea.remove(v);
                    }
                }
            }
        }
    }

    // Obtener todos los aeropuertos
    public Set<Aeropuerto> getAeropuertos() {
        return aeropuertos;
    }

    // Obtener vuelos desde un aeropuerto    
    public List<Vuelo> getVuelosDesde(Aeropuerto a) {
        if (vuelos.containsKey(a)) {
            return vuelos.get(a);
        }
        return Collections.emptyList();
    }

    // Verificar si un aeropuerto existe
    // Verificar si un aeropuerto existe en el grafo
    public boolean contieneAeropuerto(Aeropuerto aeropuerto) {
        return aeropuertos.contains(aeropuerto);
    }

    // Obtener una lista con todos los vuelos del grafo
    public List<Vuelo> getTodosLosVuelos() {
        List<Vuelo> todos = new ArrayList<>();

        for (List<Vuelo> lista : vuelos.values()) {
            todos.addAll(lista);
        }

        return todos;
    }

    public List<Vuelo> getVuelosPorAerolinea(String aerolinea) {
        if (vuelosPorAerolinea.containsKey(aerolinea)) {
            return new ArrayList<>(vuelosPorAerolinea.get(aerolinea));
            // se devuelve una copia para evitar modificar la lista interna
        }
        return Collections.emptyList();
    }

    // Verificar si existe un vuelo entre dos aeropuertos
    public boolean existeVuelo(Aeropuerto origen, Aeropuerto destino) {
        if (!vuelos.containsKey(origen)) {
            return false; // no hay vuelos desde ese origen
        }

        List<Vuelo> lista = vuelos.get(origen);
        for (Vuelo v : lista) {
            if (v.getDestino().equals(destino)) {
                return true; // encontrado
            }
        }
        return false; // no se encontró
    }

    // Editar un vuelo (modifica peso y aerolínea)
    public boolean editarVuelo(Aeropuerto origen, Aeropuerto destino, double nuevoPeso, String nuevaAerolinea) {
        if (!vuelos.containsKey(origen)) {
            return false; // No hay vuelos desde ese origen
        }
        List<Vuelo> lista = vuelos.get(origen);
        for (Vuelo v : lista) {
            if (v.getDestino().equals(destino)) {
                // --- Actualizar valores ---
                String aerolineaAnterior = v.getAerolinea();

                v.setPeso(nuevoPeso);
                v.setAerolinea(nuevaAerolinea);

                // --- Actualizar índice de vuelosPorAerolinea ---
                // Eliminar de la lista de la aerolínea anterior
                if (vuelosPorAerolinea.containsKey(aerolineaAnterior)) {
                    vuelosPorAerolinea.get(aerolineaAnterior).remove(v);
                }

                // Agregar a la nueva aerolínea
                if (!vuelosPorAerolinea.containsKey(nuevaAerolinea)) {
                    vuelosPorAerolinea.put(nuevaAerolinea, new ArrayList<>());
                }
                vuelosPorAerolinea.get(nuevaAerolinea).add(v);

                return true; // Vuelo editado correctamente
            }
        }
        return false; // No se encontró vuelo entre origen y destino
    }

    // Simular los itinerarios de vuelos en orden de salida usando PriorityQueue
    public void simularItinerarios() {
        // Cola de prioridad por hora de salida
        PriorityQueue<Vuelo> cola = new PriorityQueue<>(Comparator.comparingInt(Vuelo::getHoraSalida));

        // Agregar todos los vuelos a la cola
        for (Vuelo v : getTodosLosVuelos()) {
            cola.add(v);
        }

        System.out.println("=== Simulación de Itinerarios ===");
        while (!cola.isEmpty()) {
            Vuelo vuelo = cola.poll();
            System.out.println("Procesando vuelo: " + vuelo);
        }
    }

    // Número de conexiones (vuelos salientes) por aeropuerto
    public int getNumConexiones(Aeropuerto a) {
        return vuelos.getOrDefault(a, Collections.emptyList()).size();
    }

    // FUNCIONES ESTADISTICAS
    // Aeropuerto más conectado
    public Aeropuerto getAeropuertoMasConectado() {
        return aeropuertos.stream()
                .max(Comparator.comparingInt(this::getNumConexiones))
                .orElse(null);
    }

    // Aeropuerto menos conectado  
    public Aeropuerto getAeropuertoMenosConectado() {
        return aeropuertos.stream()
                .min(Comparator.comparingInt(this::getNumConexiones))
                .orElse(null);
    }

    // Todas las rutas más cortas desde un origen (para estadísticas)
    public Map<Aeropuerto, Rutas.RutaResultado> todasLasRutasDesde(Aeropuerto origen) {
        Map<Aeropuerto, Rutas.RutaResultado> resultados = new HashMap<>();
        Rutas rutas = new Rutas(this);

        for (Aeropuerto destino : aeropuertos) {
            if (!origen.equals(destino)) {
                resultados.put(destino, rutas.dijkstra(origen, destino));
            }
        }

        return resultados;
    }

}
