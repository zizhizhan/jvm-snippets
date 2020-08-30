package com.zizhizhan.asm;

import java.io.Serializable;

public class Hello implements Serializable {

    @FunctionalInterface
    interface Inner<P, R> {
        R handle(P p);
    }

}


