package aeropuertovuelos;

import java.io.BufferedReader;
import java.io.File;
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
         try (PrintWriter pwA = new PrintWriter(new FileWriter(ARCHIVO_AEROPUERTOS)); //try/catch obligatorio
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
     * @return 
     */
    public static GrafoVuelos cargarDatos() {
        GrafoVuelos grafo = new GrafoVuelos();
        Map<String, Aeropuerto> mapaAeropuertos = new HashMap<>();

        // --- Leer aeropuertos ---
        File fileA = new File(ARCHIVO_AEROPUERTOS);
        if (fileA.exists()) {
            BufferedReader brA = null;
            try {
                brA = new BufferedReader(new FileReader(fileA));
                String linea;
                while ((linea = brA.readLine()) != null) {
                    linea = linea.trim();
                    if (linea.isEmpty()) continue;

                    String[] partes = linea.split(";");
                    if (partes.length >= 6 && esNumero(partes[4]) && esNumero(partes[5])) {
                        Aeropuerto a = new Aeropuerto(
                            partes[0], partes[1], partes[2], partes[3],
                            Double.parseDouble(partes[4]), Double.parseDouble(partes[5])
                        );
                        if (partes.length > 7 && esNumero(partes[6]) && esNumero(partes[7])) {
                            a.setX(Double.parseDouble(partes[6]));
                            a.setY(Double.parseDouble(partes[7]));
                        }
                        grafo.agregarAeropuerto(a);
                        mapaAeropuertos.put(a.getCodigo(), a);
                    } else {
                        System.out.println("Línea de aeropuerto vacía o incompleta ignorada: " + linea);
                    }
                }
                brA.close();
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error inesperado al leer aeropuertos.");
            }
        } else {
            System.out.println("Archivo aeropuertos.txt no encontrado, grafo vacío.");
        }

        // --- Leer vuelos ---
        File fileV = new File(ARCHIVO_VUELOS);
        if (fileV.exists()) {
            BufferedReader brV = null;
            try {
                brV = new BufferedReader(new FileReader(fileV));
                String linea;
                while ((linea = brV.readLine()) != null) {
                    linea = linea.trim();
                    if (linea.isEmpty()) continue;

                    String[] partes = linea.split(";");
                    if (partes.length >= 3 && esNumero(partes[2])) {
                        Aeropuerto origen = mapaAeropuertos.get(partes[0]);
                        Aeropuerto destino = mapaAeropuertos.get(partes[1]);
                        if (origen != null && destino != null) {
                            double peso = Double.parseDouble(partes[2]);
                            String aerolinea = partes.length > 3 ? partes[3] : null;
                            grafo.agregarVuelo(origen, destino, peso, aerolinea);
                        } else {
                            System.out.println("Vuelo con aeropuertos desconocidos ignorado: " + linea);
                        }
                    } else {
                        System.out.println("Línea de vuelo vacía o incompleta ignorada: " + linea);
                    }
                }
                brV.close();
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error inesperado al leer vuelos.");
            }
        } else {
            System.out.println("Archivo vuelos.txt no encontrado, grafo vacío.");
        }

        return grafo;
    }

    // --- Método auxiliar para validar números ---
    private static boolean esNumero(String str) {
        if (str == null || str.isEmpty()) return false;
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}

