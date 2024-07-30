package com.example.Price.Tracker.Record;

import com.example.Price.Tracker.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordDTO {
    private int id;

    private String product;
    private Date date;
    private float averagePrice;

}
