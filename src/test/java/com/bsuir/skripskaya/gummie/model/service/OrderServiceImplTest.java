package com.bsuir.skripskaya.gummie.model.service;

import com.bsuir.skripskaya.gummie.model.entities.cart.Cart;
import com.bsuir.skripskaya.gummie.model.entities.cart.CartItem;
import com.bsuir.skripskaya.gummie.model.entities.gummie.Gummie;
import com.bsuir.skripskaya.gummie.model.entities.order.Order;
import com.bsuir.skripskaya.gummie.model.service.impl.OrderServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImplTest {
    private OrderService orderService;


    @Before
    public void init() {
        orderService = OrderServiceImpl.getInstance();
    }

    @Test
    public void testCreatingOrderFromCart() {
        Cart cart = new Cart();
        cart.setTotalCost(new BigDecimal(100));
        cart.setTotalItems(3);

        List<CartItem> testList = new ArrayList<>();
        testList.add(new CartItem(new Gummie(), 2));
        cart.setItems(testList);

        Order order = orderService.createOrder(cart);
        Assert.assertEquals(order.getSubtotal(), cart.getTotalCost());
    }

    @Test
    public void testCloningObjectsWhileCreatingOrderFromCart() {
        Cart cart = new Cart();
        cart.setTotalCost(new BigDecimal(100));
        cart.setTotalItems(1);

        List<CartItem> testList = new ArrayList<>();
        testList.add(new CartItem(new Gummie(), 2));
        cart.setItems(testList);

        Order order = orderService.createOrder(cart);
        for (int i = 0; i < order.getOrderItems().size(); i++) {
            Assert.assertEquals(order.getOrderItems().get(i).getGummie(), cart.getItems().get(i).getGummie());
            Assert.assertEquals(order.getOrderItems().get(i).getQuantity(), cart.getItems().get(i).getQuantity());
        }
    }

    @After
    public void clean() {
        orderService = null;
    }
}
