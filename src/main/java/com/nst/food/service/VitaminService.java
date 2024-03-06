package com.nst.food.service;

import com.nst.food.model.Food;
import com.nst.food.model.Vitamin;
import com.nst.food.dao.FoodRepository;
import com.nst.food.dao.VitaminRepository;
import com.nst.food.model.dto.BaseDto;
import com.nst.food.service.utility.VitaminUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.nst.food.service.utility.VitaminUtils.buildVitaminResponseFromModel;

@AllArgsConstructor
@Service
public class VitaminService implements CrudService<BaseDto.Response, BaseDto.RequestBody> {
    private final VitaminRepository vitaminRepository;
    private final FoodRepository foodRepository;

    @Override
    public List<BaseDto.Response> getAll() {
        return vitaminRepository.findAll().stream()
                .map(VitaminUtils::buildVitaminResponseFromModel)
                .toList();
    }

    @Override
    public BaseDto.Response get(Long id) {
        return buildVitaminResponseFromModel(vitaminRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public BaseDto.Response create(BaseDto.RequestBody createForm) {
        Vitamin vitamin = saveVitamin(createForm);
        return buildVitaminResponseFromModel(vitamin);
    }

    @Override
    public BaseDto.Response update(Long id, BaseDto.RequestBody updateForm) {
        Vitamin vitaminModel = vitaminRepository.findById(id)
                .map(vitamin -> saveVitamin(vitamin, updateForm))
                .orElseThrow(IllegalArgumentException::new);
        return buildVitaminResponseFromModel(vitaminModel);
    }

    @Override
    public void delete(Long id) {
        Vitamin vitamin = vitaminRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        for (Food food : vitamin.getFoodSet()) {
            food.getVitamins().remove(vitamin);
        }

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