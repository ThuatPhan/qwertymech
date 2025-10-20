package org.example.productservice.repository;

import org.example.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, String id);
}
