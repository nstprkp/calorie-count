package com.nst.food.service;

import com.nst.food.dao.FoodRepository;
import com.nst.food.dao.TypeProductRepository;
import com.nst.food.model.Food;
import com.nst.food.model.TypeProduct;
import com.nst.food.model.dto.FoodDto;
import com.nst.food.service.utility.FoodUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final TypeProductRepository typeProductRepository;

    @Override
    public FoodDto.Response getCalorieByFoodOrId(String foodOrId) {
        Food food = foodRepository
                .findByNameIgnoreCaseOrDailyAmountIgnoreCase(foodOrId, foodOrId)
                .orElseThrow(IllegalArgumentException::new);
        return FoodUtils.buildFoodDtoFromModel(food);
    }

    @Override
    public List<FoodDto.Response> getFoodByCalorie(Integer calorie) {
        List<Food> foodList = foodRepository.findByCalorie(calorie)
                .orElseThrow(IllegalArgumentException::new);
        return foodList.stream()
                .map(FoodUtils::buildFoodDtoFromModel)
                .toList();
    }

    @Override
    public List<FoodDto.Response> getAll() {
        return foodRepository.findAll().stream()
                .map(FoodUtils::buildFoodDtoFromModel)
                .toList();
    }

    @Override
    public FoodDto.Response get(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return FoodUtils.buildFoodDtoFromModel(food);
    }

    @Override
    public FoodDto.Response create(FoodDto.RequestBody createForm) {
        TypeProduct typeProduct = typeProductRepository.findByName(createForm.getTypeProduct())
                .orElseGet(() -> {
                    TypeProduct newTypeProduct = new TypeProduct();
                    newTypeProduct.setName(createForm.getTypeProduct());
                    return typeProductRepository.save(newTypeProduct);
                });

        Food food = FoodUtils.buildFood(createForm, typeProduct);
        foodRepository.save(food);
        return FoodUtils.buildFoodDtoFromModel(food);
    }

    @Override
    public FoodDto.Response update(Long id, FoodDto.RequestBody updateForm) {
        Food food = foodRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        TypeProduct typeProduct = typeProductRepository.findByName(updateForm.getTypeProduct())
                .orElseThrow(IllegalArgumentException::new);

        Food newFood = updateFood(food, updateForm, typeProduct);
        foodRepository.save(newFood);
        return FoodUtils.buildFoodDtoFromModel(food);
    }

    @Override
    public void delete(Long id) {
        foodRepository.deleteById(id);
    }

    private Food updateFood(Food food, FoodDto.RequestBody updateForm, TypeProduct typeProduct) {
        if (updateForm.getDailyAmount() != null) {
            food.setDailyAmount(updateForm.getDailyAmount());
        }
        if (updateForm.getFood() != null) {
            food.setName(updateForm.getFood());
        }
        if (updateForm.getCalorie() != null) {
            food.setCalorie(updateForm.getCalorie());
        }
        if (updateForm.getTypeProduct() != null) {
            food.setTypeProduct(typeProduct);
        }
        return food;
    }
}
