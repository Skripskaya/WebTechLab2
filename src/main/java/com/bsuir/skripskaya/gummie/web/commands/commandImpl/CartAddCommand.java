package com.bsuir.skripskaya.gummie.web.commands.commandImpl;

import com.bsuir.skripskaya.gummie.model.exceptions.OutOfStockException;
import com.bsuir.skripskaya.gummie.model.exceptions.ServiceException;
import com.bsuir.skripskaya.gummie.model.service.CartService;
import com.bsuir.skripskaya.gummie.model.service.impl.HttpSessionCartService;
import com.bsuir.skripskaya.gummie.web.commands.CommandHelper;
import com.bsuir.skripskaya.gummie.web.commands.ICommand;
import com.bsuir.skripskaya.gummie.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author skripskaya
 * @version 1.0
 */
public class CartAddCommand implements ICommand {
    private CartService cartService = HttpSessionCartService.getInstance();

    /**
     * Add item to cart and return to referer page
     *
     * @param request http request
     * @return jsp path
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        Map<Long, String> inputErrors = new HashMap<>();
        Object lang = request.getSession().getAttribute("lang");
        if (lang == null) {
            lang = "en";
        }
        Locale locale = new Locale(lang.toString());
        ResourceBundle rb = ResourceBundle.getBundle("messages", locale);
        int gummieId = Integer.parseInt(request.getParameter("id"));
        try {
            int quantity = parseQuantity(request.getParameter("quantity"), request);
            cartService.add(cartService.getCart(request.getSession()), (long) gummieId, quantity, request.getSession());
        } catch (OutOfStockException e) {
            inputErrors.put((long) gummieId, rb.getString("NOT_ENOUGH_ERROR") + e.getAvailableStock());
        } catch (ParseException e) {
            inputErrors.put((long) gummieId, rb.getString("NOT_A_NUMBER_ERROR"));
        } catch (ServiceException e) {
            throw new CommandException(e.getMessage());
        }

        if (inputErrors.isEmpty()) {
            request.setAttribute("successMessage", rb.getString("add_success"));
        } else {
            request.setAttribute("inputErrors", inputErrors);
        }
        if (request.getParameter("page_type").equals("productList")) {
            return CommandHelper.getInstance().getCommand("Product_List").execute(request);
        } else {
            request.setAttribute("gummie_id", gummieId);
            return CommandHelper.getInstance().getCommand("product_details").execute(request);
        }
    }

    /**
     * Parse quantity from string to int considering number format
     *
     * @param quantity string quantity
     * @param request  http request
     * @return quantity
     * @throws ParseException throws when String representation is not a number
     */
    private int parseQuantity(String quantity, HttpServletRequest request) throws ParseException {
        int result;
        Object lang = request.getSession().getAttribute("lang");
        if (lang == null) {
            lang = "en";
        }
        Locale locale = new Locale(lang.toString());
        ResourceBundle rb = ResourceBundle.getBundle("messages", locale);
        if (!quantity.matches("^\\d+([\\.\\,]\\d+)?$")) {
            throw new ParseException(rb.getString("NOT_A_NUMBER_ERROR"), 0);
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(request.getLocale());
        result = numberFormat.parse(quantity).intValue();

        return result;
    }
}
