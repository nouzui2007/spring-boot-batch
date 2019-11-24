package jp.isols.spring.batch.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonPropertyOrder({"商品", "数量", "単価($)"})
@Data
public class Order {
    @JsonProperty("商品")
    private String item;
    @JsonProperty("数量")
    private Integer quantity;
    @JsonProperty("単価($)")
    private Double unitPrice;

    public Order() {}

    public Order(String item, Integer quantity, Double unitPrice) {
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
}