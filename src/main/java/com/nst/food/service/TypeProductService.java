package com.nst.food.service;

import com.nst.food.CacheComponent;
import com.nst.food.model.Food;
import com.nst.food.model.TypeProduct;
import com.nst.food.dao.FoodRepository;
import com.nst.food.dao.TypeProductRepository;
import com.nst.food.model.dto.BaseDto;
import com.nst.food.service.utility.TypeProductUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nst.food.service.utility.TypeProductUtils.buildTypeProductResponseFromModel;

@AllArgsConstructor
@Service
@Slf4j
public class TypeProductService implements CrudService<BaseDto.Response, BaseDto.RequestBody> {
    private static final String CACHE_INFO_GET = "Cached data taken for key ";
    private static final String CACHE_INFO_REMOVE = "Cached data removed for key ";
    private static final String CACHE_INFO_UPDATE = "Cached data updated for key ";
    private static final String TYPE_ID = "TypeID ";
    private final TypeProductRepository typeProductRepository;
    private final FoodRepository foodRepository;
    private final CacheComponent cache;

    @Override
    public List<BaseDto.Response> getAll() {
        log.info("all information obtained from database");
        return typeProductRepository.findAll()
                .stream().map(TypeProductUtils::buildTypeProductResponseFromModel)
                .toList();
    }

    @Override
    public BaseDto.Response get(Long id) {
        String key = TYPE_ID + id;
        BaseDto.Response cachedResult = (BaseDto.Response) cache.get(key);
        if (cachedResult != null) {
            String logMessage = CACHE_INFO_GET + key;
            log.info(logMessage);
            return cachedResult;
        }
        TypeProduct typeProduct = typeProductRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        BaseDto.Response data = buildTypeProductResponseFromModel(typeProduct);
        cache.put(key, data);
        log.info("information obtained from database (type of product) by id");
        return data;
    }

    @Override
    public BaseDto.Response create(BaseDto.RequestBody createForm) {
        TypeProduct typeProduct = saveTypeProduct(new TypeProduct(), createForm);
        log.info("new information added to database (type of product)");
        return buildTypeProductResponseFromModel(typeProduct);
    }

    @Override
    public BaseDto.Response update(Long id, BaseDto.RequestBody updateForm) {
        TypeProduct typeProduct = typeProductRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        TypeProduct newTypeProduct = saveTypeProduct(typeProduct, updateForm);
        BaseDto.Response data = buildTypeProductResponseFromModel(newTypeProduct);
        String key = TYPE_ID + id;
        cache.remove(key);
        cache.put(key, data);
        String logMessage = CACHE_INFO_UPDATE + key;
        log.info(logMessage);
        log.info("information in the database and cache (if exist) has been updated");
        return data;
    }

    @Override
    public void delete(Long id) {
        String key = TYPE_ID + id;
        cache.remove(key);
        String logMessage = CACHE_INFO_REMOVE + key;
        log.info(logMessage);
        TypeProduct typeProduct = typeProductRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        for (Food food : typeProduct.getFoods()) {
            food.setTypeProduct(null);
            foodRepository.save(food);
        }
        log.info("information in the database and cache (if exist) has been deleted");
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