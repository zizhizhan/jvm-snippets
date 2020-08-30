package com.zizhizhan.interview.lang;

import com.zizhizhan.interview.aqs.AbstractQueuedSynchronizer;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Slf4j
public class ThreadSafeList<E> implements List<E> {

    private static Unsafe unsafe;

    private static final long headOffset;
    private static final long tailOffset;

    static {
        try {
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            unsafe = (Unsafe) theUnsafeField.get(null);
            headOffset = unsafe.objectFieldOffset(ThreadSafeList.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset(ThreadSafeList.class.getDeclaredField("tail"));
        } catch (Exception ex) {
            log.error("Initialize AQS Error.", ex);
            throw new Error(ex);
        }
    }

    /**
     * Head of the wait queue, lazily initialized.  Except for
     * initialization, it is modified only via method setHead.  Note:
     * If head exists, its waitStatus is guaranteed not to be
     * CANCELLED.
     */
    transient volatile Node<E> head;

    /**
     * Tail of the wait queue, lazily initialized.  Modified only via
     * method enq to add new wait node.
     */
    transient volatile Node<E> tail;

    @Override
    public int size() {
        int totalCount = 0;
        Node<E> curr = head.next;
        while (curr != null) {
            totalCount++;
            curr = curr.next;
        }
        return totalCount;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        Node<E> node = new Node<>(e);
        for (;;) {
            Node<E> t = tail;
            if (t == null) { // Must initialize
                if (compareAndSetHead(new Node<>(null)))
                    tail = head;
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t != null;
                }
            }
        }
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public E get(int index) {
        return null;
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    /**
     * CAS head field. Used only by enq.
     */
    private boolean compareAndSetHead(Node<E> update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    /**
     * CAS tail field. Used only by enq.
     */
    private boolean compareAndSetTail(Node<E> expect, Node<E> update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }
}
