package com.swapnildube.inventory_management.Entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
@Data
@Builder
@Document(collection = "User")
public class User {

    @Id
    private  String id;


    @Email
    @NotBlank(message = "Email is required")
    @Indexed(unique = true)
    @Field("Email")
    private  String email;


    @NotBlank
    @Min(value = 6)
    private  String password;

    @NotBlank
    private String warehousename;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
