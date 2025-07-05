package org.dacodia.afmp.voronoi;

import org.dacodia.afmp.Point;
import org.waveware.delaunator.DPoint;
import org.waveware.delaunator.Delaunator;

import java.util.List;

public class Voronoi {
    Delaunator delaunay;
    List<DPoint> points;
    int pointsN;
    Triangles cells, vertices;
    public Voronoi(Delaunator delaunay, List<DPoint> points, int pointsN) {
        this.delaunay = delaunay;
        this.points = points;
        this.pointsN = pointsN;
        cells = new Triangles();
        vertices = new Triangles();
    }
}
