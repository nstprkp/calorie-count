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

    @Query(value = "select * from food o where o.name = :name", nativeQuery = true)
    List<Food> findByNames(@Param("name") List<String> names);

    Optional<List<Food>> findByCalorie(Integer calorie);

    @Query("SELECT f FROM Food f WHERE f.calorie < :calorie")
    Optional<List<Food>> findByCalorieGreaterThan(@Param("calorie") Integer calorie);
}
