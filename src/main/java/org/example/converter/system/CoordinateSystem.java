package org.example.converter.system;

public enum CoordinateSystem {

    BGOK("БГОК");

    private String name;

    private CoordinateSystem (String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
