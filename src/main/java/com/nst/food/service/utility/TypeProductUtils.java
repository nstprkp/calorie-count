package com.nst.food.service.utility;

import com.nst.food.model.Food;
import com.nst.food.model.TypeProduct;
import com.nst.food.model.dto.BaseDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TypeProductUtils {
    public static BaseDto.Response buildTypeProductResponseFromModel(TypeProduct typeProduct) {
        List<String> foods = null;
        if (typeProduct.getFoods() != null) {
            foods = typeProduct.getFoods().stream()
                    .map(Food::getName)
                    .toList();
        }
        return BaseDto.Response.builder()
                .id(typeProduct.getId())
                .name(typeProduct.getName())
                .food(foods)
                .build();
    }
}
