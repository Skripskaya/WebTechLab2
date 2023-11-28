package com.bsuir.skripskaya.gummie.model.entities.order;

import com.bsuir.skripskaya.gummie.model.dao.OrderItemDao;
import com.bsuir.skripskaya.gummie.model.dao.impl.JdbcOrderItemDao;
import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersExtractor {
    public List<Order> extractData(ResultSet resultSet) throws SQLException, DaoException {
        List<Order> orders = new ArrayList<>();
        OrderItemDao orderItemDao = new JdbcOrderItemDao();
        while (resultSet.next()) {
            Order order = new Order();
            order.setId(resultSet.getLong("id"));
            order.setSubtotal(resultSet.getBigDecimal("subtotal"));
            order.setDeliveryPrice(resultSet.getBigDecimal("deliveryPrice"));
            order.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
            order.setStatus(OrderStatus.fromString(resultSet.getString("status")));
            order.setOrderItems(orderItemDao.getOrderItems(order.getId()));
            order.setDate(resultSet.getDate("date"));
            order.setTime(resultSet.getTime("time"));
            order.setLogin(resultSet.getString("login"));
            orders.add(order);
        }
        return orders;
    }
}
