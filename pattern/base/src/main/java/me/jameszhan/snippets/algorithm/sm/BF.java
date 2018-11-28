package me.jameszhan.snippets.algorithm.sm;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 2017/6/18
 *         Time: PM12:25
 */
public class BF implements StringMatcher {

    @Override
    public int search(String target, String pattern) {
        if (target == null || pattern == null || target.length() == 0 || pattern.length() == 0) {
            return -1;
        } else {
            int i = 0, j = 0;
            int targetLen = target.length(), patternLen = pattern.length();
            while (i < targetLen && j < patternLen) {
                if (target.charAt(i) == pattern.charAt(j)) {
                    i++;
                    j++;
                } else {
                    i = i - j + 1;
                    j = 0;
                }
            }
            if (j == patternLen) {
                return i - j;
            } else {
                return -1;
            }
        }
    }

    public static void main(String[] args) {
        BF bf = new BF();
        System.out.println(bf.search("aaabaaaabaaaaab", "aaaaa"));
        System.out.println(bf.search("abbabbbbcab", "bbcab"));
        System.out.println(bf.search("blog.csdn,blog.net", "csdn,blog"));
    }

}
