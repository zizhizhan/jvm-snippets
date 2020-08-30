package com.zizhizhan.interview.asm;

import java.io.Serializable;

public class Hello implements Serializable {

    @FunctionalInterface
    interface Inner<P, R> {
        R handle(P p);
    }

}


