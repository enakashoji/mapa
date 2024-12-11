package br.com.nakatec.mapa.service;

import br.com.nakatec.mapa.domain.dto.CoordenadaDTO;
import br.com.nakatec.mapa.domain.model.MapPoint;
import br.com.nakatec.mapa.repository.MapPointRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Locale;

@Service
public class MapService {

    private final MapPointRepository repository;
    @Value("${api.key.API_KEY}")
    private String apiKey;

    public MapService(MapPointRepository repository) {
        this.repository = repository;
    }

    public List<MapPoint> findNearbyPoints(double lat, double lon, double distance) throws ParseException {
        Point point = (Point) new WKTReader().read("POINT(" + lon + " " + lat + ")");
        point.setSRID(4326);
        return repository.findNearbyPoints(point, distance);
    }

    public List<MapPoint> getAllPoints() {
        return repository.findAll();
    }

    public byte[] gerarMapaPdf(List<CoordenadaDTO> coordenadas) throws Exception {
        String polyline = encodePolyline(coordenadas);

        String url = String.format(
                Locale.US,
                "https://maps.googleapis.com/maps/api/staticmap?size=2048x2048&path=enc:%s&key=%s",
                polyline, apiKey
        );

        BufferedImage mapa = ImageIO.read(new URL(url));

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(mapa, "png", outputStream);

            contentStream.drawImage(
                    PDImageXObject.createFromByteArray(document, outputStream.toByteArray(), "mapa"),
                    50, 400, 500, 500
            );

            contentStream.close();
            ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
            document.save(pdfStream);
            return pdfStream.toByteArray();
        }
    }

    public static String encodePolyline(List<CoordenadaDTO> coordenadas) {
        StringBuilder polyline = new StringBuilder();

        int prevLat = 0;
        int prevLng = 0;

        for (CoordenadaDTO coord : coordenadas) {
            int lat = (int) (coord.getLat() * 1E5);
            int lng = (int) (coord.getLng() * 1E5);

            int dLat = lat - prevLat;
            int dLng = lng - prevLng;

            encode(dLat, polyline);
            encode(dLng, polyline);

            prevLat = lat;
            prevLng = lng;
        }

        return polyline.toString();
    }

    private static void encode(int num, StringBuilder polyline) {
        num = num < 0 ? ~(num << 1) : (num << 1);

        while (num >= 0x20) {
            polyline.append((char) ((0x20 | (num & 0x1f)) + 63));
            num >>= 5;
        }

        polyline.append((char) (num + 63));
    }
}
