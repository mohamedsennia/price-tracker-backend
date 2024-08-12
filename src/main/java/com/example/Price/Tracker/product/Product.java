package com.example.Price.Tracker.product;

import com.example.Price.Tracker.Record.Record;
import com.example.Price.Tracker.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @SequenceGenerator(
            name ="Product_sequence",
            sequenceName = "Product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
generator ="Product_sequence"
    )
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToMany(mappedBy = "product")
    private List<Record> records;
    @Column(columnDefinition = "boolean default true")
    private boolean activated;
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                '}';
    }
    public  void toggleProductActivition(){
        this.activated=!this.activated;
    }
}
