package com.shrikantanand.productservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shrikantanand.productservice.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Byte> {

}
