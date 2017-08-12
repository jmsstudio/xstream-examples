package br.com.jmsstudio.model;

public class Product {

    private String name;
    private double price;
    private String description;
    private int code;

    public Product(String name, double price, String description, int code) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (Double.compare(product.price, price) != 0) return false;
        if (code != product.code) return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        return !(description != null ? !description.equals(product.description) : product.description != null);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + code;
        return result;
    }
}
