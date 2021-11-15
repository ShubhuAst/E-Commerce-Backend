package com.bootcamp.project.eCommerce.pojos.productFlow;

import com.bootcamp.project.eCommerce.pojos.productFlow.category.Category;
import com.bootcamp.project.eCommerce.pojos.userFlow.Seller;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_user_id")
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

    @Temporal(TemporalType.TIMESTAMP)
    Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    Date lastUpdated;

    String createdBy;

    String updatedBy;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductReview> productReviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<ProductVariation> productVariations;

    public void setProductReviews(List<ProductReview> productReviews) {

        productReviews.forEach(productReview -> productReview.setProduct(this));
        this.productReviews = productReviews;
    }

    public void setProductVariations(List<ProductVariation> productVariations) {

        productVariations.forEach(productVariation -> productVariation.setProduct(this));
        this.productVariations = productVariations;
    }

    @PrePersist
    protected void onCreate() {
        dateCreated = new Date();
        createdBy = seller.getEmail();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = new Date();
        updatedBy = seller.getEmail();
    }
}
