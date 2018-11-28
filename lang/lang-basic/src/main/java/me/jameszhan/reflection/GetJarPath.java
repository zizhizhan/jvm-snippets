package me.jameszhan.reflection;

import com.google.common.cache.Cache;
import org.slf4j.Logger;

/**
 * Create by zhiqiangzhan@gmail.com
 *
 * @author James Zhan
 * Date: 2018/10/25
 * Time: 下午2:06
 */
public class GetJarPath {

    public static void main(String[] args) {
        System.out.println(GetJarPath.class.getProtectionDomain().getCodeSource().getLocation());
        System.out.println(Cache.class.getProtectionDomain().getCodeSource().getLocation());
        System.out.println(Logger.class.getProtectionDomain().getCodeSource().getLocation());
    }



}
