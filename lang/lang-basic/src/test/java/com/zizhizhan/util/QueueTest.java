package com.zizhizhan.util;

public class QueueTest {

    public static void main(String[] args) {
        Queue queue = new Queue();

        queue.push(1);
        queue.push(2);
        queue.push(3);
        queue.push(4);
        queue.push(5);
        queue.push(6);

        while (queue.size() > 0) {
            System.out.println(queue.poll());
        }

        queue.push(7);
        queue.push(8);
        queue.push(9);
        queue.push(10);
        queue.push(11);
        queue.push(12);

        while (queue.size() > 0) {
            System.out.println(queue.poll());
        }
    }

    public static class Queue {

        private static final int MAX_SIZE = 10;

        private final int[] array = new int[MAX_SIZE];
        private int tou = 0;
        private int wei = 0;
        private int size = 0;

        public void push(int e) {
            if ((size + 1) > MAX_SIZE) {
                throw new RuntimeException("Hello");
            }
            if (wei == MAX_SIZE - 1) {
                wei = 0;
            }
            array[wei++] = e;
            size++;
        }

        public int poll() {
            if (size == 0) {
                throw new RuntimeException("OK");
            }
            size--;
            if (tou == MAX_SIZE - 1) {
                tou = 0;
            }
            return array[tou++];
        }

        public int size() {
            return size;
        }
    }

    public static class Queue2 {
        private static final int MAX_SIZE = 10;
        private final int[] data = new int[MAX_SIZE];
        private int front = -1;
        private int rear = 0;

        public void push(int e) {
            data[rear++] = e;
        }

        public int poll() {
            return data[++front];
        }

        public boolean isEmpty() {
            return front == rear;
        }

        public boolean isFull() {
            return false;
        }

        public int size() {
            return (rear + MAX_SIZE - front) % MAX_SIZE + 1;
        }
    }
}
