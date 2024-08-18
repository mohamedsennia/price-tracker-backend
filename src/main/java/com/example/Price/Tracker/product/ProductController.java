package com.example.Price.Tracker.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "https://senniamohamed.netlify.app/",
                "https://trackprices.netlify.app/"
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
    @PutMapping("/toggleProductActivition/{id}")
    public void toggleProductActivition(@PathVariable int id){this.productService.toggleProductActivition(id);}
    @GetMapping("/isActivated/{id}")
    public boolean isActivated(@PathVariable int id){return  this.productService.isActivated(id);}
    @GetMapping("/finishedIntialScrapping/{id}")
    public boolean finishedIntialScrapping(@PathVariable int id){return  this.productService.finishedIntialScrapping(id);}
}
