package com.study.model;

public class Product {
    public Integer productId;

    public Product(Integer productId){
        this.productId = productId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                '}';
    }

}
