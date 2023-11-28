package com.bsuir.skripskaya.gummie.web.commands.commandImpl;

import com.bsuir.skripskaya.gummie.model.dao.UserDao;
import com.bsuir.skripskaya.gummie.model.dao.impl.JdbcUserDao;
import com.bsuir.skripskaya.gummie.model.entities.user.User;
import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;
import com.bsuir.skripskaya.gummie.web.JspPageName;
import com.bsuir.skripskaya.gummie.web.commands.ICommand;
import com.bsuir.skripskaya.gummie.web.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author skripskaya
 * @version 1.0
 */
public class AdminUsersCommand implements ICommand {
    private static final String USERS_ATTRIBUTE = "users";
    private static final String SUCCESS_ATTRIBUTE = "successMessage";
    private static final String ERROR_ATTRIBUTE = "errorMessage";
    private final UserDao userDao = JdbcUserDao.getInstance();

    /**
     * Return admin users page jsp or delete user
     *
     * @param request http request
     * @return admin users page jsp path
     * @throws CommandException throws when there is some errors during command execution
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        try {
            if (!request.getMethod().equals("GET")) {
                deleteUser(request);
            }
            request.setAttribute(USERS_ATTRIBUTE, userDao.findAllUsers());
        } catch (DaoException e) {
            throw new CommandException(e.getMessage());
        }
        return JspPageName.ADMIN_USERS_PAGE_JSP;
    }

    /**
     * Mehtod for delete user
     *
     * @param request http request
     * @throws CommandException throws when there is some errors during command execution
     */
    private void deleteUser(HttpServletRequest request) throws CommandException {
        Long userId = Long.valueOf(request.getParameter("userId"));
        User user;
        try {
            user = userDao.findUser(userId).orElse(null);
        } catch (DaoException e) {
            throw new CommandException(e.getMessage());
        }
        Object lang = request.getSession().getAttribute("lang");
        if (lang == null) {
            lang = "en";
        }
        Locale locale = new Locale(lang.toString());
        ResourceBundle rb = ResourceBundle.getBundle("messages", locale);
        if (user != null) {
            try {
                userDao.deleteUser(user);
            } catch (DaoException e) {
                throw new CommandException(e.getMessage());
            }
            request.setAttribute(SUCCESS_ATTRIBUTE, rb.getString("user_delete_success"));
        } else {
            request.setAttribute(ERROR_ATTRIBUTE, rb.getString("error_message"));
        }
    }
}
