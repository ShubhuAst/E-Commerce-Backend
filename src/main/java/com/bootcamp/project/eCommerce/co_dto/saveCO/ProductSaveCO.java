package com.bootcamp.project.eCommerce.co_dto.saveCO;

import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import com.bootcamp.project.eCommerce.pojos.userFlow.Seller;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSaveCO {

    @NotNull(message = "Product name Can't be Null")
    String name;

    String description;

    @NotNull(message = "Category Id Can't be Null")
    Long categoryId;

    Boolean isCancellable;

    Boolean isReturnable;

    @NotNull(message = "Brand Can't be Null")
    String brand;
}
