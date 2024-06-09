package com.bootcamp.project.eCommerce.pojos.orderFlow;

import com.bootcamp.project.eCommerce.pojos.Auditable;
import com.bootcamp.project.eCommerce.pojos.productFlow.ProductVariation;
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
public class Cart extends Auditable implements Serializable {

    @Serial
    private static final long serialVersionUID = 3757467198331681898L;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    Customer customer;

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_variation_id")
    ProductVariation productVariation;

    Integer quantity;

    Boolean isWishlistItem = false;

    @Version
    Long version;
}
