package com.zizhizhan.jvm.unsafe;

import sun.misc.Unsafe;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zizhi.zhzzh
 *         Date: 7/8/15
 *         Time: 3:05 PM
 */
public class Primitives<T> {

    private static final Unsafe unsafe = Unsafe.getUnsafe();

    private volatile T value;

    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(Primitives.class.getDeclaredField("value"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    public boolean compareAndSet(T expected, T update){
        return unsafe.compareAndSwapObject(this, valueOffset, expected, update);
    }


    public T getAndSet(T update) {
        for(;;) {
            T current = value;
            if (compareAndSet(current, update)) {
                return current;
            }
        }
    }

}
