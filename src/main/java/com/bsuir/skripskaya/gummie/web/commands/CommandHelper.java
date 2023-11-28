package com.bsuir.skripskaya.gummie.web.commands;

import com.bsuir.skripskaya.gummie.web.commands.commandImpl.*;

import java.util.HashMap;
import java.util.Map;
/**
 * @author skripskaya
 * @version 1.0
 */
public final class CommandHelper {
    private static final CommandHelper instance = new CommandHelper();
    /**
     * Map with commands names and commands classes
     */
    private Map<CommandName, ICommand> commands = new HashMap<>();

    public CommandHelper() {
        commands.put(CommandName.PRODUCT_LIST, new ProductListCommand());
        commands.put(CommandName.UNKNOWN_COMMAND, new UnknownCommand());
        commands.put(CommandName.CART_ADD, new CartAddCommand());
        commands.put(CommandName.CART, new CartGetCommand());
        commands.put(CommandName.CART_UPDATE, new CartUpdateCommand());
        commands.put(CommandName.CART_DELETE, new CartDeleteCommand());
        commands.put(CommandName.AUTHORISATION, new LoginCommand());
        commands.put(CommandName.REGISTRATION, new RegistrationCommand());
        commands.put(CommandName.LOGOUT, new LogOutCommand());
        commands.put(CommandName.ORDER, new OrderPageCommand());
        commands.put(CommandName.ORDER_OVERVIEW, new OrderOverviewCommand());
        commands.put(CommandName.ADMIN_USERS, new AdminUsersCommand());
        commands.put(CommandName.USER_ORDERS, new UserOrdersCommand());
    }

    public static CommandHelper getInstance() {
        return instance;
    }

    /**
     * Method that return command class with requested name
     *
     * @param commandName name of class
     * @return instance of command class
     */
    public ICommand getCommand(String commandName) {
        CommandName name = CommandName.valueOf(commandName.toUpperCase());
        ICommand command;
        if (null != name) {
            command = commands.get(name);
        } else {
            command = commands.get(CommandName.UNKNOWN_COMMAND);
        }
        return command;
    }
}
