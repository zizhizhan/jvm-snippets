package com.zizhizhan.legacies.javap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 5/11/15
 *         Time: 2:03 PM
 */
public class HackingGeneric {

    static class Base {

    }

    interface A<T> {
        void save(T t);
    }

    interface B {
        void save(Base t);
    }

    interface C extends A<Base>, B {

    }

    static class D implements C {

        @Override public void save(Base base) {

        }
    }

    public static void main(String[] args) {
        C d = new D();
        Base base = new Base();
        //d.save(base);
    }

}
