package com.bootcamp.project.eCommerce.pojos.productFlow;

import com.bootcamp.project.eCommerce.pojos.Auditable;
import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import com.bootcamp.project.eCommerce.pojos.userFlow.Seller;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends Auditable implements Serializable {

    @Serial
    private static final long serialVersionUID = 7684372500993977579L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id")
    Seller seller;

    String name;

    String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    Category category;

    Boolean isCancellable = false;

    Boolean isReturnable = false;

    String brand;

    Boolean isActive = false;

    Boolean isDeleted = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductReview> productReviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductVariation> productVariations;

    @Version
    Long version;

    public void setProductReviews(List<ProductReview> productReviews) {

        productReviews.forEach(productReview -> productReview.setProduct(this));
        this.productReviews = productReviews;
    }

    public void setProductVariations(List<ProductVariation> productVariations) {

        productVariations.forEach(productVariation -> productVariation.setProduct(this));
        this.productVariations = productVariations;
    }
}
