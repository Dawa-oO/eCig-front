package com.floyd.ecigmanagement.uio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AromeUio {

    private int id;
    private int quantity;
    private int capacity;
    private String taste;
    private String brand;
    private double note;
    private String imageUrl;
}
