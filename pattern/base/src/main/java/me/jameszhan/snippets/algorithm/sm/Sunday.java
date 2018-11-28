package me.jameszhan.snippets.algorithm.sm;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 2017/6/18
 *         Time: PM6:19
 */
public class Sunday implements StringMatcher {

    public static void main(String[] args) {
        Sunday sunday = new Sunday();
        System.out.println(sunday.search("aaabaaaabaaaaab", "aaaaa"));
        System.out.println(sunday.search("abbabbbbcab", "bbcab"));
        System.out.println(sunday.search("blog.csdn,blog.net", "csdn,blog"));
    }


    @Override
    public int search(String text, String pattern) {
        if (text == null || pattern == null || text.isEmpty() || pattern.isEmpty() || text.length() < pattern.length()) {
            return -1;
        } else {
            int i = 0, j = 0;
            int patternLen = pattern.length(), textLen = text.length();
            int location = 0;
            while (i < textLen && j < patternLen) {
                if (text.charAt(i) == pattern.charAt(j)) {
                    i++;
                    j++;
                } else {
                    //sAim:the location of char to judge
                    //pAim:the last location of the pattern string
                    int sAim = i + patternLen;
                    char aimChar = text.charAt(sAim);
                    int pAim = patternLen - 1;
                    //to judge char from back to front,the pAim is the equal location
                    while (pAim > 0) {
                        if (pattern.charAt(pAim) == aimChar) {
                            break;
                        }
                        pAim--;
                    }
                    //record the equal location with loc.
                    //sCount:move the judge location of source string
                    //pCount:move the begin of the pattern string
                    i = i + patternLen - pAim;
                    location = i;
                    j = 0;
                }
            }
            //if pattern string don't match completed,return -1
            if (j < patternLen) {
                return -1;
            } else {
                return location;
            }
        }
    }

}
