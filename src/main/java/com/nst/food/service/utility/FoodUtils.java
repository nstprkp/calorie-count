package com.nst.food.service.utility;

import com.nst.food.model.Food;
import com.nst.food.model.TypeProduct;
import com.nst.food.model.Vitamin;
import com.nst.food.model.dto.FoodDto;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class FoodUtils {
    public static FoodDto.Response buildFoodDtoFromModel(Food parameters) {
        Set<String> vitamins = null;
        if (parameters.getVitamins() != null) {
            vitamins = parameters.getVitamins().stream()
                    .map(Vitamin::getName)
                    .collect(Collectors.toSet());
        }

        return FoodDto.Response.builder()
                .id(parameters.getId())
                .dailyAmount(parameters.getDailyAmount())
                .food(parameters.getName())
                .calorie(parameters.getCalorie())
                .typeProduct(parameters.getTypeProduct().getName())
                .vitamins(vitamins)
                .build();
    }

    public static Food buildFood(FoodDto.RequestBody requestBody, TypeProduct typeProduct) {
        return Food.builder()
                .dailyAmount(requestBody.getDailyAmount())
                .name(requestBody.getFood())
                .calorie(requestBody.getCalorie())
                .typeProduct(typeProduct)
                .build();
    }
}
