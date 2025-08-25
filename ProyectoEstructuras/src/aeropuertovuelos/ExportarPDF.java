package aeropuertovuelos;

import aeropuertovuelos.Aeropuerto;
import aeropuertovuelos.Rutas;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportarPDF {

    public static void exportarRuta(Rutas.RutaResultado resultado, String archivo)
            throws IOException, DocumentException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(archivo));
        document.open();

        document.add(new Paragraph("=== Ruta más corta ==="));
        document.add(new Paragraph("Distancia total: " + resultado.getDistanciaTotal() + " km"));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Camino:"));
        for (Aeropuerto a : resultado.getCamino()) {
            document.add(new Paragraph(" - " + a.getNombre() + " (" + a.getCodigo() + ")"));
        }

        document.close();
    }
}
