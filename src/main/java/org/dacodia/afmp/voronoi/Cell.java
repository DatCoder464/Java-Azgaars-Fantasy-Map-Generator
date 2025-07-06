package org.dacodia.afmp.voronoi;

import java.util.List;

public record Cell(
        List<Integer> v,
        List<Integer> c,
        int b
) {}
