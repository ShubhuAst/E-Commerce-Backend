package com.bootcamp.project.eCommerce.pojos.productFlow.category;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryMetadataFieldValue implements Serializable {

    @Serial
    private static final long serialVersionUID = 8039523815973465300L;

    @EmbeddedId
    CategoryMetadataFieldValueID metadataFieldValueID;

    String value;

    @Version
    Long version;
}
