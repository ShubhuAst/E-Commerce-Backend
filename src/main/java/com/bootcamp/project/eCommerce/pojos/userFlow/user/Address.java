package com.bootcamp.project.eCommerce.pojos.userFlow.user;

import com.bootcamp.project.eCommerce.pojos.userFlow.Customer;
import com.bootcamp.project.eCommerce.pojos.userFlow.Seller;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "City Can't be Null")
    String city;

    @NotNull(message = "State Can't be Null")
    String state;

    @NotNull(message = "Country Can't be Null")
    String country;

    @NotNull(message = "Address Line Can't be Null")
    String addressLine;

    @NotNull(message = "ZIP Code Can't be Null")
    @Min(value = 100000, message = "Invalid ZIP Code")
    @Max(value = 999999, message = "Invalid ZIP Code")
    Integer zipCode;

    @NotNull(message = "Label Can't be Null")
    String label;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return getCity().equals(address.getCity()) && getState().equals(address.getState()) && getCountry().equals(address.getCountry()) && getAddressLine().equals(address.getAddressLine()) && getZipCode().equals(address.getZipCode()) && getLabel().equals(address.getLabel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getState(), getCountry(), getAddressLine(), getZipCode(), getLabel());
    }
}
