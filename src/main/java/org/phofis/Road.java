package org.phofis;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Road {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    @JsonCreator
    public static Road fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}
