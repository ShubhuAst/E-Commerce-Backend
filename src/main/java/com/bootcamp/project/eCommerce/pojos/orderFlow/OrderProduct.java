package com.bootcamp.project.eCommerce.pojos.orderFlow;

import com.bootcamp.project.eCommerce.pojos.productFlow.ProductVariation;
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
public class OrderProduct implements Serializable {

    @Serial
    private static final long serialVersionUID = -473255030091659794L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    Order order;

    Integer quantity;

    Integer price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_variation_id")
    ProductVariation productVariation;

    @OneToOne(mappedBy = "orderProduct")
    OrderStatus orderStatus;

    @Version
    Long version;

    public void setOrderStatus(OrderStatus orderStatus) {

        orderStatus.setOrderProduct(this);
        this.orderStatus = orderStatus;
    }
}
