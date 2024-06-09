package com.bootcamp.project.eCommerce.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AppData {

    MASTER_ADMIN,
    DEFAULT_PRODUCT_IMAGE;

    private String data;

    public void setData(String data) {
        this.data = data;
    }
}