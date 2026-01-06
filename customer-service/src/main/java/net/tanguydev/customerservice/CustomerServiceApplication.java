package net.tanguydev.customerservice;

import net.tanguydev.customerservice.Entities.Customer;
import net.tanguydev.customerservice.Repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository repository) {
        return args -> {
            repository.save(Customer.builder().name("Tanguy").email("tanguy@gmail.com").build());
            repository.save(Customer.builder().name("Yaya").email("yaya@gmail.com").build());
            repository.save(Customer.builder().name("Renaud").email("ren@gmail.com").build());
        };
    }

}
