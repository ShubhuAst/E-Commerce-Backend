package com.bootcamp.project.eCommerce.pojos.productFlow.category;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class CategoryMetadataFieldValueID implements Serializable {

    @JoinColumn(name = "category_id")
    Long categoryId;

    @JoinColumn(name = "category_metadata_field_id")
    Long metadataFieldId;
}
