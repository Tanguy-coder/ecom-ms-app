package net.tanguydev.billingservice.web;

import net.tanguydev.billingservice.Feign.CustomerServiceRestClient;
import net.tanguydev.billingservice.Feign.ProductServiceRestClient;
import net.tanguydev.billingservice.entities.Bill;
import net.tanguydev.billingservice.model.Customer;
import net.tanguydev.billingservice.repositories.BillRepository;
import net.tanguydev.billingservice.repositories.ProductItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BillRestController {
    private  BillRepository billRepository;
    private  ProductItemRepository productItemRepository;
    private  CustomerServiceRestClient customerServiceRestClient;
    private  ProductServiceRestClient productServiceRestClient;

    public BillRestController(BillRepository billRepository, ProductItemRepository productItemRepository, CustomerServiceRestClient customerServiceRestClient, ProductServiceRestClient productServiceRestClient) {
        this.billRepository = billRepository;
        this.productItemRepository = productItemRepository;
        this.customerServiceRestClient = customerServiceRestClient;
        this.productServiceRestClient = productServiceRestClient;
    }


    @GetMapping("/bills/{id}")
    public Bill getBillById(@PathVariable Long id) {
        Bill bil = billRepository.findById(id).get();
        Customer customer = customerServiceRestClient.getCustomerById(bil.getCustomerId());
        bil.setCustomer(customer);
        bil.getProductItems().forEach(pi ->
                pi.setProduct(productServiceRestClient.getProductById(pi.getProductId())));
        return bil;
    }

}
