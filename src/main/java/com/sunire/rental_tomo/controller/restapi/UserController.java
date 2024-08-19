package com.sunire.rental_tomo.controller.restapi;

import com.sunire.rental_tomo.domain.dto.UserJoinRequest;
import com.sunire.rental_tomo.service.UserService;
import com.sunire.rental_tomo.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


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
    public ResponseEntity<Map<String, String>> login(@RequestBody UserJoinRequest userJoinRequest) {
        Map<String, String> token = userService.login(userJoinRequest);
        System.out.println(token);
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/nickname")
    public ResponseEntity<String> nickname(@RequestHeader("Authorization") String token) {
        String name = userService.nickname(token);
        return ResponseEntity.ok().body(name);
    }

    @GetMapping("getId")
    public ResponseEntity<String> getId(@RequestHeader("Authorization") String token) {
        String id = userService.getId(token);
        return ResponseEntity.ok().body(id);
    }
}


