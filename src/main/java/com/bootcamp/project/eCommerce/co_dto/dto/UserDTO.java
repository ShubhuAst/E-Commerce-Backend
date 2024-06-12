package com.bootcamp.project.eCommerce.co_dto.dto;

import com.bootcamp.project.eCommerce.pojos.userFlow.user.Address;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    Long id;

    String email;

    String firstName;

    String middleName;

    String lastName;

    String contact;

    String password;

    String gst;

    String companyName;

    boolean isDeleted;

    boolean isActive;

    boolean isExpired;

    boolean isCredentialsExpired;

    boolean isLocked;

    int invalidAttemptCount;

    @Temporal(TemporalType.DATE)
    Date dateCreated;

    @Temporal(TemporalType.DATE)
    Date lastUpdated;

    String createdBy;

    String updatedBy;

    String token;

    List<Address> address;

}
