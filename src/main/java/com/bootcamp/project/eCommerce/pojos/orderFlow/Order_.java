package com.bootcamp.project.eCommerce.pojos.orderFlow;

import com.bootcamp.project.eCommerce.pojos.userFlow.Customer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order_ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_user_id")
    Customer customer;

    Integer amountPaid;

    String paymentMethod;

    String customerAddressCity;

    String customerAddressState;

    String customerAddressCountry;

    String customerAddressAddressLine;

    Integer customerAddressZipCode;

    String customerAddressLabel;

    @Temporal(TemporalType.TIMESTAMP)
    Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    Date lastUpdated;

    String createdBy;

    String updatedBy;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderProduct> orderProducts;

    public void setOrderProducts(List<OrderProduct> orderProducts) {

        orderProducts.forEach(orderProduct -> orderProduct.setOrder(this));
        this.orderProducts = orderProducts;
    }

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
