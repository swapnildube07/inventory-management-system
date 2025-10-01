package com.swapnildube.inventory_management.repository;

import com.swapnildube.inventory_management.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRespository extends MongoRepository<User, String> {


    Optional<User> findByEmail(String email);
}
