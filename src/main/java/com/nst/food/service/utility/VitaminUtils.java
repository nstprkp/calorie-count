package com.nst.food.service.utility;

import com.nst.food.model.Food;
import com.nst.food.model.Vitamin;
import com.nst.food.model.dto.BaseDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class VitaminUtils {
    public static BaseDto.Response buildVitaminResponseFromModel(Vitamin vitamin) {
        List<String> food = null;
        if (vitamin.getFoodSet() != null) {
            food = vitamin.getFoodSet().stream()
                    .map(Food::getName)
                    .toList();
        }
        return BaseDto.Response.builder()
                .id(vitamin.getId())
                .name(vitamin.getName())
                .food(food)
                .build();
    }
}
