package com.yazikochesalna.messagestorageservice.dto.mapper

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.serializers.MessageJsonFormatDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CustomObjectMapper {
    @Bean
    open fun objectMapper(): ObjectMapper {
        //val mapper = ObjectMapper()

        val module = SimpleModule().apply {
            addDeserializer(MessagesJsonFormatDTO::class.java, MessageJsonFormatDeserializer())
        }

        //mapper.registerModule(module)
        return ObjectMapper().apply {
            registerModule(Jdk8Module())
            registerModule(JavaTimeModule())
            registerModule(module)
            registerKotlinModule()
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)

        }
    }
}