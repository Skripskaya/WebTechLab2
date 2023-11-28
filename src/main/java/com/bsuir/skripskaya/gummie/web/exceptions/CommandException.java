package com.bsuir.skripskaya.gummie.web.exceptions;

/**
 * @author skripskaya
 * @version 1.0
 */
public class CommandException extends ProjectException {
    private static final long serialVersionUID = 1L;

    public CommandException(String msg) {
        super(msg);
    }

    public CommandException(String msg, Exception e) {
        super(msg, e);
    }

}
