package com.bootcamp.project.eCommerce.co_dto.saveCO;

import com.bootcamp.project.eCommerce.pojos.userFlow.user.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSaveCO {

    @NotNull(message = "Email Id Can't be Null")
    @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
            message = "Invalid Email Id")
    String email;

    @NotNull(message = "First Name Can't be Null")
    String firstName;

    String middleName;

    @NotNull(message = "Last Name Can't be Null")
    String lastName;

    @NotNull(message = "Phone Number Can't be Null")
    @Pattern(regexp = "(0|91)?[7-9][0-9]{9}", message = "Invalid Contact Number")
    String contact;

    @NotNull(message = "Password Can't be Null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()]).{8,15}$",
            message = "Invalid Password")
    String password;

    @NotNull(message = "Confirm Password Can't be Null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()]).{8,15}$",
            message = "Confirm Password Mismatch")
    String confirmPassword;

    Address address;

    MultipartFile image;

}
