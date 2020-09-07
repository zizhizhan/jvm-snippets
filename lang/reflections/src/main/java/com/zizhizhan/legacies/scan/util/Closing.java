package com.zizhizhan.legacies.scan.util;

import java.io.IOException;
import java.io.InputStream;

public class Closing {

    private final InputStream in;

    public Closing(final InputStream in) {
        this.in = in;
    }

    public static Closing with(final InputStream in) {
        return new Closing(in);
    }

    public void f(final Closure c) throws IOException {
        if (in == null) {
            return;
        }
        try {
            c.f(in);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                throw ex;
            }
        }
    }

    public interface Closure {
        void f(InputStream in) throws IOException;
    }

}
