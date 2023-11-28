package com.bsuir.skripskaya.gummie.model.entities.stock;

import com.bsuir.skripskaya.gummie.model.entities.gummie.Gummie;

public class Stock {
    private Gummie gummie;
    private Integer stock;
    private Integer reserved;

    public Gummie getGummie() {
        return gummie;
    }

    public void setGummie(Gummie gummie) {
        this.gummie = gummie;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getReserved() {
        return reserved;
    }

    public void setReserved(Integer reserved) {
        this.reserved = reserved;
    }

}
