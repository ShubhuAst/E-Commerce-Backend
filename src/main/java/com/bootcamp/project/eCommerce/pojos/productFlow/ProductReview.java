package com.bootcamp.project.eCommerce.pojos.productFlow;

import com.bootcamp.project.eCommerce.pojos.userFlow.Customer;
import jakarta.persistence.*;
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
@IdClass(ProductReviewId.class)
public class ProductReview implements Serializable {

    @Serial
    private static final long serialVersionUID = 2398038303076564620L;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    String review;

    Float rating;

    @Version
    Long version;
}
