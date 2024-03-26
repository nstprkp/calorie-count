package com.nst.food.service;

import com.nst.food.components.CacheComponent;
import com.nst.food.model.Food;
import com.nst.food.model.Vitamin;
import com.nst.food.dao.FoodRepository;
import com.nst.food.dao.VitaminRepository;
import com.nst.food.model.dto.BaseDto;
import com.nst.food.service.utility.VitaminUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nst.food.service.utility.VitaminUtils.buildVitaminResponseFromModel;

@AllArgsConstructor
@Service
@Slf4j
public class VitaminService implements CrudService<BaseDto.Response, BaseDto.RequestBody> {
    private static final String CACHE_INFO_GET = "Cached data taken for key ";
    private static final String CACHE_INFO_REMOVE = "Cached data removed for key ";
    private static final String CACHE_INFO_UPDATE = "Cached data updated for key ";
    private static final String VITAMIN_ID = "VitaminID ";
    private final VitaminRepository vitaminRepository;
    private final FoodRepository foodRepository;
    private final CacheComponent cache;

    @Override
    public List<BaseDto.Response> getAll() {
        log.info("all information obtained from database");
        return vitaminRepository.findAll().stream()
                .map(VitaminUtils::buildVitaminResponseFromModel)
                .toList();
    }

    @Override
    public BaseDto.Response get(Long id) {
        String key = VITAMIN_ID + id;
        BaseDto.Response cachedResult = (BaseDto.Response) cache.get(key);
        if (cachedResult != null) {
            String logMessage = CACHE_INFO_GET + key;
            log.info(logMessage);
            return cachedResult;
        }
        BaseDto.Response data = buildVitaminResponseFromModel(vitaminRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new));
        cache.put(key, data);
        log.info("information obtained from database (vitamins) by id");
        return data;
    }

    @Override
    public BaseDto.Response create(BaseDto.RequestBody createForm) {
        Vitamin vitamin = saveVitamin(createForm);
        log.info("new information added to database (vitamins)");
        return buildVitaminResponseFromModel(vitamin);
    }

    @Override
    public BaseDto.Response update(Long id, BaseDto.RequestBody updateForm) {
        Vitamin vitaminModel = vitaminRepository.findById(id)
                .map(vitamin -> saveVitamin(vitamin, updateForm))
                .orElseThrow(IllegalArgumentException::new);
        BaseDto.Response data = buildVitaminResponseFromModel(vitaminModel);
        String key = VITAMIN_ID + id;
        cache.remove(key);
        cache.put(key, data);
        String logMessage = CACHE_INFO_UPDATE + key;
        log.info(logMessage);
        log.info("information in the database and cache (if exist) has been updated");
        return data;
    }

    @Override
    public void delete(Long id) {
        String key = VITAMIN_ID + id;
        cache.remove(key);
        String logMessage = CACHE_INFO_REMOVE + key;
        log.info(logMessage);
        Vitamin vitamin = vitaminRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        for (Food food : vitamin.getFoodSet()) {
            food.getVitamins().remove(vitamin);
        }
        log.info("information in the database and cache (if exist) has been deleted");
        vitaminRepository.deleteById(id);
    }

    private Vitamin saveVitamin(BaseDto.RequestBody requestBody) {
        Vitamin vitamin = new Vitamin();
        return saveVitamin(vitamin, requestBody);
    }

    private Vitamin saveVitamin(Vitamin vitamin, BaseDto.RequestBody requestBody) {
        List<Food> food = foodRepository.findByNames(requestBody.getFood());
        vitamin.setName(requestBody.getName());
        vitaminRepository.save(vitamin);

        food.forEach(foodName -> {
            Set<Vitamin> vitaminSet = foodName.getVitamins();
            if (vitaminSet == null) {
                vitaminSet = new HashSet<>();
            }
            vitaminSet.add(vitamin);
            foodName.setVitamins(vitaminSet);
            foodRepository.save(foodName);
        });

        vitamin.setFoodSet(food);
        return vitamin;
    }
}