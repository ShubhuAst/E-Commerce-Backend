package com.bootcamp.project.eCommerce.co_dto.saveCO;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetadataFieldValueSaveCO {

    @NotNull(message = "Category Id Can't be Null")
    Long categoryId;

    @NotNull(message = "Metadata Field Id Can't be Null")
    Long metadataFieldId;

    @NotNull(message = "Possible Values Can't be Null")
    String values;
}
