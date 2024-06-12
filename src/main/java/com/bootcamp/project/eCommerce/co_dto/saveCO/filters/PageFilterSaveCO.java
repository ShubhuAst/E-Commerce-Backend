package com.bootcamp.project.eCommerce.co_dto.saveCO.filters;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class PageFilterSaveCO {

    Integer max;

    Integer offset;

    String sort;

    String order;

}
