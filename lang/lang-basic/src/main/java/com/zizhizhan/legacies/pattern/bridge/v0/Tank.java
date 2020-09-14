package com.zizhizhan.legacies.pattern.bridge.v0;

public class Tank {

    Drawer drawer;
    int x, y, hei, wid;
    String name;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void draw() {
        drawer.drawRect(x, y, wid, hei);
    }

}
