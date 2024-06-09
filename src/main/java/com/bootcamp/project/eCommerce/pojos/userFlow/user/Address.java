package com.bootcamp.project.eCommerce.pojos.userFlow.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address implements Serializable {

    @Serial
    private static final long serialVersionUID = -8466613174178757997L;

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

    @Version
    Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address address)) return false;
        return getCity().equals(address.getCity()) && getState().equals(address.getState()) && getCountry().equals(address.getCountry()) && getAddressLine().equals(address.getAddressLine()) && getZipCode().equals(address.getZipCode()) && getLabel().equals(address.getLabel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getState(), getCountry(), getAddressLine(), getZipCode(), getLabel());
    }
}
