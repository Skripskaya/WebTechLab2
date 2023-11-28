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
public class OrderOverviewCommand implements ICommand {
    private OrderDao orderDao = JdbcOrderDao.getInstance();
    private static final String ORDER_ATTRIBUTE = "order";

    /**
     * Get order overview page
     *
     * @param request http request
     * @return order overview page jsp path
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        try {
            if (request.getParameter("secureId") == null) {
                request.setAttribute(ORDER_ATTRIBUTE, orderDao.getById((Long) request.getAttribute("Id")).orElse(null));
            } else {
                request.setAttribute(ORDER_ATTRIBUTE, orderDao.getById((Long) request.getAttribute("Id")).orElse(null));
            }

        } catch (DaoException e) {
            throw new CommandException(e.getMessage());
        }
        return JspPageName.ORDER_OVERVIEW_PAGE_JSP;
    }
}
