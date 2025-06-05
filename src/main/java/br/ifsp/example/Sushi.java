package br.ifsp.example;

import br.ifsp.orm.EntityId;
import br.ifsp.orm.OrmEntity;

@OrmEntity
public class Sushi {
    @EntityId
    private String name;
    private Double price;
    private int quantity;

    public Sushi() {
    }

    public Sushi(String name, Double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("| Sushi = %s | Price = R$%.2f | Quantity = %d |", name, price, quantity);
    }
}
