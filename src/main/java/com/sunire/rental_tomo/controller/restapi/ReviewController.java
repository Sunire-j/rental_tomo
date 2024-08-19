package com.sunire.rental_tomo.controller.restapi;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @PostMapping("/write")
    public ResponseEntity<String> writeReview(Authentication auth){
        return ResponseEntity.ok().body(auth.getName()+"님, 리뷰 등록 성공");
    }
}
