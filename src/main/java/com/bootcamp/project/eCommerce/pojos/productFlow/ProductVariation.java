package com.bootcamp.project.eCommerce.pojos.productFlow;

import com.bootcamp.project.eCommerce.pojos.orderFlow.Cart;
import com.bootcamp.project.eCommerce.pojos.orderFlow.OrderProduct;
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
public class ProductVariation implements Serializable {

    @Serial
    private static final long serialVersionUID = -5387458971183104967L;

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

    @Version
    Long version;

    public void setOrderProduct(OrderProduct orderProduct) {

        orderProduct.setProductVariation(this);
        this.orderProduct = orderProduct;
    }

    public void setCarts(List<Cart> carts) {

        carts.forEach(cart -> cart.setProductVariation(this));
        this.carts = carts;
    }
}
