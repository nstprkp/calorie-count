package com.nst.food.controller;

import com.nst.food.model.dto.BaseDto;
import com.nst.food.service.CrudService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/type-product")
public class TypeProductController {
    private final CrudService<BaseDto.Response, BaseDto.RequestBody> typeProductService;

    @GetMapping
    public ResponseEntity<List<BaseDto.Response>> getAllTypeProduct() {
        List<BaseDto.Response> typeProducts = typeProductService.getAll();
        return new ResponseEntity<>(typeProducts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseDto.Response> getTypeProductById(@PathVariable Long id) {
        BaseDto.Response typeProduct = typeProductService.get(id);
        return new ResponseEntity<>(typeProduct, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseDto.Response> createTypeProduct(@RequestBody BaseDto.RequestBody typeProduct) {
        return new ResponseEntity<>(typeProductService.create(typeProduct), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseDto.Response> updateTypeProduct(@PathVariable Long id, @RequestBody BaseDto.RequestBody typeProduct) {
        return new ResponseEntity<>(typeProductService.update(id, typeProduct), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeProduct(@PathVariable Long id) {
        typeProductService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}