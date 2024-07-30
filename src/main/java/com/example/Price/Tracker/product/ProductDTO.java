package com.example.Price.Tracker.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class  ProductDTO {
    private int id;
    private String name;
        private int ownerId;

}
