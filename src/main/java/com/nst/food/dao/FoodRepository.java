package com.nst.food.dao;

import com.nst.food.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findByNameIgnoreCaseOrDailyAmountIgnoreCase(String name, String dailyAmount);

    @Query(value = "select * from food o where o.name in (?1)", nativeQuery = true)
    List<Food> findByNames(@Param("1") List<String> names);

    Optional<List<Food>> findByCalorie(Integer calorie);
}
