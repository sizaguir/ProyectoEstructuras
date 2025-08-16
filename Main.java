package aeropuertovuelos;

public class Main {
    public static void main(String[] args) {
        
        //️⃣ Cargar datos existentes (o vacío si no hay archivos)
        GrafoVuelos grafo = DatosVuelos.cargarDatos();
        System.out.println("? Datos cargados desde archivo.");
        
        //Agregar aeropuertos si no existen
        Aeropuerto daxing = new Aeropuerto("PKX", "Aeropuerto Internacional de Daxing", "Pekín", "China", 39.5098, 116.4106);
        Aeropuerto jfk = new Aeropuerto("JFK", "Aeropuerto Internacional John F. Kennedy", "Nueva York", "Estados Unidos", 40.6413, -73.7781);
        Aeropuerto heathrow = new Aeropuerto("LHR", "Aeropuerto de Heathrow", "Londres", "Reino Unido", 51.4700, -0.4543);

        grafo.agregarAeropuerto(daxing);
        grafo.agregarAeropuerto(jfk);
        grafo.agregarAeropuerto(heathrow);

        // 3️⃣ Agregar vuelos
        grafo.agregarVuelo(daxing, jfk, 10800, "Air China"); // Distancia ficticia en km
        grafo.agregarVuelo(daxing, heathrow, 9200, "British Airways");
        grafo.agregarVuelo(jfk, heathrow, 5560, "Delta");

        // 4️⃣ Mostrar todos los aeropuertos
        System.out.println("\n Lista de Aeropuertos:");
        for (Aeropuerto a : grafo.getAeropuertos()) {
            System.out.println(a);
        }

        // 5️⃣ Mostrar vuelos desde cada aeropuerto
        System.out.println("\n Lista de Vuelos:");
        for (Aeropuerto a : grafo.getAeropuertos()) {
            for (Vuelo v : grafo.getVuelosDesde(a)) {
                System.out.println(v);
            }
        }

        // 6️⃣ Probar ruta más corta usando Dijkstra
        Rutas rutas = new Rutas(grafo);
        System.out.println("\n? Ruta más corta de PKX a LHR:");
        Rutas.RutaResultado resultado = rutas.dijkstra(daxing, heathrow);
        System.out.println(resultado);

        // 7️⃣ Eliminar un vuelo
        System.out.println("\n Eliminando vuelo PKX → LHR...");
        grafo.eliminarVuelo(daxing, heathrow);

        // Mostrar vuelos después de eliminar
        System.out.println("\n Vuelos después de eliminar:");
        for (Aeropuerto a : grafo.getAeropuertos()) {
            for (Vuelo v : grafo.getVuelosDesde(a)) {
                System.out.println(v);
            }
        }

        // 8️⃣ Eliminar un aeropuerto
        System.out.println("\n Eliminando aeropuerto JFK...");
        grafo.eliminarAeropuerto(jfk);

        // Mostrar aeropuertos después de eliminar
        System.out.println("\n Aeropuertos después de eliminar:");
        for (Aeropuerto a : grafo.getAeropuertos()) {
            System.out.println(a);
        }

        // 9️⃣ Guardar cambios en archivo
        DatosVuelos.guardarDatos(grafo);
        System.out.println("\n Datos guardados correctamente.");
    }
}
