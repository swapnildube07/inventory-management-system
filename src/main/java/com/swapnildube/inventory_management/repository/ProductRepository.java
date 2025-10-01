package com.swapnildube.inventory_management.repository;


import com.swapnildube.inventory_management.Entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product,String> {

  List<Product> findAllByOwnerId(String ownerId);

  Optional<Product> findByOwnerIdAndProductnameIgnoreCase(String ownerId, String productname);


  @Query("{ 'ownerId': ?0, $expr: { $lte: [ '$stockQuantity', '$lowStockThreshold' ] } }")
  List<Product> findLowStockProductsForOwner(String ownerId);



}
