package com.zizhizhan.util;

import java.util.Objects;

public class Key {

    private String k1;
    private int k2;
    private long k3;

    public Key(String k1, int k2, long k3) {
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return k2 == key.k2 && k3 == key.k3 && Objects.equals(k1, key.k1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(k1);
    }

    @Override
    public String toString() {
        return String.format("%s_%d_%d", k1, k2, k3);
    }
}
