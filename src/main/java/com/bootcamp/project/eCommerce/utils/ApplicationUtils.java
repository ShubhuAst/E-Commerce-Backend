package com.bootcamp.project.eCommerce.utils;

import com.bootcamp.project.eCommerce.config.SpringContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

public class ApplicationUtils {

    public static boolean isProfileActive(String profile) {
        return Arrays.asList(SpringContext.getBean(Environment.class).getActiveProfiles()).contains(profile);
    }

    public static String getConfig(String property) {
        return SpringContext.getBean(Environment.class).getProperty(property);
    }

}
