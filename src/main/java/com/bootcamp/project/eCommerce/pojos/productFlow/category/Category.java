package com.bootcamp.project.eCommerce.pojos.productFlow.category;

import com.bootcamp.project.eCommerce.pojos.productFlow.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 954956908956276801L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @JsonIgnore
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    List<Category> childCategories;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_category_id")
    Category parentCategory;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    List<Product> products;

    @Version
    Long version;

    public void setProducts(List<Product> products) {
        products.forEach(product -> product.setCategory(this));
        this.products = products;
    }
}
