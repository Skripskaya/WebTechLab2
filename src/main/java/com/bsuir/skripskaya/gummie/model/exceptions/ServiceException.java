package com.bsuir.skripskaya.gummie.model.exceptions;

/**
 * @author skripskaya
 * @version 1.0
 */
public class ServiceException extends Exception {
    /**
     * Place message of exception
     *
     * @param message message of exception
     */
    public ServiceException(String message) {
        super(message);
    }
}
