package com.bsuir.skripskaya.gummie.model.exceptions;

/**
 * @author skripskaya
 * @version 1.0
 */
public class DaoException extends Exception {
    /**
     * Place message of exception
     *
     * @param message message of exception
     */
    public DaoException(String message) {
        super(message);
    }
}
