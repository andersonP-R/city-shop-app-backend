package com.anderson.cityshop.dao;

import com.anderson.cityshop.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface ICategoryDao extends CrudRepository<Category, Long> {
}
