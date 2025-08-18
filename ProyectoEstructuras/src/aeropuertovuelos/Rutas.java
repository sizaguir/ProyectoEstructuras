package aeropuertovuelos;

import java.util.*;

public class Rutas {

    private GrafoVuelos grafo;

    public Rutas(GrafoVuelos grafo) {
        this.grafo = grafo;
    }

    /**
     * Encuentra la ruta más corta usando Dijkstra
     * @param origen Aeropuerto inicial
     * @param destino Aeropuerto final
     * @return Objeto RutaResultado con la distancia y el camino
     */
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

