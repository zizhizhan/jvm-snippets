package com.zizhizhan.interview.lang;


public class Node<T> {

    volatile Node<T> prev;

    volatile Node<T> next;

    final T value;

    /**
     * Returns previous node, or throws NullPointerException if null.
     * Use when predecessor cannot be null.  The null check could
     * be elided, but is present to help the VM.
     *
     * @return the predecessor of this node
     */
    final Node<T> predecessor() throws NullPointerException {
        Node<T> p = prev;
        if (p == null)
            throw new NullPointerException();
        else
            return p;
    }

    public Node(T value) {
        this.value = value;
    }
}