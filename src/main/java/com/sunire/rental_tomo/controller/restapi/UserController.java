package com.sunire.rental_tomo.controller.restapi;

import com.sunire.rental_tomo.domain.dto.UserJoinRequest;
import com.sunire.rental_tomo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest userJoinRequest) {
        log.info("UserJoinRequest: {}", userJoinRequest);
        String hi = userService.join(userJoinRequest);
        return new ResponseEntity<>("User joined successfully", HttpStatus.OK);
    }



    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserJoinRequest userJoinRequest) {
        String token = userService.login(userJoinRequest);
        return ResponseEntity.ok().body(token);
    }
}


