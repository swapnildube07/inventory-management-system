package com.swapnildube.inventory_management.Controller;

import com.swapnildube.inventory_management.Entity.Product;
import com.swapnildube.inventory_management.Service.ProductSerice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductSerice productService;

    public ProductController(ProductSerice productService) {
        this.productService = productService;
    }

    private String currentOwnerId() {

        return (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts(currentOwnerId());
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product productcreated = productService.createProduct(currentOwnerId(), product);
            return ResponseEntity.status(HttpStatus.CREATED).body(productcreated);
        } catch (IllegalArgumentException e) {
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @GetMapping("/name")
    public ResponseEntity<?> getProductByName(@RequestBody Map<String, String> request) {
        try {
            String productname = request.get("productname");
            Product product = productService.getProductByName(currentOwnerId(), productname);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product having name   "+request.get("productname")+"  not Found");
        }
    }

    @PutMapping("/{name}")
    public ResponseEntity<?> updateProduct(@PathVariable String name,
                                 @RequestBody Product updatedProduct) {
      try {
          Product product = productService.updateProduct(currentOwnerId(), name, updatedProduct);
          return ResponseEntity.ok(product);
      }catch (IllegalArgumentException e){
          return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found");
      }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteProduct(@PathVariable String name) {
        try {
            productService.deleteProduct(currentOwnerId(), name);
            return ResponseEntity.noContent().build();
        }catch (IllegalArgumentException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/increase/{name}")
    public ResponseEntity<?> increaseStock(@PathVariable String name,
                                 @RequestBody Map<String, Integer> request) {
      try {
          int Quantity = request.get("Quantity");
          Product product =  productService.increaseStock(currentOwnerId(), name, Quantity);
          return ResponseEntity.ok(product);
      }
      catch (IllegalArgumentException e){
          return  ResponseEntity.badRequest().body(e.getMessage());
      }
    }

    @PostMapping("/decrease/{name}")
    public ResponseEntity<?> decreaseStock(@PathVariable String name,
                                 @RequestBody Map<String, Integer> request) {
        try {
            int Quantity = request.get("Quantity");
            Product product =  productService.decreaseStock(currentOwnerId(), name, Quantity);
            return  ResponseEntity.ok(product);
        }catch (IllegalArgumentException e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockProducts() {
        try {
            List<Product> products =  productService.getLowStockProducts(currentOwnerId());
            if(products.isEmpty()){
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Low Stock Product Found");
            }
            return  ResponseEntity.ok(products);
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
