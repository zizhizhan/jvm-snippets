package com.zizhizhan.legacies.javap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 5/11/15
 *         Time: 2:19 PM
 */
public class HackingBasic {

    static interface TA {

    }

    static interface TB {

    }

    static class Base implements TA, TB {

    }

    interface A {
        void save(TA t);
    }

    interface B {
        void save(TB t);
    }

    interface C extends A, B {

    }

    static class D implements C {

        @Override
        public void save(TA t) {

        }

        @Override
        public void save(TB t) {

        }
    }

    public static void main(String[] args) {
        C d = new D();
        Base base = new Base();
        //d.save(base);
    }
}
