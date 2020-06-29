package com.floyd.ecigmanagement.models;

import lombok.Data;

@Data
public class Preparation {

    private int id;
    private Arome arome;
    private int quantity;
    private int capacity;
}
