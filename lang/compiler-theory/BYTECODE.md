
### Bytecode Structure

```java
ClassFile {
    u4 magic;
    u2 minor_version;
    u2 major_version;
    u2 constant_pool_count;
    cp_info constant_pool[constant_pool_count-1];
    u2 access_flags;
    u2 this_class;
    u2 super_class;
    u2 interfaces_count;
    u2 interfaces[interfaces_count];
    u2 fields_count;
    field_info fields[fields_count];
    u2 methods_count;
    method_info methods[methods_count];
    u2 attributes_count;
    attribute_info attributes[attributes_count];
}
```

```sh
javac src/main/java/me/jameszhan/underlying/bytecode/Add.java -d target/classes
```

```sh
javap -cp target/classes -sysinfo -constants me.jameszhan.underlying.bytecode.Add
```

输出： 

```
Classfile /u/rebirth/perfect/underlying/target/classes/me/jameszhan/underlying/bytecode/Add.class
  Last modified May 4, 2015; size 406 bytes
  MD5 checksum 1e394e11c968f235868d8e4df4836533
  Compiled from "Add.java"
public class me.jameszhan.underlying.bytecode.Add {
  public me.jameszhan.underlying.bytecode.Add();
  int add(int, int);
}
```

```sh
javap -cp target/classes -c me.jameszhan.underlying.bytecode.Add
```

```
Compiled from "Add.java"
public class me.jameszhan.underlying.bytecode.Add {
  public me.jameszhan.underlying.bytecode.Add();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  int add(int, int);
    Code:
       0: iload_1
       1: iload_2
       2: iadd
       3: ireturn
}
```


```
public static void main(java.lang.String[]);
    Code:
       0: iconst_0
       1: iconst_0
       2: iconst_1
       3: iadd
       4: iconst_1
       5: iadd
       6: iconst_1
       7: iadd
       8: iconst_1
       9: iadd
      10: iconst_1
      11: iadd
      12: iconst_1
      13: iadd
      14: dup
      15: ifeq          37
      18: iconst_1
      19: isub
      20: iconst_1
      21: iadd
      22: iconst_1
      23: iadd
      24: iconst_1
      25: iadd
      26: iconst_1
      27: iadd
      28: iconst_1
      29: iadd
      30: iconst_1
      31: iadd
      32: iconst_1
      33: iadd
      34: goto          14
      37: pop
      38: dup
      39: invokestatic  #12                 // Method java/lang/String.valueOf:(I)Ljava/lang/String;
      42: getstatic     #18                 // Field java/lang/System.out:Ljava/io/PrintStream;
      45: swap
      46: invokevirtual #24                 // Method java/io/PrintStream.print:(Ljava/lang/Object;)V
      49: ldc           #26                 // String
      51: dup
      52: getstatic     #18                 // Field java/lang/System.out:Ljava/io/PrintStream;
      55: swap
      56: invokevirtual #29                 // Method java/io/PrintStream.println:(Ljava/lang/Object;)V
      59: return
}
```

```
Code:
   0: iconst_0
   1: istore_2
   2: iload_2
   3: iload_1
   4: if_icmpge     13
   7: iinc          2, 1
  10: goto          2
  13: iload_2
  14: iload_1
  15: iadd
  16: istore_3
  17: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
  20: iload_3
  21: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
  24: return
```  
