package com.zizhizhan.legacies.lang;

public class IPlusplus {
    public static void main(String[] args) {
        int m, i = 2;
        //m = (++i) + (--i) + (i++) + (++i) + (i--) + (++i);
        int a = (++i);
        int b = (--i);
        int c = (i++);
        int d = (++i);
        int e = (i--);
        int f = (++i);

        m = a + b + c + d + e + f;
        System.out.println(m);
        System.out.println(i);
    }

}
