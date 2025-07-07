package com.yazikochesalna.messagestorageservice.controller

import com.yazikochesalna.common.service.JwtService
import io.swagger.v3.oas.annotations.Operation
import jakarta.annotation.security.PermitAll
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
open class DebugController(
    @Autowired
    private val jwtService: JwtService
) {
    init {
        println("Ура наконецто мы работаем боже = ${jwtService.hashCode()}")
    }

    //Тащим сервисный токен
    @GetMapping("/service-token")
    fun generateToken() = jwtService.generateServiceToken()
}