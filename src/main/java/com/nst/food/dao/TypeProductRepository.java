package com.nst.food.dao;

import com.nst.food.model.TypeProduct;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeProductRepository extends JpaRepository<TypeProduct, Long> {
    Optional<TypeProduct> findByName(String name);
}