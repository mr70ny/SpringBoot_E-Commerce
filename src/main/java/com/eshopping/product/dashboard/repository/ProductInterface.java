package com.eshopping.product.dashboard.repository;

import com.eshopping.product.dashboard.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductInterface extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByIdAsc();

    List<Product> getProductByCategoryAndAvailability(Optional<String> Category, Boolean availability);

    List<Product> getProductByCategoryOrderByAvailabilityDescDiscountedPriceAscIdAsc(Optional<String> category);
}
