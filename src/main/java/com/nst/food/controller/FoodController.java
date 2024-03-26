package com.nst.food.controller;

import com.nst.food.model.dto.FoodDto;
import com.nst.food.service.FoodService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calorie-count")
@Validated
public class FoodController {
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/food")
    public ResponseEntity<FoodDto.Response> getCalorieByFood(@RequestParam("name") String food) {
        FoodDto.Response calorie = foodService.getCalorieByFoodOrId(food);
        return ResponseEntity.ok(calorie);
    }

    @GetMapping("/calorie")
    public ResponseEntity<List<FoodDto.Response>> getFoodByCalorie(@RequestParam("name") @PositiveOrZero Integer calorie) {
        List<FoodDto.Response> food = foodService.getFoodByCalorie(calorie);
        return ResponseEntity.ok(food);
    }

    @GetMapping
    public ResponseEntity<List<FoodDto.Response>> getAllFood() {
        List<FoodDto.Response> food = foodService.getAll();
        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodDto.Response> getFoodById(@PathVariable Long id) {
        FoodDto.Response food = foodService.get(id);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }

    @GetMapping("/calorie-greaterThan")
    public ResponseEntity<List<FoodDto.Response>> getFoodByCalorieGreaterThan(@RequestParam("calorie") Integer calorie) {
        List<FoodDto.Response> food = foodService.getFoodByCalorieGreaterThan(calorie);
        return ResponseEntity.ok(food);
    }

    @PostMapping
    public ResponseEntity<FoodDto.Response> createFood(@RequestBody FoodDto.RequestBody food) {
        return new ResponseEntity<>(foodService.create(food), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodDto.Response> updateFood(@PathVariable Long id, @RequestBody FoodDto.RequestBody food) {
        return new ResponseEntity<>(foodService.update(id, food), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}