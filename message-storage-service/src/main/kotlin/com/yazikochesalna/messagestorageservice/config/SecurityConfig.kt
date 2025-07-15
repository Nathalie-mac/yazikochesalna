package com.yazikochesalna.messagestorageservice.config

import com.yazikochesalna.common.filter.JwtFilter
import com.yazikochesalna.messagestorageservice.config.properties.FrontProperties
import jakarta.servlet.http.HttpServletRequest
import lombok.RequiredArgsConstructor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import java.util.List

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(jsr250Enabled = true)
@EnableConfigurationProperties(FrontProperties::class)
open class SecurityConfig(private val jwtFilter: JwtFilter,
    private val frontProperties: FrontProperties)
{

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .cors { cors: CorsConfigurer<HttpSecurity?> ->
                cors
                    .configurationSource { request: HttpServletRequest? ->
                        val config = CorsConfiguration()
                        config.allowedOrigins = frontProperties.origins
                        config.allowedMethods = listOf("GET", "POST")
                        config.allowedHeaders = listOf("*")
                        config.allowCredentials = true
                        config
                    }
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/v3/api-docs/**",
                    "/error",
                ).permitAll().anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling{it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))}

        return http.build()
    }

    @Bean
    @Throws(Exception::class)
    open fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}