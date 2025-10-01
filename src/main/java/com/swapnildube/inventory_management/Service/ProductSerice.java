package com.swapnildube.inventory_management.Service;

import com.swapnildube.inventory_management.Entity.Product;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Component
public interface ProductSerice {
    Product createProduct(String ownerId, Product product);

    List<Product> getAllProducts(String ownerId);



    Product getProductByName(String ownerId, String name);



    Product updateProduct(String ownerId, String name, Product updatedProduct);

    void deleteProduct(String ownerId, String name);

    Product increaseStock(String ownerId, String name, int Quantity);

    Product decreaseStock(String ownerId, String name, int Quantity);

    List<Product> getLowStockProducts(String ownerId);
}