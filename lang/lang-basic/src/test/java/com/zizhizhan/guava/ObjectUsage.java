package com.zizhizhan.guava;

import com.google.common.base.Preconditions;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 16/9/1
 *         Time: PM9:19
 */
public class ObjectUsage {

    public static void main(String[] args) {
        Object o = new Object();
        int[] array = {1, 2, 3};
        Preconditions.checkNotNull(o, "检查对象不能为空");
        Preconditions.checkElementIndex(2, array.length, "检查数组索引");
        Preconditions.checkArgument(10 <= 100, "检查参数值");
        Preconditions.checkState(true, "检查对象状态");
        System.out.println(Arrays.toString(array));
    }

}
