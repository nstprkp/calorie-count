package com.nst.food.service;

import com.nst.food.model.Food;
import com.nst.food.model.TypeProduct;
import com.nst.food.dao.FoodRepository;
import com.nst.food.dao.TypeProductRepository;
import com.nst.food.model.dto.BaseDto;
import com.nst.food.service.utility.TypeProductUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nst.food.service.utility.TypeProductUtils.buildTypeProductResponseFromModel;

@AllArgsConstructor
@Service
public class TypeProductService implements CrudService<BaseDto.Response, BaseDto.RequestBody> {

    private final TypeProductRepository typeProductRepository;
    private final FoodRepository foodRepository;

    @Override
    public List<BaseDto.Response> getAll() {
        return typeProductRepository.findAll()
                .stream().map(TypeProductUtils::buildTypeProductResponseFromModel)
                .toList();
    }

    @Override
    public BaseDto.Response get(Long id) {
        TypeProduct typeProduct = typeProductRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return buildTypeProductResponseFromModel(typeProduct);
    }

    @Override
    public BaseDto.Response create(BaseDto.RequestBody createForm) {
        TypeProduct typeProduct = saveTypeProduct(new TypeProduct(), createForm);
        return buildTypeProductResponseFromModel(typeProduct);
    }

    @Override
    public BaseDto.Response update(Long id, BaseDto.RequestBody updateForm) {
        TypeProduct typeProduct = typeProductRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        TypeProduct newTypeProduct = saveTypeProduct(typeProduct, updateForm);
        return buildTypeProductResponseFromModel(newTypeProduct);
    }

    @Override
    public void delete(Long id) {
        TypeProduct typeProduct = typeProductRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        for (Food food : typeProduct.getFoods()) {
            food.setTypeProduct(null);
            foodRepository.save(food);
        }

        typeProductRepository.deleteById(id);
    }

    private TypeProduct saveTypeProduct(TypeProduct typeProduct, BaseDto.RequestBody requestBody) {
        List<Food> countryParameters = foodRepository.findByNames(requestBody.getFood());

        typeProduct.setName(requestBody.getName());
        typeProductRepository.save(typeProduct);

        for (Food country : countryParameters) {
            country.setTypeProduct(typeProduct);
            foodRepository.save(country);
        }

        typeProduct.setFoods(countryParameters);

        return typeProduct;
    }
}