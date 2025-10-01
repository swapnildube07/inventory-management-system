package com.swapnildube.inventory_management.Entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Products")
@CompoundIndexes({
        @CompoundIndex(def = "{'ownerId':1,'productname':1}", unique = true)

})

public class Product  {

    @Id
    private String id;


    @Indexed
    @NotBlank(message = "Owner Id required")
    private String  ownerId;


    @Indexed(unique = true)
    @NotBlank(message="Product Name Cannot be Black")
    @Size(min=3,max= 100)
    private String productname;


    private String description;


    @Min(value = 0,message = "Stock quantity can't be negative")
    private int stockQuantity;


    @Min(value = 0,message = "Low Threshold can't be Negative")
    private int lowStockThreshold;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


}
