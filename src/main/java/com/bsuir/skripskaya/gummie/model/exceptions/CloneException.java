package com.bsuir.skripskaya.gummie.model.exceptions;

/**
 * @author skripskaya
 * @version 1.0
 */
public class CloneException extends RuntimeException {
    /**
     * Place message of exception
     * @param message message of exception
     */
    public CloneException(String message) {
        super(message);
    }
}
