package com.floyd.ecigmanagement.uio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoosterUio {

    private Integer id;
    private Integer quantity;
    private Integer capacity;
    private String pgvg;
    private String imageUrl;
}
