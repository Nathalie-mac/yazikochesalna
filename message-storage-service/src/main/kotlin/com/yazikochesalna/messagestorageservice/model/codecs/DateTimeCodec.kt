package com.yazikochesalna.messagestorageservice.model.codecs

import com.datastax.oss.driver.api.core.ProtocolVersion
import com.datastax.oss.driver.api.core.type.DataType
import com.datastax.oss.driver.api.core.type.DataTypes
import com.datastax.oss.driver.api.core.type.codec.TypeCodec
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs
import com.datastax.oss.driver.api.core.type.reflect.GenericType
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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

    //TODO: обсудить с фронтом и бэком зону
    override fun decode(bytes: ByteBuffer?, protocolVersion: ProtocolVersion): LocalDateTime? {
        val dateTime = TypeCodecs.TIMESTAMP.decode(bytes, protocolVersion)
        return LocalDateTime.ofInstant(dateTime, ZoneId.systemDefault())
    }

    override fun format(p0: LocalDateTime?): String {
        if (p0 == null) return "1900-01-01T00:00:00Z"
        return p0.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    override fun parse(p0: String?): LocalDateTime? {
        if (p0 == null) return null
        return LocalDateTime.parse(p0)
    }

    override fun encode(p0: LocalDateTime?, p1: ProtocolVersion): ByteBuffer? {
        if (p0 == null) return null
        //val dtLength = TypeCodecs.INT.encodePrimitive(8, p1)
        val dtBuffer = TypeCodecs.TIMESTAMP.encode(p0.toInstant(ZoneOffset.UTC), p1)
        return ByteBuffer.allocate(dtBuffer!!.capacity())
    }
}