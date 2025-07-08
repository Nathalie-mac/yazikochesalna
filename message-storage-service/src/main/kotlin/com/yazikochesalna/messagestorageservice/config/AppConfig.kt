package com.yazikochesalna.messagestorageservice.config

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveSession
import com.datastax.oss.driver.api.core.CqlSession
import com.yazikochesalna.messagestorageservice.config.properties.CassandraProperties
import com.yazikochesalna.messagestorageservice.model.codecs.DateTimeCodec
import com.yazikochesalna.messagestorageservice.repository.AttachmentRepository
import com.yazikochesalna.messagestorageservice.repository.MessageRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories
import java.net.InetSocketAddress
import java.time.LocalDateTime

@EnableConfigurationProperties(CassandraProperties::class)
@Configuration
@EnableCassandraRepositories(basePackageClasses = [AttachmentRepository::class, MessageRepository::class])
open class AppConfig(
    private val cassandraProperties: CassandraProperties
){
    @Bean
    open fun session(dateTimeCodec: DateTimeCodec): ReactiveSession{
        //println("Injecting codec: ${dateTimeCodec.javaClass.name}")
        return CqlSession.builder(

        ).withKeyspace(cassandraProperties.keyspaceName)
            .addContactPoint(InetSocketAddress(cassandraProperties.contactPoints, cassandraProperties.port))
            .withAuthCredentials(cassandraProperties.username, cassandraProperties.password)
            .withLocalDatacenter(cassandraProperties.localDatacenter)
            .addTypeCodecs(dateTimeCodec)
            .build()
            .also { session ->
                val codecRegistry = session.context.codecRegistry
                //println("Registered codecs: ${codecRegistry}")
            }
    }

}