package com.example.Price.Tracker.product;

import com.example.Price.Tracker.Mapper;
import com.example.Price.Tracker.Record.Record;
import com.example.Price.Tracker.Record.RecordService;
import com.example.Price.Tracker.Record.Website;
import com.example.Price.Tracker.Scrapper;
import com.example.Price.Tracker.user.User;
import com.example.Price.Tracker.user.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.font.TextHitInfo;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
@Service
public class ProductService {
    private ProductRepository productRepository;
    private Mapper mapper;
    private UserRepository userRepository;
    private RecordService recordService;
    LinkedList<Scrapper> scrappers;
    @Autowired
    public ProductService(ProductRepository productRepository, Mapper mapper, UserRepository userRepository, RecordService recordService){
        this.productRepository=productRepository;
        this.mapper=mapper;
        this.userRepository=userRepository;
        this.recordService=recordService;
        this.scrappers=new LinkedList<Scrapper>();
    }
    public List<ProductDTO> getAllProducts(){
        return this.productRepository.findAll().stream().map(mapper::toProduDTO).collect(Collectors.toList());
    }
    public List<ProductDTO> getUsersProducts(int id){

    return  this.productRepository.findByOwner(id).stream().map(mapper::toProduDTO).collect(Collectors.toList());
    }
    public ProductDTO getProductById( int id){
      Optional<Product> product= this.productRepository.findById(id);
    if(product.isPresent()){
        return this.mapper.toProduDTO(product.get());
    }else {
        return null;
    }
    }
    public void addProduct(ProductDTO productDTO){
        Product product=this.mapper.toProduct(productDTO);
        Optional<User> owner=this.userRepository.findById(productDTO.getOwnerId());
        owner.ifPresent(product::setOwner);

        for (Website site : Website.values()) {
            this.scrappers.add(new Scrapper(product.getName(),site));
            this.scrappers.getLast().start();

        }
        System.out.println("started");
        //this.productRepository.save(product);
    }
}
