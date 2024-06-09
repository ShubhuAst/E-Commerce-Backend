package com.bootcamp.project.eCommerce.pojos.orderFlow;

import com.bootcamp.project.eCommerce.pojos.Auditable;
import com.bootcamp.project.eCommerce.pojos.userFlow.Customer;
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
@Table(name = "order_")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends Auditable implements Serializable {

    @Serial
    private static final long serialVersionUID = -7834539896173287719L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    Customer customer;

    Integer amountPaid;

    String paymentMethod;

    String customerAddressCity;

    String customerAddressState;

    String customerAddressCountry;

    String customerAddressAddressLine;

    Integer customerAddressZipCode;

    String customerAddressLabel;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderProduct> orderProducts;

    @Version
    Long version;

    public void setOrderProducts(List<OrderProduct> orderProducts) {

        orderProducts.forEach(orderProduct -> orderProduct.setOrder(this));
        this.orderProducts = orderProducts;
    }

}
