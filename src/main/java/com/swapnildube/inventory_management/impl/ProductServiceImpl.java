package com.swapnildube.inventory_management.impl;

import com.swapnildube.inventory_management.Entity.Product;
import com.swapnildube.inventory_management.Service.ProductSerice;

import com.swapnildube.inventory_management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductSerice {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    @Qualifier("productListRedisTemplate")
    private final RedisTemplate<String, List<Product>> productListRedisTemplate;

    private void evictProductsCache(String ownerId) {
        if (productListRedisTemplate == null) {
            return;
        }
        productListRedisTemplate.delete("products:owner:" + ownerId);
    }


    @Override
    public Product createProduct(String ownerId, Product product) {

        product.setOwnerId(ownerId);

        Optional<Product> existing  = productRepository.findByOwnerIdAndProductnameIgnoreCase(ownerId, product.getProductname());
        if(existing.isPresent()){
            throw new IllegalArgumentException("Product already exists in inventory. Use update instead of creating.");
        }
        Product saved = productRepository.save(product);
        evictProductsCache(ownerId);
        return saved;
    }

    //@Override
//    public List<Product> getAllProducts(String ownerId) {
//        String key = "products:owner:" + ownerId;
//
//        // First, try to serve from cache to avoid hitting MongoDB when possible
//        if (productListRedisTemplate == null) {
//            return productRepository.findAllByOwnerId(ownerId);
//        }
//        List<Product> cached = productListRedisTemplate.opsForValue().get(key);
//        if (cached != null) {
//            return cached;
//        }
//        List<Product> products = productRepository.findAllByOwnerId(ownerId);
//
//        // Cache the fresh list for 15 minutes for faster subsequent reads
//        productListRedisTemplate.opsForValue().set(
//                key,
//                products,
//                15,
//                TimeUnit.MINUTES
//        );
//
//        return products;
//    }
    public List<Product> getAllProducts(String ownerId) {
        String key = "products:owner:" + ownerId;

        if (productListRedisTemplate != null) {
            List<Product> cached = productListRedisTemplate.opsForValue().get(key);
            if (cached != null) {
                return cached;
            }
        }

        List<Product> products = productRepository.findAllByOwnerId(ownerId);

        if (productListRedisTemplate != null) {
            productListRedisTemplate.opsForValue().set(
                    key,
                    products,
                    15,
                    TimeUnit.MINUTES
            );
        }

        return products;
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
            evictProductsCache(ownerId);
            return  productRepository.save(existing);




    }

    @Override
    public void deleteProduct(String ownerId, String name) {
            Product product = getProductByName(ownerId, name);
            productRepository.delete(product);
            evictProductsCache(ownerId);
    }

    @Override
    public Product increaseStock(String ownerId, String productname, int Quantity) {
        if(Quantity<0){
            throw  new IllegalArgumentException("Increase Stock Quantity Must be Positive");
        }

        Product product = getProductByName(ownerId,productname);
        product.setStockQuantity(product.getStockQuantity()+Quantity);
        Product saved = productRepository.save(product);
        evictProductsCache(ownerId);
        return saved;
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
        Product saved = productRepository.save(product);
        evictProductsCache(ownerId);
        return saved;
    }

    @Override
    public List<Product> getLowStockProducts(String ownerId) {
        return productRepository.findLowStockProductsForOwner(ownerId);
    }
}
