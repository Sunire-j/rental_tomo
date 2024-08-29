package com.sunire.rental_tomo.repository;

import com.sunire.rental_tomo.domain.entity.SellerItem;
import com.sunire.rental_tomo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerItemRepository extends JpaRepository<SellerItem, Long> {
    List<SellerItem> findByUserOrderByCategory(User user);
    void deleteAllByUser(User user);
}
