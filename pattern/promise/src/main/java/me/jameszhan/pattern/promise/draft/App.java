/**
 * The MIT License
 * Copyright (c) 2014-2016 Ilkka Seppälä
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.jameszhan.pattern.promise.draft;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Promise object is used for asynchronous computations. A Promise represents an operation
 * that hasn't completed yet, but is expected in the future.
 *
 * <p>A Promise represents a proxy for a value not necessarily known when the promise is created. It
 * allows you to associate dependent promises to an asynchronous action's eventual success value or
 * failure reason. This lets asynchronous methods return values like synchronous methods: instead
 * of the final value, the asynchronous method returns a promise of having a value at some point
 * in the future.
 *
 * <p>Promises provide a few advantages over callback objects:
 * <ul>
 * <li> Functional composition and error handling
 * <li> Prevents callback hell and provides callback aggregation
 * </ul>
 *
 * <p>
 * In this application the usage of promise is demonstrated with two examples:
 * <ul>
 * <li>Count Lines: In this example a file is downloaded and its line count is calculated.
 * The calculated line count is then consumed and printed on console.
 * <li>Lowest Character Frequency: In this example a file is downloaded and its lowest frequency
 * character is found and printed on console. This happens via a chain of promises, we start with
 * a file download promise, then a promise of character frequency, then a promise of lowest frequency
 * character which is finally consumed and result is printed on console.
 * </ul>
 *
 * @see CompletableFuture
 */
@Slf4j
public class App {

    private static final String DEFAULT_URL = "https://raw.githubusercontent.com/jameszhan/linux-internal/master/bin/psh";
    private final CountDownLatch stopLatch;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    private App() {
        stopLatch = new CountDownLatch(2);
    }

    /**
     * Program entry point
     *
     * @param args arguments
     * @throws InterruptedException if main thread is interrupted.
     */
    public static void main(String[] args) throws InterruptedException {
        App app = new App();
        try {
            app.promiseUsage();
        } finally {
            app.stop();
        }
    }

    private void promiseUsage() {
        calculateLineCount();
        calculateLowestFrequencyChar();
    }

    /*
     * Calculate the lowest frequency character and when that promise is fulfilled,
     * consume the result in a Consumer<Character>
     */
    private void calculateLowestFrequencyChar() {
        lowestFrequencyChar().thenAccept(charFrequency -> {
            log.info("Char with lowest frequency is: {}", charFrequency);
            taskCompleted();
        });
    }

    /*
     * Calculate the line count and when that promise is fulfilled, consume the result
     * in a Consumer<Integer>
     */
    private void calculateLineCount() {
        countLines().thenAccept(count -> {
            log.info("Line count is: {}", count);
            taskCompleted();
        });
    }

    /*
     * Calculate the character frequency of a file and when that promise is fulfilled,
     * then promise to apply function to calculate lowest character frequency.
     */
    private Promise<Character> lowestFrequencyChar() {
        return characterFrequency().thenApply(App::lowestFrequencyChar);
    }

    /*
     * Download the file at DEFAULT_URL and when that promise is fulfilled,
     * then promise to apply function to calculate character frequency.
     */
    private Promise<Map<Character, Integer>> characterFrequency() {
        return download(DEFAULT_URL).thenApply(App::characterFrequency);
    }

    /*
     * Download the file at DEFAULT_URL and when that promise is fulfilled,
     * then promise to apply function to count lines in that file.
     */
    private Promise<Integer> countLines() {
        return download(DEFAULT_URL).thenApply(App::countLines);
    }

    /*
     * Return a promise to provide the local absolute path of the file downloaded in background.
     * This is an async method and does not wait until the file is downloaded.
     */
    private Promise<String> download(String urlString) {
        return Promise.await(() -> downloadFile(urlString), executor).caught(throwable -> {
            throwable.printStackTrace();
            taskCompleted();
        });
    }

    private void stop() throws InterruptedException {
        stopLatch.await();
        executor.shutdown();
    }

    private void taskCompleted() {
        stopLatch.countDown();
    }

    /**
     * Calculates character frequency of the file provided.
     * @param fileLocation location of the file.
     * @return a map of character to its frequency, an empty map if file does not exist.
     */
    private static Map<Character, Integer> characterFrequency(String fileLocation) {
        Map<Character, Integer> characterToFrequency = new HashMap<>();
        try (Reader reader = new FileReader(fileLocation);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            for (String line; (line = bufferedReader.readLine()) != null; ) {
                for (char c : line.toCharArray()) {
                    if (!characterToFrequency.containsKey(c)) {
                        characterToFrequency.put(c, 1);
                    } else {
                        characterToFrequency.put(c, characterToFrequency.get(c) + 1);
                    }
                }
            }
        } catch (IOException ex) {
            log.error("Unexpected IO exception.", ex);
        }
        return characterToFrequency;
    }

    /**
     * @return the character with lowest frequency if it exists, {@code Optional.empty()} otherwise.
     */
    private static Character lowestFrequencyChar(Map<Character, Integer> charFrequency) {
        Character lowestFrequencyChar;
        Iterator<Map.Entry<Character, Integer>> iterator = charFrequency.entrySet().iterator();
        Map.Entry<Character, Integer> entry = iterator.next();
        int minFrequency = entry.getValue();
        lowestFrequencyChar = entry.getKey();

        while (iterator.hasNext()) {
            entry = iterator.next();
            if (entry.getValue() < minFrequency) {
                minFrequency = entry.getValue();
                lowestFrequencyChar = entry.getKey();
            }
        }

        return lowestFrequencyChar;
    }

    /**
     * @return number of lines in the file at provided location. 0 if file does not exist.
     */
    private static Integer countLines(String fileLocation) {
        int lineCount = 0;
        try (Reader reader = new FileReader(fileLocation);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            while (bufferedReader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException ex) {
            log.error("Unexpected IO exception.", ex);
        }
        return lineCount;
    }

    /**
     * Downloads the contents from the given urlString, and stores it in a temporary directory.
     * @return the absolute path of the file downloaded.
     */
    private static String downloadFile(String urlString) throws IOException {
        log.info("Downloading contents from url: {}", urlString);
        URL url = new URL(urlString);
        File file = File.createTempFile("promise_pattern", null);
        try (Reader reader = new InputStreamReader(url.openStream());
             BufferedReader bufferedReader = new BufferedReader(reader);
             FileWriter writer = new FileWriter(file)) {
            for (String line; (line = bufferedReader.readLine()) != null; ) {
                writer.write(line);
                writer.write("\n");
            }
            log.info("File downloaded at: {}", file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (IOException ex) {
            throw ex;
        }
    }
}
