package com.bootcamp.project.eCommerce.config;

import com.bootcamp.project.eCommerce.pojos.userFlow.user.User;
import com.bootcamp.project.eCommerce.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class AsynchConfiguration {

    final UserRepository userRepository;

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsynchThread-");
        executor.initialize();
        return executor;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkPasswordExpiryEveryNight() {

        List<User> userList = userRepository.findAll();

        userList.forEach(user -> {
            LocalDate currentDate = LocalDate.now();
            LocalDate passwordLastUpdated = user.getPasswordLastUpdated().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate lastUpdatedPlus90Days = passwordLastUpdated.plusDays(90);

            if (currentDate.isBefore(lastUpdatedPlus90Days)) {
                user.setIsCredentialsExpired(true);
                userRepository.save(user);
            }
        });
    }
}