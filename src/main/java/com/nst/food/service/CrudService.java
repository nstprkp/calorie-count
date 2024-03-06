package com.nst.food.service;

import java.util.List;

public interface CrudService<T, S> {
    List<T> getAll();

    T get(Long id);

    T create(S createForm);

    T update(Long id, S updateForm);

    void delete(Long id);
}
