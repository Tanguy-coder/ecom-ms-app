package net.tanguydev.billingservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Product {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
}
