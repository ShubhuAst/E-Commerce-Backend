package com.bootcamp.project.eCommerce.co_dto.saveCO.filters;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFilterSaveCO extends PageFilterSaveCO {

    String email;
}
