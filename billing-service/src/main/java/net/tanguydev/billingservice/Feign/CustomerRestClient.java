package net.tanguydev.billingservice.Feign;

import net.tanguydev.billingservice.entities.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service")
public interface CustomerRestClient {
    @GetMapping("/customers/{id}")
    public Customer getCustomerById(@PathVariable  Long id);

    @GetMapping("/customers")
    PagedModel<Customer> getCustomers();
}
