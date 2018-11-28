package me.jameszhan.snippets.algorithm.misc;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 11/14/18
 * Time: 8:55 PM
 */
public class Ngrams {

    private Ngrams() {}

    private static final CharMatcher SPLITTER = CharMatcher.anyOf(" \t\n,.;");
    private static final String NGRAM_SPLITTER = " ";
    private static final Set<String> IGNORE_WORDS = Sets.newHashSet("CO", "COMPANY", "LTD", "LIMITED",
            "INC", "CORPORATION", "TRADE", "TREADING", "HOLDING", "ENTERPRISE", "LLC", "LLP", "BHD", "GMBH");

    public static void main(String[] args) {
        String s = "d e f CO COMPANY LTD LIMITED A B C ";
        System.out.println(ngram(s, 2));
        System.out.println(ngram(s, 3));

    }

    public static List<String> ngram(String str, int n) {
        if (Strings.isNullOrEmpty(str)) {
            return Lists.newArrayList();
        } else {
            List<String> featureList = Splitter.on(SPLITTER).omitEmptyStrings().splitToList(str);
            List<String> features = Lists.newArrayList();
            for (String feature : featureList) {
                if (!IGNORE_WORDS.contains(feature.toUpperCase())) {
                    features.add(feature);
                }
            }
            if (features.size() < n) {
                return Lists.newArrayList(str);
            } else {
                return ngram(features, n);
            }
        }
    }

    private static List<String> ngram(List<String> featureList, int n) {
        List<String> features = Lists.newArrayList();
        if (featureList == null || featureList.isEmpty() || featureList.size() < n) {
            return features;
        } else {
            for (int i = 0; i < featureList.size(); i++) {
                if (i < featureList.size() - (n - 1)) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < n; j++) {
                        sb.append(featureList.get(i + j));
                        if (j != n - 1) {
                            sb.append(NGRAM_SPLITTER);
                        }
                    }
                    features.add(sb.toString());
                }
            }
        }
        return features;
    }

}
