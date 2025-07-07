package com.yazikochesalna.messagestorageservice.config

import com.datastax.oss.driver.api.core.CqlSession
import com.yazikochesalna.messagestorageservice.model.codecs.DateTimeCodec
import com.yazikochesalna.messagestorageservice.model.codecs.MessageCodec
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate
import org.springframework.data.cassandra.core.cql.ReactiveCqlTemplate
import java.net.InetSocketAddress

@Configuration
open class AppConfig {
    @Value("\${spring.cassandra.username}")
    private lateinit var username: String

    @Value("\${spring.cassandra.password}")
    private lateinit var password: String

    @Value("\${spring.cassandra.keyspace-name}")
    private lateinit var keyspace: String

    @Value("\${spring.cassandra.contact-points}")
    private lateinit var contactPoint: String

    @Value("\${spring.cassandra.local-datacenter}")
    private lateinit var dataCenter: String

    @Bean
    open fun session(): CqlSession{
        return CqlSession.builder().withKeyspace(keyspace)
            .addContactPoint(InetSocketAddress(contactPoint, 9042))
            .withAuthCredentials(username, password)
            .withLocalDatacenter(dataCenter)
            .addTypeCodecs(DateTimeCodec())
            .build()
    }

//    override fun getKeyspaceName(): String {
//        return keyspace
//    }
//
//    @Bean
//    override fun reactiveCassandraTemplate(): ReactiveCassandraTemplate {
//        return ReactiveCassandraTemplate(reactiveCassandraSession())
//    }
//
//    @Bean
//    override fun reactiveCqlTemplate(): ReactiveCqlTemplate{
//        return ReactiveCqlTemplate(reactiveCassandraSession())
//    }

}