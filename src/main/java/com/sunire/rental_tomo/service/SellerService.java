package com.sunire.rental_tomo.service;

import com.sunire.rental_tomo.domain.entity.Category;
import com.sunire.rental_tomo.domain.entity.SellerItem;
import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.repository.CategoryRepository;
import com.sunire.rental_tomo.repository.SellerItemRepository;
import com.sunire.rental_tomo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerService {

    private final SellerItemRepository sellerItemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    public List<SellerItem> getUserSelling(User user) {
        return sellerItemRepository.findByUserOrderByCategory(user);
    }

    @Transactional
    public void setSellerStatus(Boolean status, User user) {
        if (status) {
            //트루면 is_seller를 켜주고, 깡통값을 넣어줌.
            user.setIsSeller(status);
            userRepository.save(user);

            List<Category> categories = categoryRepository.findChildrenCategories();

            for (Category category : categories) {
                SellerItem sellerItem = SellerItem.builder()
                        .user(user)
                        .category(category)
                        .summary("")
                        .status(false)
                        .build();

                sellerItemRepository.save(sellerItem);
            }

        } else {
            //false면 is_seller를 끄고, 깡통값으로 초기화함<-굳이? 그냥 냅두자
            //오히려 지우는방향으로 가야함. 값이 계속 쌓임
            user.setIsSeller(status);
            userRepository.save(user);

//            List<Category> categories = categoryRepository.findChildrenCategories();

            sellerItemRepository.deleteAllByUser(user);

//            for(Category category : categories){
//                SellerItem sellerItem = SellerItem.builder()
//                        .user(user)
//                        .category(category)
//                        .summary("")
//                        .status(false)
//                        .build();
//
//
//                sellerItemRepository.save(sellerItem);
//        }
        }
    }
}