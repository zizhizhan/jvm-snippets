package com.zizhizhan.notes.script;

class Param {
    private long length;
    private long width;
    private long height;

    public Param(long l, long w, long h) {
        length = l;
        width = w;
        height = h;
    }

    long getLength() {
        return length;
    }

    void setLength(long length) {
        this.length = length;
    }

    long getWidth() {
        return width;
    }

    void setWidth(long width) {
        this.width = width;
    }

    long getHeight() {
        return height;
    }

    void setHeight(long height) {
        this.height = height;
    }

    public String toString() {
        return "(" + length + ", " + width + ", " + height + ")";
    }
}
