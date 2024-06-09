package com.bootcamp.project.eCommerce.pojos.orderFlow;

import com.bootcamp.project.eCommerce.constants.StatusType;
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
public class OrderStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = -1957751720485392952L;

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_product_id")
    OrderProduct orderProduct;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    StatusType oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    StatusType currentStatus;

    String transitionNoteComments;

    @Version
    Long version;

}
