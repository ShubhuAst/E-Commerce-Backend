package com.bootcamp.project.eCommerce.pojos.userFlow;

import com.bootcamp.project.eCommerce.pojos.productFlow.Product;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.Address;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PrimaryKeyJoinColumn(name = "seller_user_id")
public class Seller extends User {

    String gst;

    String companyContact;

    String companyName;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    List<Product> products;

    public void setProducts(List<Product> products) {
        products.forEach(product -> product.setSeller(this));
        this.products = products;
    }
}
