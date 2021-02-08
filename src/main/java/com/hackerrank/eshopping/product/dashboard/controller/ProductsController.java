package com.hackerrank.eshopping.product.dashboard.controller;

import com.hackerrank.eshopping.product.dashboard.model.Product;
import com.hackerrank.eshopping.product.dashboard.repository.ProductInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ProductsController {

    private final ProductInterface productInterface;

    public ProductsController(ProductInterface productInterface) {
        this.productInterface = productInterface;
    }

    @PostMapping(value = "/products", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            if (productInterface.existsById(product.getId())) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(productInterface.save(product), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/products/{product_id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Product> updateProductById(@RequestBody Product product, @PathVariable("product_id") long id) {
        try {
            Optional<Product> productOptional = productInterface.findById(id);
            if (productOptional.isPresent()) {
                Product addProduct = productOptional.get();
                addProduct.setId(id);
                if (product.getName() != null) {
                    addProduct.setName(product.getName());
                }
                if (product.getCategory() != null) {
                    addProduct.setCategory(product.getCategory());
                }
                if (product.getRetailPrice() != null) {
                    addProduct.setRetailPrice(product.getRetailPrice());
                }
                if (product.getDiscountedPrice() != null) {
                    addProduct.setDiscountedPrice(product.getDiscountedPrice());
                }
                if (product.getAvailability() != null) {
                    addProduct.setAvailability(product.getAvailability());
                }
                return new ResponseEntity<>(productInterface.save(addProduct), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/products/{product_id}", produces = "application/json")
    public ResponseEntity<Product> getProductById(@PathVariable("product_id") long id) {
        try {
            Optional<Product> productOptional = productInterface.findById(id);
            if (productOptional.isPresent()) {
                return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/products", produces = "application/json")
    public ResponseEntity<List<Product>> getProducts(@RequestParam("category") Optional<String> category, @RequestParam("availability") Optional<Integer> availability) {
        try {
            if (!category.isPresent() && !availability.isPresent()) { //ALL
                List<Product> productList = productInterface.findAllByOrderByIdAsc();
                return new ResponseEntity<>(productList, HttpStatus.OK);
            } else if (category.isPresent() && !availability.isPresent()) { //Only Category
                List<Product> productListCategory = productInterface.getProductByCategoryOrderByAvailabilityDescDiscountedPriceAscIdAsc(Optional.of(URLDecoder.decode(category.get(), "UTF-8")));
                return new ResponseEntity<>(productListCategory, HttpStatus.OK);
            } else if (category.isPresent() && availability.isPresent()) { //Category and Availability
                Boolean availabiltyBool = availability.get() == 1;
                List<Product> productListCategoryAndAvailability = productInterface.getProductByCategoryAndAvailability(Optional.of(URLDecoder.decode(category.get(), "UTF-8")), availabiltyBool);
                productListCategoryAndAvailability.forEach(product -> product.setDiscountedpercentage(discountPercentage(product.getRetailPrice(), product.getDiscountedPrice())));
                List<Product> collect = productListCategoryAndAvailability.stream()
                        .sorted((v1, v2) -> {
                            int i1 = v2.getDiscountedpercentage().compareTo(v1.getDiscountedpercentage());
                            if (i1 != 0) {
                                return i1;
                            }
                            BigDecimal discountedPrice = v1.getDiscountedPrice();
                            BigDecimal discountedPrice1 = v2.getDiscountedPrice();
                            int i = discountedPrice.compareTo(discountedPrice1);
                            if (i != 0) {
                                return i;
                            }
                            return v1.getId().compareTo(v2.getId());
                        }).collect(Collectors.toList());
                return new ResponseEntity<>(collect, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Integer discountPercentage(BigDecimal retailPrice, BigDecimal discountPrice) {

        BigDecimal subtract = retailPrice.subtract(discountPrice);
        BigDecimal divide = subtract.divide(retailPrice);
        BigDecimal multiply = divide.multiply(new BigDecimal("100"));
        return multiply.intValue();
    }

}
