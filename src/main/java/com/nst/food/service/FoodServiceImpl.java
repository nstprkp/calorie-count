package com.nst.food.service;

import com.nst.food.CacheComponent;
import com.nst.food.dao.FoodRepository;
import com.nst.food.dao.TypeProductRepository;
import com.nst.food.model.Food;
import com.nst.food.model.TypeProduct;
import com.nst.food.model.dto.FoodDto;
import com.nst.food.service.utility.FoodUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FoodServiceImpl implements FoodService {
    private static final String CACHE_INFO_GET = "Cached data taken for key ";
    private static final String CACHE_INFO_REMOVE = "Cached data removed for key ";
    private static final String CACHE_INFO_UPDATE = "Cached data updated for key ";
    private final FoodRepository foodRepository;
    private final TypeProductRepository typeProductRepository;
    private final CacheComponent cache;

    @Override
    public FoodDto.Response getCalorieByFoodOrId(String foodOrId) {
        String key = "foodOrId " + foodOrId;
        FoodDto.Response cachedResult = (FoodDto.Response) cache.get(key);
        if (cachedResult != null) {
            String logMessage = CACHE_INFO_GET + key;
            log.info(logMessage);
            return cachedResult;
        }
        Food food = foodRepository
                .findByNameIgnoreCaseOrDailyAmountIgnoreCase(foodOrId, foodOrId)
                .orElseThrow(IllegalArgumentException::new);
        FoodDto.Response data = FoodUtils.buildFoodDtoFromModel(food);
        cache.put(key, data);
        log.info("information obtained from database by food");
        return data;
    }

    @Override
    public List<FoodDto.Response> getFoodByCalorie(Integer calorie) {
        String key = "Calorie " + calorie;
        List<FoodDto.Response> cachedResult = (List<FoodDto.Response>) cache.get(key);
        if (cachedResult != null) {
            String logMessage = CACHE_INFO_GET + key;
            log.info(logMessage);
            return cachedResult;
        }
        List<Food> foodList = foodRepository.findByCalorie(calorie)
                .orElseThrow(IllegalArgumentException::new);
        List<FoodDto.Response> data = foodList.stream()
                .map(FoodUtils::buildFoodDtoFromModel)
                .toList();
        cache.put(key, data);
        log.info("information obtained from database by calories");
        return data;
    }

    @Override
    public List<FoodDto.Response> getAll() {
        log.info("all information obtained from database");
        return foodRepository.findAll().stream()
                .map(FoodUtils::buildFoodDtoFromModel)
                .toList();
    }

    @Override
    public FoodDto.Response get(Long id) {
        String key = "ID " + id;
        FoodDto.Response cachedResult = (FoodDto.Response) cache.get(key);
        if (cachedResult != null) {
            String logMessage = CACHE_INFO_GET + key;
            log.info(logMessage);
            return cachedResult;
        }
        Food food = foodRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        FoodDto.Response data = FoodUtils.buildFoodDtoFromModel(food);
        cache.put(key, data);
        log.info("information obtained from database by ID");
        return data;
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
        log.info("new information added to database");
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
        FoodDto.Response data = FoodUtils.buildFoodDtoFromModel(food);
        String key = "ID " + id;
        cache.remove(key);
        cache.put(key, data);
        String logMessage = CACHE_INFO_UPDATE + key;
        log.info(logMessage);
        log.info("information in the database and cache (if exist) has been updated");
        return data;
    }

    @Override
    public void delete(Long id) {
        String key = "ID " + id;
        cache.remove(key);
        String logMessage = CACHE_INFO_REMOVE + key;
        log.info(logMessage);
        foodRepository.deleteById(id);
        log.info("information in the database and cache (if exist) has been deleted");
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

    @Override
    public List<FoodDto.Response> getFoodByCalorieGreaterThan(Integer calorie) {
        List<Food> foodList = foodRepository.findByCalorieGreaterThan(calorie)
                .orElseThrow(IllegalArgumentException::new);
        return foodList.stream()
                .map(FoodUtils::buildFoodDtoFromModel)
                .toList();
    }
}
