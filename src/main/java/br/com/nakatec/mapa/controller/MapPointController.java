package br.com.nakatec.mapa.controller;

import br.com.nakatec.mapa.domain.dto.CoordenadaDTO;
import br.com.nakatec.mapa.domain.model.MapPoint;
import br.com.nakatec.mapa.service.MapService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/gerar")
    public ResponseEntity<byte[]> gerarMapa(@RequestBody List<CoordenadaDTO> coordenadas) throws Exception {
        byte[] pdf = mapService.gerarMapaPdf(coordenadas);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mapa.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}
