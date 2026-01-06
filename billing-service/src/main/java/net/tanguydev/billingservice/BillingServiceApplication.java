package net.tanguydev.billingservice;

import net.tanguydev.billingservice.Feign.CustomerRestClient;
import net.tanguydev.billingservice.Feign.ProductRestClient;
import net.tanguydev.billingservice.entities.Bill;
import net.tanguydev.billingservice.entities.Customer;
import net.tanguydev.billingservice.entities.Product;
import net.tanguydev.billingservice.entities.ProductItem;
import net.tanguydev.billingservice.repositories.BillRepository;
import net.tanguydev.billingservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(BillRepository billRepository,
                           ProductItemRepository  productItemRepository,
                           CustomerRestClient customerRestClient,
                           ProductRestClient  productRestClient
                           ) {
        return args -> {
            Collection <Customer> customers = customerRestClient.getCustomers().getContent();
            Collection <Product> products = productRestClient.getProducts().getContent();

            customers.forEach(customer -> {
                Bill bill = Bill.builder()
                        .billingDate(new Date())
                        .customerId(customer.getId())
                        .build();

                billRepository.save(bill);

                products.forEach(product -> {
                    ProductItem productItem = ProductItem.builder()
                            .bill(bill)
                            .productId(product.getId())
                            .quantity(1+ new Random().nextInt(10))
                            .unitPrice(product.getPrice())
                            .build();

                    productItemRepository.save(productItem);
                });
            });
        };
    }

}
