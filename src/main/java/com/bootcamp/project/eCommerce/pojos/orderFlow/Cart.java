package com.bootcamp.project.eCommerce.pojos.orderFlow;

import com.bootcamp.project.eCommerce.pojos.productFlow.ProductVariation;
import com.bootcamp.project.eCommerce.pojos.userFlow.Customer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart implements Serializable {

    static final Long serialVersionUID = 1L;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_user_id")
    Customer customer;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_variation_id")
    ProductVariation productVariation;

    Integer quantity;

    Boolean isWishlistItem = false;

    @Temporal(TemporalType.TIMESTAMP)
    Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    Date lastUpdated;

    String createdBy;

    String updatedBy;

    @PrePersist
    protected void onCreate() {
        dateCreated = new Date();
        createdBy = customer.getEmail();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = new Date();
        updatedBy = customer.getEmail();
    }
}
