package edu.ntnu.idi.idatt.io;

import edu.ntnu.idi.idatt.exception.BoardGameException;

/**
 * Generic interface for handling read and write operations to and from files.
 *
 * <p>This interface is designed to provide a standard way to implement file
 * reading and writing functionality for various data types. Implementing
 * classes can define how specific types of data are serialized and deserialized
 * from files.
 *
 * @param <T> The type of data to be read from or written to a file.
 */

public interface FileHandler<T> {

    T readFromFile(String fileName) throws BoardGameException;

    void writeToFile(T data, String filename) throws BoardGameException;
}
