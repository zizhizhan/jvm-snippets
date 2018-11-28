package me.jameszhan.snippets.algorithm.misc;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 2017/5/25
 *         Time: AM11:28
 */
public class LevenshteinDistance {

    public int lowerOfThree(int first, int second, int third) {
        int min = first;
        if (second < min) {
            min = second;
        }
        if (third < min) {
            min = third;
        }
        return min;
    }

    public int compareDistance(String str1, String str2) {
        int[][] matrix;
        int n = str1.length();
        int m = str2.length();

        int temp = 0;
        char ch1;
        char ch2;
        int i = 0;
        int j = 0;
        if (n == 0) {
            return m;
        }
        if (m == 0) {

            return n;
        }
        matrix = new int[n + 1][m + 1];

        for (i = 0; i <= n; i++) {
            matrix[i][0] = i;
        }

        for (j = 0; j <= m; j++) {
            matrix[0][j] = j;
        }

        for (i = 1; i <= n; i++) {
            ch1 = str1.charAt(i - 1);
            for (j = 1; j <= m; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                matrix[i][j] = lowerOfThree(matrix[i - 1][j] + 1, matrix[i][j - 1] + 1, matrix[i - 1][j - 1] + temp);
            }
            System.out.println(Arrays.deepToString(matrix));
        }


        return matrix[n][m];
    }


    public double LevenshteinDistancePercent(String str1, String str2) {
        int maxLenth = str1.length() > str2.length() ? str1.length() : str2.length();
        int val = compareDistance(str1, str2);
        System.out.println(val);
        return 1.0 - (double) val / maxLenth;
    }


    public static void main(String[] args) {
        LevenshteinDistance ld = new LevenshteinDistance();
        System.out.println(ld.LevenshteinDistancePercent("jary", "jerry"));
    }
}
