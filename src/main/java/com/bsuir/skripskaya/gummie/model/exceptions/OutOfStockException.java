package com.bsuir.skripskaya.gummie.model.exceptions;

import com.bsuir.skripskaya.gummie.model.entities.gummie.Gummie;

/**
 * @author skripskaya
 * @version 1.0
 */
public class OutOfStockException extends Exception {
    /**
     * Gummie that outOfStock
     */
    private Gummie gummie;
    /**
     * Requested stock of gummie
     */
    private int requestedStock;
    /**
     * Available stock of gummie
     */
    private int availableStock;

    /**
     * Constructor of exception
     * @param gummie gummie of exception
     * @param requestedStock requested stock of exception
     * @param availableStock available stock of exceptttion
     */
    public OutOfStockException(Gummie gummie, int requestedStock, int availableStock) {
        this.gummie = gummie;
        this.requestedStock = requestedStock;
        this.availableStock = availableStock;
    }

    /**
     * Place exception message
     * @param s exception message
     */
    public OutOfStockException(String s) {
        super(s);
    }

    public Gummie getProduct() {
        return gummie;
    }

    public int getRequestedStock() {
        return requestedStock;
    }

    public int getAvailableStock() {
        return availableStock;
    }

}
