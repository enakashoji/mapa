package br.com.nakatec.mapa.repository;

import br.com.nakatec.mapa.domain.model.MapPoint;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapPointRepository extends JpaRepository<MapPoint, Long> {

    @Query(value = "SELECT * FROM planet_osm_point m WHERE ST_DWithin(m.way, :point, :distance) = true", nativeQuery = true)
    List<MapPoint> findNearbyPoints(@Param("point") Point point, @Param("distance") double distance);

}
