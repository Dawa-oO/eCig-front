package com.floyd.ecigmanagement.uio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoosterUio {

    private int id;
    private int quantity;
    private int capacity;
    private String pgvg;
    private String imageUrl;
}
