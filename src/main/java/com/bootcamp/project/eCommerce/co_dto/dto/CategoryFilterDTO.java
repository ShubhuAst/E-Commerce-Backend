package com.bootcamp.project.eCommerce.co_dto.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryFilterDTO {

    List<FieldValueDTO> fieldValues;

    List<MaxMinInProduct> productMAX_MIN;

    Set<String> brands;
}
