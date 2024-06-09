package com.bootcamp.project.eCommerce.pojos.userFlow;

import com.bootcamp.project.eCommerce.pojos.orderFlow.Cart;
import com.bootcamp.project.eCommerce.pojos.orderFlow.Order;
import com.bootcamp.project.eCommerce.pojos.productFlow.ProductReview;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PrimaryKeyJoinColumn(name = "customer_user_id")
public class Customer extends User {

    @Serial
    private static final long serialVersionUID = -664173170934806469L;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<ProductReview> productReviews;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<Order> orders;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    Cart cart;

    public void setCart(Cart cart) {
        cart.setCustomer(this);
        this.cart = cart;
    }

    public void setProductReviews(List<ProductReview> productReviews) {
        productReviews.forEach(productReview -> productReview.setCustomer(this));
        this.productReviews = productReviews;
    }

    public void setOrders(List<Order> orders) {
        orders.forEach(order_ -> order_.setCustomer(this));
        this.orders = orders;
    }

}
