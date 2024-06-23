package com.example.Price.Tracker.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "https://senniamohamed.netlify.app/"
        },
        methods = {
                RequestMethod.OPTIONS,
                RequestMethod.GET,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.POST
        }
)
@NoArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {
private ProductService productService;
@Autowired
    public ProductController(ProductService productService){
    this.productService=productService;
}
@GetMapping("/getAllProducts")
    public List<ProductDTO> getAllProducts(){
    return  productService.getAllProducts();
}
@GetMapping("/getUsersProducts/{id}")
    public List<ProductDTO> getUsersProducts(@PathVariable int id){

return this.productService.getUsersProducts(id);
}
@GetMapping("/getProductById/{id}")
public ProductDTO getProductById(@PathVariable int id){
   return this.productService.getProductById(id);
}
@PostMapping("addProduct")
    public void addProduct(@RequestBody ProductDTO product){
this.productService.addProduct(product);
}
}
