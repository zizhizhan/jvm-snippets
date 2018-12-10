package me.jameszhan.concurrent;

import java.util.concurrent.CompletableFuture;

public class PromiseMain {

    private static int getNextInt(int v) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return v + 1;
    }

    public static void main(String[] args) throws Exception {
        int i = 10;
        CompletableFuture f = CompletableFuture
                .supplyAsync(() -> getNextInt(i))
                .thenApply(PromiseMain::getNextInt)
                .thenApply(PromiseMain::getNextInt)
                .thenApply(PromiseMain::getNextInt)
                .thenApply(PromiseMain::getNextInt)
                .thenApply(PromiseMain::getNextInt)
                .thenApply(PromiseMain::getNextInt)
                .thenApply((v) -> {
                    System.out.println(v);
                    return v;
                });

        Object value = f.get();
        System.out.println(value);
    }

}
