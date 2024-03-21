package com.nst.food.service;

import com.nst.food.model.dto.FoodDto;

import java.util.List;

public interface FoodService extends CrudService<FoodDto.Response, FoodDto.RequestBody> {
    FoodDto.Response getCalorieByFoodOrId(String foodOrId);

    List<FoodDto.Response> getFoodByCalorie(Integer calorie);

    List<FoodDto.Response> getFoodByCalorieGreaterThan(Integer calorie);
}
