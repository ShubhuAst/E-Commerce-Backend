package com.bootcamp.project.eCommerce.pojos.productFlow.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryMetadataFieldValue implements Serializable {

    static final Long serialVersionUID = 1L;
    
    @EmbeddedId
    CategoryMetadataFieldValueID metadataFieldValueID;

    String values_;
}
