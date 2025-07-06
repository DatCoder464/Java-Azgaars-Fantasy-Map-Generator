package org.dacodia.afmp;

import org.dacodia.afmp.voronoi.Cell;
import org.dacodia.afmp.voronoi.Vertice;
import org.dacodia.afmp.voronoi.Voronoi;
import org.waveware.delaunator.DPoint;
import org.waveware.delaunator.Delaunator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Grid {
    double spacing;
    int cellsX, cellsY;
    long cellsDesired, seed;
    List<DPoint> boundary, points;
    List<Cell> cells;
    List<Vertice> vertices;

    //generate grid
    public Grid(int seed, long numPoints, long graphWidth, long graphHeight) {
        this.seed = seed;
        cellsDesired = numPoints;

        spacing = (double) Math.round(Math.sqrt((double) (graphWidth * graphHeight) / cellsDesired) * 100) / 100;

        boundary = getBoundaryPoints(graphWidth, graphHeight, spacing);
        points = getJitteredGrid(graphWidth, graphHeight, spacing);
        cellsX = (int) Math.floor((graphWidth + 0.5 * spacing - 1e-10) / spacing);
        cellsY = (int) Math.floor((graphHeight + 0.5 * spacing - 1e-10) / spacing);

        List<DPoint> allPoints = Stream.concat(points.stream(), boundary.stream()).toList();
        Delaunator delaunay = new Delaunator(allPoints);

        Voronoi voronoi = new Voronoi(delaunay, allPoints, points.size());

        cells = voronoi.getCells();
        vertices = voronoi.getVertices();
    }

    private List<DPoint> getJitteredGrid(long width, long height, double spacing) {
        double radius = spacing / 2; // square radius
        double jittering = radius * 0.9; // max deviation
        double doubleJittering = jittering * 2;
        Supplier<Double> jitter = () -> Math.random() * doubleJittering - jittering;

        List<DPoint> points = new ArrayList<>();
        for (double y = radius; y < height; y += spacing) {
            for (double x = radius; x < width; x += spacing) {
                double xj = Math.min(Math.round((x + jitter.get()) * 100) / 100, width);
                double yj = Math.min(Math.round((y + jitter.get()) * 100) / 100, height);
                points.add(new DPoint(xj, yj));
            }
        }
        return points;
    }

    private List<DPoint> getBoundaryPoints(long width, long height, double spacing) {
        long offset = Math.round(-spacing);
        double bSpacing = spacing * 2;
        long w = width - offset * 2;
        long h = height - offset * 2;
        int numberX = (int) (Math.ceil(w / bSpacing) - 1);
        int numberY = (int) (Math.ceil(h / bSpacing) - 1);
        List<DPoint> points = new ArrayList<>(numberX);

        for(int i = 0; i < numberX; i++) {
            double x = Math.ceil((double) ((w * i) + (w >> 2)) / numberX + offset);
            points.add(new DPoint(x, offset));
            points.add(new DPoint(x, offset + h));
        }

        for(int i = 0; i < numberY; i++) {
            double y = Math.ceil((double) ((h * i) + (h >> 2)) / numberY + offset);
            points.add(new DPoint(offset, y));
            points.add(new DPoint(offset + w, y));
        }

        return points;
    }
}
