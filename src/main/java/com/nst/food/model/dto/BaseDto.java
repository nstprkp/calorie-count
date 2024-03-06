package com.nst.food.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class BaseDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class RequestBody {
        private String name;
        private List<String> food;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class Response {
        private Long id;
        private String name;
        private List<String> food;
    }
}
