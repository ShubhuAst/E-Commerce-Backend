package com.bootcamp.project.eCommerce.pojos.productFlow;

import com.bootcamp.project.eCommerce.pojos.orderFlow.Cart;
import com.bootcamp.project.eCommerce.pojos.orderFlow.OrderProduct;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    Product product;

    Integer quantityAvailable;

    Integer price;

    String metadata;

    String primaryImageName;

    Boolean isActive = true;

    @OneToOne(mappedBy = "productVariation", cascade = CascadeType.ALL)
    OrderProduct orderProduct;

    @OneToMany(mappedBy = "productVariation", cascade = CascadeType.ALL)
    List<Cart> carts;

    public void setOrderProduct(OrderProduct orderProduct) {

        orderProduct.setProductVariation(this);
        this.orderProduct = orderProduct;
    }

    public void setCarts(List<Cart> carts) {

        carts.forEach(cart -> cart.setProductVariation(this));
        this.carts = carts;
    }
}
