package org.dacodia.afmp.voronoi;

import org.apache.commons.lang3.tuple.Triple;
import org.waveware.delaunator.DPoint;

public record Vertice(
        DPoint circumcenter,
        int edge1,
        int edge2,
        int edge3,
        DPoint adj1,
        DPoint adj2,
        DPoint adj3
) {}
