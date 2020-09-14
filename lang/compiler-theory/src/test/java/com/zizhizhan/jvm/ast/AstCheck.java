package com.zizhizhan.jvm.ast;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/6
 *         Time: PM4:11
 */
public class AstCheck {

    //    0: iconst_1
    //    1: newarray       int
    //    3: astore_1
    //    4: iconst_1
    //    5: istore_2
    //    6: iconst_2
    //    7: istore_3
    //    7: istore_3
    //
    //    # 左子树：数组下标
    //    #  a[0]
    //    8: aload_1
    //    9: iconst_0
    //
    //    # 右子树：加法
    //    # a
    //    10: iload_2
    //    # b
    //    11: iload_3
    //    # +
    //    12: iadd
    //
    //    # 根节点：赋值
    //    13: iastore
    //
    //    14: aload_1
    //    15: iconst_0
    //    16: iaload
    //    17: ireturn

    /**
     *          =
     * a[0]             +
     *              a       b
     */
    public int astCheck() {
        int[] arr = new int[1];
        int a = 1;
        int b = 2;
        arr[0] = a + b;
        return arr[0];
    }

}
