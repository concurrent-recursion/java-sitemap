package io.github.concurrentrecursion.sitemap.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility class for working with streams.
 */
@UtilityClass
public class Streams {

    /**
     * Converts a stream to a stream of batches using the given batchSize to split the stream.<br>
     * For example: A stream is given, and a batchSize of 15 is defined. Then the output will be a stream of lists with
     * a size of 15 (except for the final batch which may have fewer).<br>
     * This method does NOT load the entire stream in memory and is capable of processing an endless stream
     * @param stream The input stream of data
     * @param batchSize The size of the List that will be returned by the stream.
     * @return A Stream of Lists consisting of data from the input stream
     * @param <T> The Type of object contained in the input/output stream
     */
    public static <T> Stream<List<T>> batchStream(Stream<T> stream, final int batchSize) {
        Iterator<T> iterator = stream.iterator();
        Iterator<List<T>> listIterator = new Iterator<>() {

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public List<T> next() {
                List<T> result = new ArrayList<>(batchSize);
                for (int i = 0; i < batchSize && iterator.hasNext(); i++) {
                    result.add(iterator.next());
                }
                return result;
            }
        };
        return StreamSupport.stream(((Iterable<List<T>>) () -> listIterator).spliterator(), false);
    }
}
