package com.jbs.textgameengine.screen.utility;

public class Point {
    public float x;
    public float y;
    public float z;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point(Point copyPoint) {
        x = copyPoint.x;
        y = copyPoint.y;
        z = copyPoint.z;
    }

    public Point(float x, float y) {
        this(x, y, 0);
    }

    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
