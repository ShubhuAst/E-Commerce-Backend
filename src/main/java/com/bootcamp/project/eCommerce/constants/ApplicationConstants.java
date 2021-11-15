package com.bootcamp.project.eCommerce.constants;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum ApplicationConstants {

    MASTER_ADMIN,
    DEFAULT_PRODUCT_IMAGE();

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}