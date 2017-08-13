package br.com.jmsstudio.model;

import java.util.ArrayList;
import java.util.List;

public class Purchase {

    private int id;
    private List<Product> products = new ArrayList<>();

    public Purchase(int id, List<Product> products) {
        this.id = id;
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Purchase purchase = (Purchase) o;

        if (id != purchase.id) return false;
        return products.equals(purchase.products);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + products.hashCode();
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", products=" + products +
                '}';
    }
}
