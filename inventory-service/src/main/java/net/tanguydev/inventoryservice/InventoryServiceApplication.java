package net.tanguydev.inventoryservice;

import net.tanguydev.inventoryservice.Entities.Product;
import net.tanguydev.inventoryservice.Repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder().name("Yaourt").price(1200.0).quantity(20).build());
            productRepository.save(Product.builder().name("Milk").price(700.0).quantity(13).build());
            productRepository.save(Product.builder().name("Sugar").price(1300.0).quantity(40).build());
            productRepository.save(Product.builder().name("Coockies").price(900.0).quantity(20).build());
            productRepository.save(Product.builder().name("Cake").price(1200.0).quantity(14).build());
        };
    }

}
