package com.bootcamp.project.eCommerce.co_dto.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaxMinInProduct {

    Long id;

    String productName;

    Integer maximumPrice;

    Integer minimumPrice;
}
