package com.bootcamp.project.eCommerce.pojos.orderFlow;

import com.bootcamp.project.eCommerce.constants.StatusType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatus implements Serializable {

    static final Long serialVersionUID = 1L;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_product_id")
    OrderProduct orderProduct;

    @Enumerated(EnumType.STRING)
    StatusType fromStatus;

    @Enumerated(EnumType.STRING)
    StatusType toStatus;

    String transitionNoteComments;
}
