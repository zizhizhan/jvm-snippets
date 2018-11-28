package me.jameszhan.snippets.algorithm.misc;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 2017/4/26
 *         Time: PM2:05
 */
public class Sigmod {

    public static double sigmod(double input) {
        double exp = Math.exp(input * -1);
        return 1.0 / (1.0 + exp);
    }

    public static void main(String[] args) {
        for (double i = -100; i < 100; i++) {
            System.out.format("%s: %s\n", i, sigmod(i));
        }
    }
}
