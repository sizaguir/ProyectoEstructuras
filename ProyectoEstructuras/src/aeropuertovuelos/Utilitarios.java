package aeropuertovuelos;

import java.util.List;

public class Utilitarios {
    
     // Distribuye aeropuertos según latitud/longitud escaladas
    public static void distribuirPorCoordenadas(List<Aeropuerto> aeropuertos, double ancho, double alto) {
        if (aeropuertos.isEmpty()) return;

        // Rango de coordenadas geográficas
        double minLat = aeropuertos.stream().mapToDouble(Aeropuerto::getLatitud).min().orElse(0);
        double maxLat = aeropuertos.stream().mapToDouble(Aeropuerto::getLatitud).max().orElse(1);
        double minLon = aeropuertos.stream().mapToDouble(Aeropuerto::getLongitud).min().orElse(0);
        double maxLon = aeropuertos.stream().mapToDouble(Aeropuerto::getLongitud).max().orElse(1);

        for (Aeropuerto a : aeropuertos) {
            double x = (a.getLongitud() - minLon) / (maxLon - minLon) * ancho;
            // invertimos Y para que el norte quede arriba
            double y = (1 - (a.getLatitud() - minLat) / (maxLat - minLat)) * alto;
            a.setX(x);
            a.setY(y);
        }
    }

}
