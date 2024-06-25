package com.example.Price.Tracker.Record;

import com.example.Price.Tracker.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Record {
    @Id
    @SequenceGenerator(
            name = "record_sequence",
            sequenceName = "record_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "record_sequence"
    )
    private int id;

    @ManyToOne()
    @JoinColumn(name = "productId")
    private Product product;
    private Date date;
    private float averagePrice;

    public Record( Product product, Date date, float averagePrice) {

        this.product = product;
        this.date = date;
        this.averagePrice = averagePrice;
    }
}
