package com.yazikochesalna.messagingservice.controller;

import com.yazikochesalna.common.service.JwtService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//TODO: снести когда будет авторизация

@RequiredArgsConstructor
@RestController
@RequestMapping("/test-user")
public class TestUserController {

    private final JwtService jwtService;

    @GetMapping("/user/{UserID}")
    public ResponseEntity<? extends Object> generateJwt(@PathVariable long UserID) {


        return ResponseEntity.ok(new Object() {
            public final String JWT = jwtService.generateAccessToken(UserID);
        });
    }

    @GetMapping("/")
    public ResponseEntity<? extends Object> getJwtInfo() {

        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication());
    }

    @GetMapping("/service")
    public ResponseEntity<? extends Object> generateJwt() {

        return ResponseEntity.ok(new Object() {
            public final String JWT = jwtService.generateServiceToken();
        });
    }

    @GetMapping("/forservice")
    @RolesAllowed("SERVICE")
    public ResponseEntity<? extends Object> testServiceAccess() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication());
    }

}
