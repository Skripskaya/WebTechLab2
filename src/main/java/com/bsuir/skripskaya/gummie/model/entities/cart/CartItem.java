package com.bsuir.skripskaya.gummie.model.entities.cart;

import com.bsuir.skripskaya.gummie.model.entities.gummie.Gummie;
import com.bsuir.skripskaya.gummie.model.exceptions.CloneException;

import java.io.Serializable;

public class CartItem implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private Gummie gummie;
    private int quantity;

    public CartItem(Gummie product, int quantity) {
        this.gummie = product;
        this.quantity = quantity;
    }

    public Gummie getGummie() {
        return gummie;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setGummie(Gummie product) {
        this.gummie = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "code=" + gummie.getId() +
                ", quantity=" + quantity;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneException("Error copying the product " + gummie.getId() + "with quantity" + quantity);
        }
    }
}
