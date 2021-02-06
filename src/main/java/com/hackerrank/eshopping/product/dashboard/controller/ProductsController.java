package com.hackerrank.eshopping.product.dashboard.controller;

import com.hackerrank.eshopping.product.dashboard.model.Product;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController {

    @PostMapping(value = "/products", produces = "application/json",
    consumes = "application/json")
    public Product addNewProduct(@RequestBody Product product) {


      
    }

}
