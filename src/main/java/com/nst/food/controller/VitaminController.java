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
@RequestMapping("api/vitamins")
public class VitaminController {
    private final CrudService<BaseDto.Response, BaseDto.RequestBody> vitaminService;

    @GetMapping
    public ResponseEntity<List<BaseDto.Response>> getAllVitamins() {
        List<BaseDto.Response> timeZones = vitaminService.getAll();
        return new ResponseEntity<>(timeZones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseDto.Response> getVitaminsById(@PathVariable Long id) {
        BaseDto.Response timeZone = vitaminService.get(id);
        return new ResponseEntity<>(timeZone, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseDto.Response> createVitamin(@RequestBody BaseDto.RequestBody vitamin) {
        return new ResponseEntity<>(vitaminService.create(vitamin), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseDto.Response> updateVitamin(@PathVariable Long id, @RequestBody BaseDto.RequestBody vitamin) {
        return new ResponseEntity<>(vitaminService.update(id, vitamin), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVitamin(@PathVariable Long id) {
        vitaminService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}