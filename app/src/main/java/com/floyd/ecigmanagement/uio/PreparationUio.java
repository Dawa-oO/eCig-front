package com.floyd.ecigmanagement.uio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreparationUio {

    private int id;
    private int aromeId;
    private String arome;
    private int quantity;
    private int capacity;
    private String imageUrl;
}
