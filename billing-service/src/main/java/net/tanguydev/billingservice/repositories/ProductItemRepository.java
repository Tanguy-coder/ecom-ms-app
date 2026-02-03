package net.tanguydev.billingservice.repositories;

import net.tanguydev.billingservice.entities.Bill;
import net.tanguydev.billingservice.entities.ProductItem;
import net.tanguydev.billingservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    List<ProductItem> findByBillId(Long billId);
}
