package br.com.nakatec.mapa.domain.model;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "planet_osm_point")
public class MapPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point way;

    public MapPoint() {
    }

    public MapPoint(Long id, String name, Point way) {
        this.id = id;
        this.name = name;
        this.way = way;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getWay() {
        return way;
    }

    public void setWay(Point way) {
        this.way = way;
    }
}
