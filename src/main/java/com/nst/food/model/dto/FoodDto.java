package com.nst.food.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class FoodDto {
    @Data
    @Builder
    public class Response {
        private Long id;
        private String dailyAmount;
        private String food;
        private Integer calorie;

        private String typeProduct;
        private Set<String> vitamins;
    }

    @Data
    @Builder
    public class RequestBody {
        private String dailyAmount;
        private String food;
        private Integer calorie;

        private String typeProduct;
    }
}
