package com.floyd.ecigmanagement.models;

import lombok.Data;

@Data
public class Preparation {

    private Integer id;
    private Arome arome;
    private Integer quantity;
    private Integer capacity;
}
