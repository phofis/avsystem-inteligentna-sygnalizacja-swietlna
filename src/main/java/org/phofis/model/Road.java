package org.phofis.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;

public enum Road {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    @JsonCreator
    public static Road fromString(String value) {
        return valueOf(value.toUpperCase());
    }

    public static final HashMap<Road,Integer> values = new HashMap<>();
    static {
        values.put(NORTH,0);
        values.put(EAST, 1);
        values.put(SOUTH, 2);
        values.put(WEST, 3);
    }

    // 1 = left, 2 = across, 3 = right
    public static Integer getOrientation(Road r1, Road r2){
        return (values.get(r2)-values.get(r1)+4)%4;
    }

}
