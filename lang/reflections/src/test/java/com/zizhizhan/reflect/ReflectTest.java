package com.zizhizhan.reflect;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class ReflectTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        Foo foo = new Foo();
        System.out.println(foo.getS());
        System.out.println(foo.getTest());
		/*List<String> list = foo.getList();
		for (String string : list) {
			System.out.println(string);			
		}
		*/
        List<String> rep = new LinkedList<String>();
        rep.add("abc");
        rep.add("gih");

        try {
            Field f = foo.getClass().getDeclaredField("s");
            f.setAccessible(true);
            f.set(foo, "uvw");
			
			/*f = foo.getClass().getDeclaredField("list");
			f.setAccessible(true);
			f.set(foo, rep);*/

            f = foo.getClass().getDeclaredField("test");
            f.setAccessible(true);
            f.set(null, "cccc");
        } catch (SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        System.out.println("--------------------------");

        System.out.println(foo.getS());
        System.out.println(foo.getTest());
		/*list = foo.getList();
		for (String string : list) {
			System.out.println(string);			
		}*/
    }
}

