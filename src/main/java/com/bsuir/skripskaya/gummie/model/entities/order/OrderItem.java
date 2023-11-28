package com.bsuir.skripskaya.gummie.model.entities.order;

import com.bsuir.skripskaya.gummie.model.entities.gummie.Gummie;

public class OrderItem {
    private Long id;
    private Gummie gummie;
    private Order order;
    private int quantity;

    public Gummie getGummie() {
        return gummie;
    }

    public void setGummie(final Gummie gummie) {
        this.gummie = gummie;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }
}