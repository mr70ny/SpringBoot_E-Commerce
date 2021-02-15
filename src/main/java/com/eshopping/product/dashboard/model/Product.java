package com.eshopping.product.dashboard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@DynamicUpdate
public class Product {

    @Column(name = "id", unique = true)
    @JsonProperty("id")
    @Id
    private Long id;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "category")
    @JsonProperty("category")
    private String category;

    @Column(name = "retail_price")
    @JsonProperty("retail_price")
    @Digits(integer=13, fraction=2)
    private BigDecimal retailPrice;

    @Column(name = "discounted_price")
    @JsonProperty("discounted_price")
    @Digits(integer=13, fraction=2)
    private BigDecimal discountedPrice;

    @Column(name = "availability")
    @JsonProperty("availability")
    private Boolean availability;

    @JsonIgnore
    @Transient
    private Integer discountedpercentage;
    }