package com.yazikochesalna.messagestorageservice.model.codecs

import com.datastax.oss.driver.api.core.ProtocolVersion
import com.datastax.oss.driver.api.core.type.DataType
import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.core.type.codec.TypeCodec
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs
import com.datastax.oss.driver.api.core.type.reflect.GenericType
import org.springframework.stereotype.Component
import java.nio.ByteBuffer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Component
class DateTimeCodec : TypeCodec<LocalDateTime> {

    override fun getJavaType(): GenericType<LocalDateTime> {
        return GenericType.LOCAL_DATE_TIME
    }

    override fun getCqlType(): DataType {
        return DataTypes.TIMESTAMP
    }

    override fun accepts(javaClass: Class<*>): Boolean {
        return javaClass == LocalDateTime::class.java
    }

    override fun accepts(cqlType: DataType): Boolean {
        return cqlType.asCql(false, true) == "timestamp"
    }

    override fun decode(bytes: ByteBuffer?, protocolVersion: ProtocolVersion): LocalDateTime? {
        return bytes?.let {
            val instant = TypeCodecs.TIMESTAMP.decode(bytes, protocolVersion)
            instant?.atOffset(ZoneOffset.UTC)?.toLocalDateTime()
        }
    }

    override fun format(p0: LocalDateTime?): String {
        //if (p0 == null) return "NULL"
        return p0?.atOffset(ZoneOffset.UTC)?.format(DateTimeFormatter.ISO_INSTANT)?: "NULL"
    }

    override fun parse(p0: String?): LocalDateTime? {
        if (p0 == null) return null
        return Instant.parse(p0).atOffset(ZoneOffset.UTC).toLocalDateTime()
    }

    override fun encode(p0: LocalDateTime?, p1: ProtocolVersion): ByteBuffer? {
        if (p0 == null) return null
        //val dtLength = TypeCodecs.INT.encodePrimitive(8, p1)
        val instant = p0.atOffset(ZoneOffset.UTC).toInstant()
        return TypeCodecs.TIMESTAMP.encode(instant, p1)
    }
}