package com.swapnildube.inventory_management.impl;

import com.swapnildube.inventory_management.Entity.Product;
import com.swapnildube.inventory_management.Service.ProductSerice;

import com.swapnildube.inventory_management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductSerice {

    private final ProductRepository productRepository;


    @Override
    public Product createProduct(String ownerId, Product product) {

        product.setOwnerId(ownerId);

        Optional<Product> existing  = productRepository.findByOwnerIdAndProductnameIgnoreCase(ownerId, product.getProductname());
        if(existing.isPresent()){
            throw new IllegalArgumentException("Product already exists in inventory. Use update instead of creating.");
        }
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts(String ownerId) {
        return productRepository.findAllByOwnerId(ownerId);
    }



    @Override
    public Product getProductByName(String ownerId, String name) {
        return productRepository.findByOwnerIdAndProductnameIgnoreCase(ownerId, name)
                .orElseThrow(() -> new IllegalArgumentException("Product not found for this owner."));
    }



    @Override
    public Product updateProduct(String ownerId, String productname, Product updatedProduct) {
            Product existing  = getProductByName(ownerId,productname);
            existing.setDescription(updatedProduct.getDescription());
            existing.setProductname(updatedProduct.getProductname());
            existing.setStockQuantity(Math.max(0,updatedProduct.getStockQuantity()));
            existing.setLowStockThreshold(Math.max(0,updatedProduct.getLowStockThreshold()));

            return  productRepository.save(existing);


    }

    @Override
    public void deleteProduct(String ownerId, String name) {
            Product product = getProductByName(ownerId, name);
            productRepository.delete(product);
    }

    @Override
    public Product increaseStock(String ownerId, String productname, int Quantity) {
        if(Quantity<0){
            throw  new IllegalArgumentException("Increase Stock Quantity Must be Positive");
        }

        Product product = getProductByName(ownerId,productname);
        product.setStockQuantity(product.getStockQuantity()+Quantity);
        return productRepository.save(product);
    }

    @Override
    public Product decreaseStock(String ownerId, String ProductName, int Quantity) {
        Product product =getProductByName(ownerId,ProductName);
        if(Quantity<0){
            throw  new IllegalArgumentException("Decrease amount cannot be negative");
        }
        if (product.getStockQuantity() < Quantity) {
            throw new IllegalArgumentException("Cannot decrease more than available stock");
        }

        product.setStockQuantity(product.getStockQuantity()-Quantity);
        return productRepository.save(product);
    }

    @Override
    public List<Product> getLowStockProducts(String ownerId) {
        return productRepository.findLowStockProductsForOwner(ownerId);
    }
}

