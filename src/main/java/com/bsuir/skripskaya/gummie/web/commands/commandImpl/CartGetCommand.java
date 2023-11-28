package com.bsuir.skripskaya.gummie.web.commands.commandImpl;

import com.bsuir.skripskaya.gummie.model.service.CartService;
import com.bsuir.skripskaya.gummie.model.service.impl.HttpSessionCartService;
import com.bsuir.skripskaya.gummie.web.JspPageName;
import com.bsuir.skripskaya.gummie.web.commands.ICommand;
import com.bsuir.skripskaya.gummie.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author skripskaya
 * @version 1.0
 */
public class CartGetCommand implements ICommand {
    private static final String CART_ATTRIBUTE = "cart";
    private final CartService cartService = HttpSessionCartService.getInstance();

    /**
     * Get cart jsp
     *
     * @param request http request
     * @return cart jsp path
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        request.getSession().setAttribute(CART_ATTRIBUTE, cartService.getCart(request.getSession()));
        return JspPageName.CART_JSP;
    }
}
