package com.floyd.ecigmanagement.uio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreparationUio {

    private Integer id;
    private String arome;
    private Integer quantity;
    private Integer capacity;
    private String imageUrl;
}
