package aeropuertovuelos;

import java.util.*;

public class Rutas {

    private GrafoVuelos grafo;

    public Rutas(GrafoVuelos grafo) {
        this.grafo = grafo;
    }

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

