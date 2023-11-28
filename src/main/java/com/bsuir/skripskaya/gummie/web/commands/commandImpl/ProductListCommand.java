package com.bsuir.skripskaya.gummie.web.commands.commandImpl;

import com.bsuir.skripskaya.gummie.model.dao.GummieDao;
import com.bsuir.skripskaya.gummie.model.dao.impl.JdbcGummieDao;
import com.bsuir.skripskaya.gummie.model.enums.SortField;
import com.bsuir.skripskaya.gummie.model.enums.SortOrder;
import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;
import com.bsuir.skripskaya.gummie.web.JspPageName;
import com.bsuir.skripskaya.gummie.web.commands.ICommand;
import com.bsuir.skripskaya.gummie.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * @author skripskaya
 * @version 1.0
 */
public class ProductListCommand implements ICommand {
    private final GummieDao gummieDao = JdbcGummieDao.getInstance();
    private static final String QUERY_PARAMETER = "query";
    private static final String SORT_PARAMETER = "sort";
    private static final String ORDER_PARAMETER = "order";
    private static final String GUMMIES_ATTRIBUTE = "gummies";
    private static final String PAGE_PARAMETER = "page";
    private static final String PAGE_ATTRIBUTE = "numberOfPages";
    private static final int GUMMIES_ON_PAGE = 10;

    /**
     * Get product list and return product list jsp
     * @param request http request
     * @return product list jsp path
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String pageNumber = request.getParameter(PAGE_PARAMETER);
        Long number;
        try {
            request.setAttribute(GUMMIES_ATTRIBUTE, gummieDao.findAll(((pageNumber == null ? 1 : Integer.parseInt(pageNumber)) - 1) * GUMMIES_ON_PAGE, GUMMIES_ON_PAGE,
                    Optional.ofNullable(request.getParameter(SORT_PARAMETER)).map(SortField::valueOf).orElse(null),
                    Optional.ofNullable(request.getParameter(ORDER_PARAMETER)).map(SortOrder::valueOf).orElse(null), request.getParameter(QUERY_PARAMETER)));
            number = Long.valueOf(2);//gummieDao.numberByQuery(request.getParameter(QUERY_PARAMETER));
        } catch (DaoException e) {
            throw new CommandException(e.getMessage());
        }
        request.setAttribute(PAGE_ATTRIBUTE, (number + GUMMIES_ON_PAGE - 1) / GUMMIES_ON_PAGE);
        return JspPageName.PRODUCT_LIST_JSP;
    }
}
