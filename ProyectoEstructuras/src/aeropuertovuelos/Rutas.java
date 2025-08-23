package aeropuertovuelos;

import java.util.*;

public class Rutas {

    private GrafoVuelos grafo;

    public Rutas(GrafoVuelos grafo) {
        this.grafo = grafo;
    }

    /**
     * Encuentra la ruta más corta usando Dijkstra
     *
     * @param origen Aeropuerto inicial
     * @param destino Aeropuerto final
     * @return Objeto RutaResultado con la distancia y el camino
     */
    /*
    //Esta implementacion deberia reconsiderarse por la que no esta comentada en la línea
    //Explicacion: https://www.notion.so/Explicacion-de-problema-con-el-algoritmo-Dijkstra-25878dc0e08480dd81e6e19fec03183e?source=copy_link
    
    public RutaResultado dijkstra(Aeropuerto origen, Aeropuerto destino) {
        Map<Aeropuerto, Double> distancias = new HashMap<>();
        Map<Aeropuerto, Aeropuerto> predecesores = new HashMap<>();
        PriorityQueue<Aeropuerto> cola = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));

        // Inicializar distancias
        for (Aeropuerto a : grafo.getAeropuertos()) {
            distancias.put(a, Double.POSITIVE_INFINITY);
        }
        distancias.put(origen, 0.0);

        cola.add(origen);

        while (!cola.isEmpty()) {
            Aeropuerto actual = cola.poll();

            for (Vuelo vuelo : grafo.getVuelosDesde(actual)) {
                Aeropuerto vecino = vuelo.getDestino();
                double nuevaDistancia = distancias.get(actual) + vuelo.getPeso();

                if (nuevaDistancia < distancias.get(vecino)) {
                    distancias.put(vecino, nuevaDistancia);
                    predecesores.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        // Reconstruir el camino
        List<Aeropuerto> camino = new ArrayList<>();
        Aeropuerto paso = destino;
        while (paso != null) {
            camino.add(0, paso);
            paso = predecesores.get(paso);
        }

        return new RutaResultado(camino, distancias.get(destino));
    }*/

    public RutaResultado dijkstra(Aeropuerto origen, Aeropuerto destino) {
        Map<Aeropuerto, Double> distancias = new HashMap<>();
        Map<Aeropuerto, Aeropuerto> predecesores = new HashMap<>();
        Set<Aeropuerto> visitados = new HashSet<>();

        PriorityQueue<Aeropuerto> cola = new PriorityQueue<>(
                Comparator.comparingDouble(distancias::get)
        );

        for (Aeropuerto a : grafo.getAeropuertos()) {
            distancias.put(a, Double.POSITIVE_INFINITY);
        }
        distancias.put(origen, 0.0);
        cola.add(origen);

        while (!cola.isEmpty()) {
            Aeropuerto actual = cola.poll();
            if (visitados.contains(actual)) {
                continue;
            }
            visitados.add(actual);

            if (actual.equals(destino)) {
                break;
            }

            for (Vuelo vuelo : grafo.getVuelosDesde(actual)) {
                Aeropuerto vecino = vuelo.getDestino();
                if (visitados.contains(vecino)) {
                    continue;
                }

                double nuevaDist = distancias.get(actual) + vuelo.getPeso();

                if (nuevaDist < distancias.get(vecino)) {
                    distancias.put(vecino, nuevaDist);
                    predecesores.put(vecino, actual);
                    cola.add(vecino); // Reagregar para reordenar
                }
            }
        }

        // Reconstruir camino y manejar destino inalcanzable
        if (distancias.get(destino) == Double.POSITIVE_INFINITY) {
            return new RutaResultado(Collections.emptyList(), Double.POSITIVE_INFINITY);
        }

        List<Aeropuerto> camino = new ArrayList<>();
        for (Aeropuerto at = destino; at != null; at = predecesores.get(at)) {
            camino.add(at);
        }
        Collections.reverse(camino);

        return new RutaResultado(camino, distancias.get(destino));
    }

    // Clase interna para guardar el resultado de la ruta
    public static class RutaResultado {
        private List<Aeropuerto> camino;
        private double distanciaTotal;

        public RutaResultado(List<Aeropuerto> camino, double distanciaTotal) {
            this.camino = camino;
            this.distanciaTotal = distanciaTotal;
        }

        public List<Aeropuerto> getCamino() {
            return camino;
        }

        public double getDistanciaTotal() {
            return distanciaTotal;
        }

        @Override
        public String toString() {
            return "Distancia total: " + distanciaTotal + " | Camino: " + camino;
        }
    }
}

