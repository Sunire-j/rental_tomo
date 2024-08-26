package com.sunire.rental_tomo.controller.restapi;

import com.sunire.rental_tomo.domain.dto.ItemAttrEditRequest;
import com.sunire.rental_tomo.domain.entity.Category;
import com.sunire.rental_tomo.domain.entity.SellerItem;
import com.sunire.rental_tomo.domain.entity.User;
import com.sunire.rental_tomo.repository.SellerItemRepository;
import com.sunire.rental_tomo.service.SellerService;
import com.sunire.rental_tomo.service.SmallService;
import com.sunire.rental_tomo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final UserService userService;
    private final SmallService smallService;
    private final SellerItemRepository sellerItemRepository;

    @PostMapping("/onoff")
    public ResponseEntity<String> onoff(@RequestBody Map<String, Boolean> body, HttpServletRequest request) {

        String name = userService.isLogin(request);
        User user = userService.userInfo(name).orElse(null);

        Boolean status = body.get("status");

        sellerService.setSellerStatus(status, user);
        //이걸로 켜면

        return ResponseEntity.ok(status.toString());
    }

    @PostMapping("/itemattrchange")
    public ResponseEntity<String> itemattrchange(@RequestBody Map<String, ItemAttrEditRequest> settings, HttpServletRequest request) {

        String name = userService.isLogin(request);
        User user = userService.userInfo(name).orElse(null);

        settings.forEach((id, setting) -> {
            //잘 들어오는중
            //필요한 객체 = 카테고리, user

            Category cat = smallService.getCategory(Long.valueOf(id)).orElse(null);
            int i = 0;

            SellerItem selleritem = SellerItem.builder().
                    user(user).
                    category(cat)
                    .summary(setting.getText())
                    .status(setting.isChecked())
                    .build();

            sellerItemRepository.save(selleritem);
        });

        return ResponseEntity.ok("ok");
    }

}
