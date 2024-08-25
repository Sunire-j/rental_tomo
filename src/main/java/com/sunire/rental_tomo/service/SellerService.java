package com.sunire.rental_tomo.service;

import com.sunire.rental_tomo.domain.entity.SellerItem;
import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.repository.SellerItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerService {

    private final SellerItemRepository sellerItemRepository;

    public List<SellerItem> getUserSelling(User user){
        return sellerItemRepository.findByUser(user);
    }



}
