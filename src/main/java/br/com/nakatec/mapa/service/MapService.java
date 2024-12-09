package br.com.nakatec.mapa.service;

import br.com.nakatec.mapa.domain.dto.MapRequest;
import br.com.nakatec.mapa.domain.model.MapPoint;
import br.com.nakatec.mapa.repository.MapPointRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class MapService {

    private final MapPointRepository repository;

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
}
