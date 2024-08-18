package com.example.Price.Tracker;

import com.example.Price.Tracker.Record.Record;
import com.example.Price.Tracker.Record.RecordDTO;
import com.example.Price.Tracker.product.Product;
import com.example.Price.Tracker.product.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapper {
public Product toProduct(ProductDTO productDTO){
    return  new Product(productDTO.getId(),productDTO.getName(),null, List.of(),productDTO.isActivated(),productDTO.isFinishedIntialScrapping());
}
public ProductDTO toProduDTO(Product product){
    return  new ProductDTO(product.getId(),product.getName(),product.getOwner().getId(),product.isActivated(),product.isFinishedIntialScrapping());
}
public Record toRecord(RecordDTO recordDTO){
    return new Record(recordDTO.getId(),null,recordDTO.getDate(),recordDTO.getAveragePrice());
}
public  RecordDTO toRecordDTO(Record record){
    return  new RecordDTO(record.getId(),record.getProduct().getName(),record.getDate(),record.getAveragePrice());
}
}
