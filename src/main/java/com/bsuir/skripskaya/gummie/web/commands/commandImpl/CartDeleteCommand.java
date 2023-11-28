package com.bsuir.skripskaya.gummie.web.commands.commandImpl;

import com.bsuir.skripskaya.gummie.model.service.CartService;
import com.bsuir.skripskaya.gummie.model.service.impl.HttpSessionCartService;
import com.bsuir.skripskaya.gummie.web.commands.CommandHelper;
import com.bsuir.skripskaya.gummie.web.commands.ICommand;
import com.bsuir.skripskaya.gummie.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author skripskaya
 * @version 1.0
 */
public class CartDeleteCommand implements ICommand {
    private CartService cartService = HttpSessionCartService.getInstance();

    /**
     * Delete cart item and return cart jsp
     *
     * @param request http request
     * @return cart jsp
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        Object lang = request.getSession().getAttribute("lang");
        if (lang == null) {
            lang = "en";
        }
        Locale locale = new Locale(lang.toString());
        ResourceBundle rb = ResourceBundle.getBundle("messages", locale);
        int gummieId = Integer.parseInt(request.getParameter("id"));
        cartService.delete(cartService.getCart(request.getSession()), (long) gummieId, request.getSession());
        request.setAttribute("successMessage", rb.getString("delete_success"));
        return CommandHelper.getInstance().getCommand("cart").execute(request);
    }
}
