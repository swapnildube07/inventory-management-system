package com.swapnildube.inventory_management.ProductServiceimpleTest;

import com.swapnildube.inventory_management.Entity.Product;
import com.swapnildube.inventory_management.impl.ProductServiceImpl;
import com.swapnildube.inventory_management.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class decreaseStockTest {


    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;


    @BeforeEach
    void setup(){

        MockitoAnnotations.openMocks(this);
        product = new Product();
        product.setOwnerId("owner123");
        product.setProductname("Apples");
        product.setStockQuantity(10);
    }
    @Test
    void decreaseStock(){
        when(productRepository.findByOwnerIdAndProductnameIgnoreCase("owner123", "Apples"))
                .thenReturn(Optional.of(product));


        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        Product updated = productService.decreaseStock("owner123", "Apples", 5);

        assertEquals(5, updated.getStockQuantity());
        verify(productRepository, times(1)).save(updated);
    }


    @Test
    void  decreaseStokcthanAvailable(){
        when(productRepository.findByOwnerIdAndProductnameIgnoreCase("owner123", "Apples"))
                .thenReturn(Optional.of(product));


        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);


        assertThrows(IllegalArgumentException.class,()->{
            productService.decreaseStock("owner123","Apples",30);
        });
        verify(productRepository, never()).save(any(Product.class));
    }
}
