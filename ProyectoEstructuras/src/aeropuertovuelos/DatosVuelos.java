package aeropuertovuelos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class DatosVuelos {

    private static final String ARCHIVO_AEROPUERTOS = "aeropuertos.txt";
    private static final String ARCHIVO_VUELOS = "vuelos.txt";

    /**
     * Guarda la información del grafo en archivos de texto.
     */
    public static void guardarDatos(GrafoVuelos grafo) {
         try (PrintWriter pwA = new PrintWriter(new FileWriter(ARCHIVO_AEROPUERTOS));
             PrintWriter pwV = new PrintWriter(new FileWriter(ARCHIVO_VUELOS))) {

            // Guardar aeropuertos
            for (Aeropuerto a : grafo.getAeropuertos()) {
                pwA.println(a.getCodigo() + ";" + a.getNombre() + ";" + a.getCiudad() + ";" + a.getPais() 
                             + ";" + a.getLatitud() + ";" + a.getLongitud() 
                             + ";" + a.getX() + ";" + a.getY());
            }

            // Guardar vuelos
            for (Aeropuerto origen : grafo.getAeropuertos()) {
                for (Vuelo v : grafo.getVuelosDesde(origen)) {
                    pwV.println(origen.getCodigo() + ";" + v.getDestino().getCodigo() + ";" + v.getPeso() + ";" + v.getAerolinea());
                }
            }

        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
        }
    }

    /**
     * Carga la información desde archivos y la inserta en el grafo.
     */
    public static GrafoVuelos cargarDatos() {
        GrafoVuelos grafo = new GrafoVuelos();
        Map<String, Aeropuerto> mapaAeropuertos = new HashMap<>();

        try (BufferedReader brA = new BufferedReader(new FileReader(ARCHIVO_AEROPUERTOS))) {
            String linea;
            while ((linea = brA.readLine()) != null) {
                String[] partes = linea.split(";");
                Aeropuerto a = new Aeropuerto(
                        partes[0], // código
                        partes[1], // nombre
                        partes[2], // ciudad
                        partes[3], // país
                        Double.parseDouble(partes[4]), // latitud
                        Double.parseDouble(partes[5])  // longitud
                );
                // Cargar posiciones x e y si existen
                if (partes.length > 7) {
                    a.setX(Double.parseDouble(partes[6]));
                    a.setY(Double.parseDouble(partes[7]));
                }
                grafo.agregarAeropuerto(a);
                mapaAeropuertos.put(a.getCodigo(), a);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de aeropuertos no encontrado, iniciando vacío.");
        } catch (IOException e) {
            System.err.println("Error al leer aeropuertos: " + e.getMessage());
        }

        try (BufferedReader brV = new BufferedReader(new FileReader(ARCHIVO_VUELOS))) {
            String linea;
            while ((linea = brV.readLine()) != null) {
                String[] partes = linea.split(";");
                Aeropuerto origen = mapaAeropuertos.get(partes[0]);
                Aeropuerto destino = mapaAeropuertos.get(partes[1]);
                double peso = Double.parseDouble(partes[2]);
                String aerolinea = partes.length > 3 ? partes[3] : null;
                if (origen != null && destino != null) {
                    grafo.agregarVuelo(origen, destino, peso, aerolinea);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de vuelos no encontrado, iniciando vacío.");
        } catch (IOException e) {
            System.err.println("Error al leer vuelos: " + e.getMessage());
        }

        return grafo;
    }
}
