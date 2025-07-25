package com.yazikochesalna.messagestorageservice.config

import com.fasterxml.jackson.databind.JsonDeserializer
import com.yazikochesalna.messagestorageservice.config.properties.CassandraProperties
import com.yazikochesalna.messagestorageservice.dto.MessagesJsonFormatDTO
import com.yazikochesalna.messagestorageservice.dto.serializers.MessageJsonFormatDeserializer
//import com.yazikochesalna.messagestorageservice.dto.serializers.KafkaMessageJsonDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import kotlin.collections.HashMap
import org.springframework.kafka.core.*
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.listener.CommonErrorHandler
import org.springframework.kafka.listener.DefaultErrorHandler

@EnableConfigurationProperties(KafkaProperties::class)
@Configuration
open class KafkaConfig(
    private val kafkaProperties: KafkaProperties
) {
    // Producer возм не нужен
//    @Bean
//    open fun producerFactory(): ProducerFactory<String, MessagesJsonFormatDTO> {
//        val config = HashMap(kafkaProperties.buildProducerProperties())
//        return DefaultKafkaProducerFactory(config)
//    }

//    @Bean
//    open fun kafkaTemplate(): KafkaTemplate<String, MessagesJsonFormatDTO> {
//        return KafkaTemplate(producerFactory())
//    }

    // Consumer
    @Bean
    open fun consumerFactory(): ConsumerFactory<String, MessagesJsonFormatDTO> {
        val props = HashMap(kafkaProperties.buildConsumerProperties()).apply {
            //put(JsonDeserializer,  MessagesJsonFormatDTO::class.java)
        //put(JsonDeserializer., MessagesJsonFormatDTO::class.java)
            //put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.consumer.groupId)
            //put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            //put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageJsonFormatDeserializer::class.java)
            //put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer::class.java)
            //put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            //put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer::class.java)
            //put(Jso, MessagesJsonFormatDTO::class.java)
            //put(JsonDeserializer.TRUSTED_PACKAGES, "com.yazikochesalna.messagestorageservice.dto")

        }
        return DefaultKafkaConsumerFactory(props/* StringDeserializer(), MessageJsonFormatDeserializer()*/ )
    }

    @Bean
    open fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, MessagesJsonFormatDTO> =
        ConcurrentKafkaListenerContainerFactory<String, MessagesJsonFormatDTO>().apply {
            consumerFactory = consumerFactory()
            containerProperties.ackMode = kafkaProperties.listener.ackMode
            containerProperties.groupId = kafkaProperties.consumer.groupId
            isBatchListener = true
            setConcurrency(1)
        }
        /*{
        val factory = ConcurrentKafkaListenerContainerFactory<String, MessagesJsonFormatDTO>()
        factory.consumerFactory = consumerFactory()
        factory.containerProperties.ackMode = kafkaProperties.listener.ackMode
        factory.containerProperties.groupId = kafkaProperties.consumer.groupId
        factory.isBatchListener = true
        factory.setConcurrency(1)
        //factory.setCommonErrorHandler(CommonErrorHandler::class)
        return factory
    }*/

    @Bean
    open fun errorHandler(): CommonErrorHandler {
        return DefaultErrorHandler { record, ex ->
            //log.error("Error processing offset ${record.offset()}: ${ex.message}")
        }
    }
}