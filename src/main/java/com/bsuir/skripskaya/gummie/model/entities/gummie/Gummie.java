package com.bsuir.skripskaya.gummie.model.entities.gummie;


import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Gummie {
    private Long id;
    private String name;
    private BigDecimal price;

    public Gummie() {
    }

    public Gummie(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Gummie(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gummie gummie = (Gummie) o;
        return id.equals(gummie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
