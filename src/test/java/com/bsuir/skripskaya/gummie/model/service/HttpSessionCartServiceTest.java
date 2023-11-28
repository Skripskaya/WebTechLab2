package com.bsuir.skripskaya.gummie.model.service;

import com.bsuir.skripskaya.gummie.model.entities.cart.Cart;
import com.bsuir.skripskaya.gummie.model.exceptions.OutOfStockException;
import com.bsuir.skripskaya.gummie.model.exceptions.ServiceException;
import com.bsuir.skripskaya.gummie.model.service.impl.HttpSessionCartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpSessionCartServiceTest {
    private HttpSessionCartService cartService;
    private HttpServletRequest request;
    private HttpSession session;

    @Before
    public void setup() {
        cartService = HttpSessionCartService.getInstance();
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testGetCart_NewCart() {
        Cart cart = cartService.getCart(request.getSession());
        assertNotNull(cart);
        assertEquals(BigDecimal.ZERO, cart.getTotalCost());
    }

    @Test
    public void testDeleteCartItem() throws OutOfStockException, ServiceException {
        Cart cart = new Cart();
        cartService.add(cart, 1L, 5, request.getSession());

        cartService.delete(cart, 1L, request.getSession());

        assertEquals(0, cart.getTotalItems());
    }

}