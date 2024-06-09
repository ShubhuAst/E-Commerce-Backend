package com.bootcamp.project.eCommerce;

import com.bootcamp.project.eCommerce.constants.AppData;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.Address;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.GrantedAuthorityImpl;
import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import com.bootcamp.project.eCommerce.repos.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationStartupBootstrap {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        User user = userRepository.findByEmail("asthana.shubham99@gmail.com");
        if (user != null) {
            AppData.MASTER_ADMIN.setData("asthana.shubham99@gmail.com");
            return;
        }
        if (AppData.MASTER_ADMIN.getData() == null) {

            User master_admin = new User();

            master_admin.setEmail("asthana.shubham99@gmail.com");
            master_admin.setFirstName("Shubham");
            master_admin.setLastName("Asthana");
            String password = passwordEncoder.encode("100Million$");
            master_admin.setPasswordHash(password);

            Address mAdmin_address = new Address();
            mAdmin_address.setCity("Lucknow");
            mAdmin_address.setState("Uttar Pradesh");
            mAdmin_address.setCountry("India");
            mAdmin_address.setAddressLine("8/66 Vikas Nagar");
            mAdmin_address.setZipCode(226022);
            mAdmin_address.setLabel("Office");
            master_admin.setContact("8888999900");

            master_admin.setAddresses(List.of(mAdmin_address));

            GrantedAuthorityImpl mAdmin_authority = new GrantedAuthorityImpl();
            mAdmin_authority.setAuthority("ROLE_ADMIN");

            master_admin.setGrantedAuthorities(List.of(mAdmin_authority));
            master_admin.setIsActive(true);

            userRepository.save(master_admin);
            AppData.MASTER_ADMIN.setData("asthana.shubham99@gmail.com");
        }
    }
}
