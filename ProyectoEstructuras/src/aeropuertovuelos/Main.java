package aeropuertovuelos;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        
        // Cargar datos existentes
        GrafoVuelos grafo = DatosVuelos.cargarDatos();
        System.out.println("Datos cargados desde archivo.");
        
        // Agregar aeropuertos de prueba si no existen
        Aeropuerto daxing = new Aeropuerto("PKX", "Aeropuerto Internacional de Daxing", "Pekin", "China", 39.5098, 116.4106);
        Aeropuerto jfk = new Aeropuerto("JFK", "Aeropuerto Internacional John F. Kennedy", "Nueva York", "Estados Unidos", 40.6413, -73.7781);
        Aeropuerto heathrow = new Aeropuerto("LHR", "Aeropuerto de Heathrow", "Londres", "Reino Unido", 51.4700, -0.4543);
        Aeropuerto cdg = new Aeropuerto("CDG", "Aeropuerto de Paris-Charles de Gaulle", "Paris", "Francia", 49.0097, 2.5478);
        Aeropuerto dxb = new Aeropuerto("DXB", "Aeropuerto Internacional de Dubai", "Dubai", "Emiratos arabes", 25.2532, 55.3657);

        grafo.agregarAeropuerto(daxing);
        grafo.agregarAeropuerto(jfk);
        grafo.agregarAeropuerto(heathrow);
        grafo.agregarAeropuerto(cdg);
        grafo.agregarAeropuerto(dxb);

        // Agregar vuelos
        grafo.agregarVuelo(daxing, jfk, 10800, "Air China");
        grafo.agregarVuelo(daxing, heathrow, 9200, "British Airways");
        grafo.agregarVuelo(jfk, heathrow, 5560, "Delta");
        grafo.agregarVuelo(heathrow, cdg, 350, "Air France");
        grafo.agregarVuelo(cdg, dxb, 5200, "Emirates");
        grafo.agregarVuelo(dxb, daxing, 5800, "Emirates");

        // Mostrar aeropuertos
        System.out.println("\nLista de Aeropuertos:");
        for (Aeropuerto a : grafo.getAeropuertos()) {
            System.out.println(" - " + a);
        }

        // Mostrar vuelos
        System.out.println("\nLista de Vuelos:");
        for (Aeropuerto a : grafo.getAeropuertos()) {
            for (Vuelo v : grafo.getVuelosDesde(a)) {
                System.out.println(" - " + v);
            }
        }

        // Pruebas estadisticas
        System.out.println("\nESTADISTICAS DEL GRAFO:");
        
        // Número de conexiones por aeropuerto
        System.out.println("\nConexiones por aeropuerto:");
        for (Aeropuerto a : grafo.getAeropuertos()) {
            int conexiones = grafo.getNumConexiones(a);
            System.out.println(" - " + a.getCodigo() + ": " + conexiones + " vuelos salientes");
        }
        
        // Aeropuerto mas conectado
        Aeropuerto masConectado = grafo.getAeropuertoMasConectado();
        System.out.println("\nAeropuerto mas conectado: " + 
            masConectado.getCodigo() + " con " + 
            grafo.getNumConexiones(masConectado) + " conexiones");
        
        // Aeropuerto menos conectado
        Aeropuerto menosConectado = grafo.getAeropuertoMenosConectado();
        System.out.println("Aeropuerto menos conectado: " + 
            menosConectado.getCodigo() + " con " + 
            grafo.getNumConexiones(menosConectado) + " conexiones");

        // Prueba de dijkstra (RUTAS)
        System.out.println("\nPRUEBAS DE RUTAS:");
        Rutas rutas = new Rutas(grafo);
        
        // Ruta existente
        System.out.println("\nRuta PKX -> CDG:");
        Rutas.RutaResultado resultado1 = rutas.dijkstra(daxing, cdg);
        if (resultado1.getDistanciaTotal() != Double.POSITIVE_INFINITY) {
            System.out.println("Distancia: " + resultado1.getDistanciaTotal() + " km");
            System.out.println("Camino: " + resultado1.getCamino());
        } else {
            System.out.println("No hay ruta disponible");
        }
        
        // Ruta inalcanzable (deberia manejarse correctamente, es decir caer en el else)
        Aeropuerto aislado = new Aeropuerto("ISO", "Aeropuerto Aislado", "Isla", "Desconocido");
        grafo.agregarAeropuerto(aislado);
        
        System.out.println("\nRuta PKX -> ISO (inalcanzable):");
        Rutas.RutaResultado resultado2 = rutas.dijkstra(daxing, aislado);
        if (resultado2.getDistanciaTotal() != Double.POSITIVE_INFINITY) {
            System.out.println("Distancia: " + resultado2.getDistanciaTotal() + " km");
            System.out.println("Camino: " + resultado2.getCamino());
        } else {
            System.out.println("Correcto: No hay ruta disponible");
        }

        // Pruebas desde DAXING
        System.out.println("\nTODAS LAS RUTAS DESDE DAXING (PKX):");
        Map<Aeropuerto, Rutas.RutaResultado> todasRutas = grafo.todasLasRutasDesde(daxing);
        
        for (Map.Entry<Aeropuerto, Rutas.RutaResultado> entry : todasRutas.entrySet()) {
            Aeropuerto destino = entry.getKey();
            Rutas.RutaResultado ruta = entry.getValue();
            
            if (ruta.getDistanciaTotal() != Double.POSITIVE_INFINITY) {
                System.out.println("-> " + destino.getCodigo() + ": " + 
                    ruta.getDistanciaTotal() + " km");
            }
        }

        // Pruebas CRUD
        System.out.println("\nPRUEBAS CRUD:");
        
        // Eliminar un vuelo
        System.out.println("\nEliminando vuelo PKX -> LHR...");
        grafo.eliminarVuelo(daxing, heathrow);
        System.out.println("Vuelo eliminado");

        // Eliminar un aeropuerto
        System.out.println("\nEliminando aeropuerto JFK...");
        grafo.eliminarAeropuerto(jfk);
        System.out.println("Aeropuerto eliminado");

        // Vuelos de aerolinea
        System.out.println("\nVuelos por aerolinea:");
        System.out.println("Emirates: " + grafo.getVuelosPorAerolinea("Emirates").size() + " vuelos");
        System.out.println("Air China: " + grafo.getVuelosPorAerolinea("Air China").size() + " vuelos");

        // Guardar Cambios
        DatosVuelos.guardarDatos(grafo);
        System.out.println("\nDatos guardados correctamente.");
    }
}