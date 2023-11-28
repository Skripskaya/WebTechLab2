package com.bsuir.skripskaya.gummie.web.commands;

import com.bsuir.skripskaya.gummie.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
/**
 * @author skripskaya
 * @version 1.0
 */
public interface ICommand {
    /**
     * Main function of all commands
     *
     * @param request http request
     * @return jsp path
     * @throws CommandException throws when some errors during execution of command
     */
    String execute(HttpServletRequest request) throws CommandException;
}
