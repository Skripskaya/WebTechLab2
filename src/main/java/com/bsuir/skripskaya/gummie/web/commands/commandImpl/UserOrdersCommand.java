package com.bsuir.skripskaya.gummie.web.commands.commandImpl;

import com.bsuir.skripskaya.gummie.model.dao.OrderDao;
import com.bsuir.skripskaya.gummie.model.dao.impl.JdbcOrderDao;
import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;
import com.bsuir.skripskaya.gummie.web.JspPageName;
import com.bsuir.skripskaya.gummie.web.commands.ICommand;
import com.bsuir.skripskaya.gummie.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author skripskaya
 * @version 1.0
 */
public class UserOrdersCommand implements ICommand {
    private OrderDao orderDao = JdbcOrderDao.getInstance();
    private static final String ORDERS_ATTRIBUTE = "orders";

    /**
     * Get user orders and show user orders page
     * @param request http request
     * @return user orders page jsp path
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        try {
            request.setAttribute(ORDERS_ATTRIBUTE, orderDao.findOrdersByLogin(request.getSession().getAttribute("login").toString()));
        } catch (DaoException e) {
            throw new CommandException(e.getMessage());
        }
        return JspPageName.USER_ORDERS_PAGE_JSP;
    }
}
