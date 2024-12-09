package br.com.nakatec.mapa.controller;

import br.com.nakatec.mapa.domain.dto.Coordinates;
import br.com.nakatec.mapa.domain.model.MapPoint;
import br.com.nakatec.mapa.service.MapService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/points")
public class MapPointController {

    private final MapService mapService;

    public MapPointController(
            MapService mapService
    ) {
        this.mapService = mapService;
    }

    @GetMapping
    public List<MapPoint> getAllPoints() {
        return mapService.getAllPoints();
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<MapPoint>> getNearbyPoints(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam double distance) throws Exception {
        return ResponseEntity.ok(mapService.findNearbyPoints(lat, lon, distance));
    }

//    @PostMapping(value = "/generate-image", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<ByteArrayResource> generateMapImage(@RequestBody Coordinates coordinates) {
//        try {
//            byte[] imageBytes = mapService.generateMapImage(coordinates.getLatitude(), coordinates.getLongitude());
//            ByteArrayResource byteArrayResource = new ByteArrayResource(imageBytes);
//            return ResponseEntity.ok()
//                    .header("Content-Disposition", "attachment; filename=map.png")
//                    .body(byteArrayResource);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body(null);
//        }
//    }
}
