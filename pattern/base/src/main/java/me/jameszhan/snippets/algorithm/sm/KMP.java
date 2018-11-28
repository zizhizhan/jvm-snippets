package me.jameszhan.snippets.algorithm.sm;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 2017/6/18
 *         Time: PM12:37
 */
public class KMP implements StringMatcher {

    public static void main(String[] args) {
        KMP kmp = new KMP();
        System.out.println(kmp.search("aaabaaaabaaaaab", "aaaaa"));
        System.out.println(kmp.search("abbabbbbcab", "bbcab"));
        System.out.println(kmp.search("blog.csdn,blog.net", "csdn,blog"));

    }

    public int search(String target, String pattern) {
        if (target == null || pattern == null || target.length() == 0 || pattern.length() == 0) {
            return -1;
        } else {
            int[] next = next(pattern);
            int i = 0, j = 0;
            int targetLen = target.length(), patternLen = pattern.length();
            while (i < targetLen && j < patternLen) {
                if (j == -1 || target.charAt(i) == pattern.charAt(j)) {
                    // System.out.format("-%d:%d\n", i, j);
                    i++;
                    j++;
                } else {
                    // System.out.format("+%d:%d\n", i, j);
                    j = next[j];
                }
            }
            if (j == patternLen) {
                return i - j;
            } else {
                return -1;
            }
        }
    }

    private int[] next(String pattern) {
        int[] next = new int[pattern.length()];
        next[0] = -1;
        int i = 0, j = -1;
        while (i < pattern.length() - 1) {
            if (j == -1 || pattern.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                next[i] = j;
//                if (pattern.charAt(i) != pattern.charAt(j)) {
//                    next[i] = j;
//                } else {
//                    next[i] = next[j];
//                }
            } else {
                j = next[j];
            }
        }
        return next;
    }

}
