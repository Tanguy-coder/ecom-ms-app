package net.tanguydev.billingservice;

import net.tanguydev.billingservice.Feign.CustomerServiceRestClient;
import net.tanguydev.billingservice.Feign.ProductServiceRestClient;
import net.tanguydev.billingservice.entities.Bill;
import net.tanguydev.billingservice.model.Customer;
import net.tanguydev.billingservice.model.Product;
import net.tanguydev.billingservice.entities.ProductItem;
import net.tanguydev.billingservice.repositories.BillRepository;
import net.tanguydev.billingservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(BillRepository billRepository,
                           ProductItemRepository  productItemRepository,
                           CustomerServiceRestClient customerServiceRestClient,
                           ProductServiceRestClient productServiceRestClient
                           ) {
        return args -> {
            List<Long> productIds = List.of(1L, 2L, 3L);
            List<Long> customerIds = List.of(1L, 2L, 3L);

            customerIds.forEach(client -> {
                Bill bill = new Bill();
                bill.setCustomerId(client);
                bill.setBillingDate(new Date());
                billRepository.save(bill);

                productIds.forEach(productId -> {
                    ProductItem productItem = new ProductItem();
                    productItem.setProductId(productId);
                    productItem.setUnitPrice(1000*Math.random()*6000);
                    productItem.setQuantity(new Random().nextInt(20)+1);
                    productItem.setProductId(productId);
                    productItem.setBill(bill);
                    productItemRepository.save(productItem);
                });

                });
        };
    }

}
