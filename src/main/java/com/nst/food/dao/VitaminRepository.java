package com.nst.food.dao;

import com.nst.food.model.Vitamin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VitaminRepository extends JpaRepository<Vitamin, Long> {

}
