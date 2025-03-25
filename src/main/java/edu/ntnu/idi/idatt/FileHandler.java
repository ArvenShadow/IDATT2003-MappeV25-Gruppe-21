package edu.ntnu.idi.idatt;

public interface FileHandler<T> {

    T readFromFile(String fileName) throws BoardGameException;

    void writeToFile(T data, String filename) throws BoardGameException;
}
