package com.zizhizhan.lang;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import org.slf4j.Logger;
import sun.misc.Launcher;
import sun.misc.Unsafe;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/25
 * Time: 下午2:06
 */
public class GetJarPath {

    public static void main(String[] args) throws Exception {
        showClassJarPath(Unsafe.class);
        showClassJarPath(GetJarPath.class);
        showClassJarPath(Cache.class);
        showClassJarPath(Logger.class);

        System.out.format("\nBootstrapClassloader:\n");
        for (URL url : Launcher.getBootstrapClassPath().getURLs()) {
            System.out.println(url);
        }

        String extDirs = System.getProperty("java.ext.dirs");
        System.out.format("\nExtClassPath:\n%s\n", pettyList(extDirs, ':'));

        String appClassPath = System.getProperty("java.class.path");
        System.out.format("\nAppClassPath: %s\n", pettyList(appClassPath, ':'));

        // SystemClassLoader is equal to AppClassloader
    }

    private static void showClassJarPath(Class<?> clazz) {
        ProtectionDomain domain = clazz.getProtectionDomain();
        if (domain != null) {
            CodeSource codeSource = domain.getCodeSource();
            if (codeSource !=  null) {
                System.out.format("%s: \t%s\n", clazz, codeSource.getLocation());
            } else {
                System.out.format("%s: \t(domain: %s)\n", clazz, domain);
            }
        }
    }

    private static String pettyList(String origin, char splitter) {
        return Joiner.on('\n').join(Splitter.on(splitter).split(origin));
    }

}
