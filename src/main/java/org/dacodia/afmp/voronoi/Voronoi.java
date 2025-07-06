package org.dacodia.afmp.voronoi;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.waveware.delaunator.DPoint;
import org.waveware.delaunator.Delaunator;

import java.util.ArrayList;
import java.util.List;

public class Voronoi {
    Delaunator delaunay;
    List<DPoint> points;
    int pointsN;
    List<Cell> cells;
    List<Vertice> vertices;
    public Voronoi(Delaunator delaunay, List<DPoint> points, int pointsN) {
        this.delaunay = delaunay;
        this.points = points;
        this.pointsN = pointsN;
        cells = new ArrayList<>();
        vertices = new ArrayList<>();

        for(int e = 0; e < this.delaunay.triangles.length; e++) {

            int n = delaunay.triangles[nextHalfedge(e)];
            if(n < this.pointsN && cells.get(n) == null) {
                List<Integer> edges = edgesAroundPoint(e);
                List<Integer> v = edges.stream().map(Voronoi::triangleOfEdge).toList();
                List<Integer> c =edges.stream().map(i -> this.delaunay.triangles[i]).filter(i -> i < this.pointsN).toList();
                int b = edges.size() > c.size() ? 1 : 0;
                cells.set(n, new Cell(v, c, b));
            }

            int t = triangleOfEdge(e);

            if (vertices.get(t) == null) {
                DPoint[] c = pointsOfTriangle(t);            // vertex: adjacent cells
                DPoint p = circumcenter(c[0], c[1], c[2]);              // vertex: coordinates
                int[] v = new int[3]; // vertex: adjacent vertices
                int t3 = t * 3;
                for(int i = 0; i < 3; i++) {
                    v[i] = triangleOfEdge(delaunay.halfedges[t3 + i]);
                }
                vertices.add(new Vertice(p, v[0], v[1], v[2], c[0], c[1], c[2]));
            }
        }
    }

    public List<Cell> getCells() {
        return cells;
    }

    public List<Vertice> getVertices() {
        return vertices;
    }

    public DPoint[] pointsOfTriangle(int t) {
        int t3 = t * 3;
        return new DPoint[] {points.get(delaunay.triangles[t3]), points.get(delaunay.triangles[t3 + 1]), points.get(delaunay.triangles[t3 + 2])};
    }

    public DPoint circumcenter(DPoint a, DPoint b, DPoint c) {
    double ax = a.x;
    double ay = a.y;
    double bx = b.x;
    double by = b.y;
    double cx = c.x;
    double cy = c.y;
    double ad = ax * ax + ay * ay;
    double bd = bx * bx + by * by;
    double cd = cx * cx + cy * cy;
    double D = 2 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by));
    return new DPoint(
            Math.floor(1 / D * (ad * (by - cy) + bd * (cy - ay) + cd * (ay - by))),
            Math.floor(1 / D * (ad * (cx - bx) + bd * (ax - cx) + cd * (bx - ax)))
    );
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
