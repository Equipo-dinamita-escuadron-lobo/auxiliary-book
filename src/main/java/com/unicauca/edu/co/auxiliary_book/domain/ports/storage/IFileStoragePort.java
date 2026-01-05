package com.unicauca.edu.co.auxiliary_book.domain.ports.storage;

/**
 * Output port for storing binary artifacts.
 */
public interface IFileStoragePort {

    /**
     * Stores a binary resource and returns the absolute path where it is available.
     *
     * @param content  file content.
     * @param fileName desired file name.
     * @return absolute path to the stored file.
     */
    String store(byte[] content, String fileName);
}
