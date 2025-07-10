package com.yazikochesalna.messagestorageservice.config

import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import kotlin.collections.HashMap
import org.springframework.kafka.core.*
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory


@Configuration
open class KafkaConfig(
    private val kafkaProperties: KafkaProperties
) {
    // Producer возм не нужен
    @Bean
    open fun producerFactory(): ProducerFactory<String, MessagesJsonFormatDTO> {
        val config = HashMap(kafkaProperties.buildProducerProperties())
        return DefaultKafkaProducerFactory(config)
    }

    @Bean
    open fun kafkaTemplate(): KafkaTemplate<String, MessagesJsonFormatDTO> {
        return KafkaTemplate(producerFactory())
    }

    // Consumer
    @Bean
    open fun consumerFactory(): ConsumerFactory<String, MessagesJsonFormatDTO> {
        val props = HashMap(kafkaProperties.buildConsumerProperties()).apply {
            put(ConsumerConfig.GROUP_ID_CONFIG, "message-storage-service-instance-${UUID.randomUUID()}")

            // Можно переопределить десериализаторы, если они не заданы в `application.yml`
            // put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            // put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer::class.java)
        }
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    open fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, MessagesJsonFormatDTO> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, MessagesJsonFormatDTO>()
        factory.consumerFactory = consumerFactory()
        factory.containerProperties.ackMode = kafkaProperties.listener.ackMode
        return factory
    }
}