package com.bsuir.skripskaya.gummie.model.entities.order;

import com.bsuir.skripskaya.gummie.model.dao.GummieDao;
import com.bsuir.skripskaya.gummie.model.dao.impl.JdbcGummieDao;
import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemsExtractor {
    public List<OrderItem> extractData(ResultSet resultSet) throws SQLException, DaoException {
        List<OrderItem> orderItems = new ArrayList<>();
        GummieDao gummieDao = JdbcGummieDao.getInstance();
        while (resultSet.next()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setGummie(gummieDao.get(resultSet.getLong("gummieId")).orElse(null));
            orderItem.setQuantity(resultSet.getInt("quantity"));
            orderItems.add(orderItem);
        }
        return orderItems;
    }
}
