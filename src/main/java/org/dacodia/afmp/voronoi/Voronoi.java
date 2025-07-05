package org.dacodia.afmp.voronoi;

import org.waveware.delaunator.DPoint;
import org.waveware.delaunator.Delaunator;

import java.util.ArrayList;
import java.util.List;

public class Voronoi {
    Delaunator delaunay;
    List<DPoint> points;
    int pointsN;
    List<Cell> cells;
    public Voronoi(Delaunator delaunay, List<DPoint> points, int pointsN) {
        this.delaunay = delaunay;
        this.points = points;
        this.pointsN = pointsN;
        cells = new ArrayList<>();
        vertices = new Triangles();

        for(int e = 0; e < this.delaunay.triangles.length; e++) {

            int p = delaunay.triangles[nextHalfedge(e)];
            if(p < this.pointsN && cells.get(p) == null) {
                List<Integer> edges = edgesAroundPoint(e);
                List<Integer> v = edges.stream().map(Voronoi::triangleOfEdge).toList();
                List<Integer> c =edges.stream().map(i -> this.delaunay.triangles[i]).filter(i -> i < this.pointsN).toList();
                int b = edges.size() > c.size() ? 1 : 0;
                cells.set(p, new Cell(v, c, b));
            }

            int t = triangleOfEdge(e);

            if (!vertices.p[t]) {
                this.vertices.p[t] = this.triangleCenter(t);              // vertex: coordinates
                this.vertices.v[t] = this.trianglesAdjacentToTriangle(t); // vertex: adjacent vertices
                this.vertices.c[t] = this.pointsOfTriangle(t);            // vertex: adjacent cells
            }
        }
    }

    public static int triangleOfEdge(int e) { return Math.floorDiv(e, 3); }

    public int nextHalfedge(int e) { return (e % 3 == 2) ? e - 2 : e + 1; }

    public List<Integer> edgesAroundPoint( int start) {
    List<Integer> result = new ArrayList<>();
    int incoming = start;
    do {
        result.add(incoming);
        int outgoing = nextHalfedge(incoming);
        incoming = this.delaunay.halfedges[outgoing];
    } while (incoming != -1 && incoming != start && result.size() < 20);
    return result;
    }
}
