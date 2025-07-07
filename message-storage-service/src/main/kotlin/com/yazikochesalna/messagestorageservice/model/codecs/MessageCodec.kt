package com.yazikochesalna.messagestorageservice.model.codecs

import com.datastax.oss.driver.api.core.ProtocolVersion
import com.datastax.oss.driver.api.core.type.DataType
import com.datastax.oss.driver.api.core.type.codec.TypeCodec
import com.datastax.oss.driver.api.core.type.reflect.GenericType
import com.yazikochesalna.messagestorageservice.model.db.Message
import java.nio.ByteBuffer

class MessageCodec : TypeCodec<Message> {
    override fun encode(p0: Message?, p1: ProtocolVersion): ByteBuffer? {
        TODO("Not yet implemented")
    }

    override fun getCqlType(): DataType {
        TODO("Not yet implemented")
    }

    override fun getJavaType(): GenericType<Message> {
        TODO("Not yet implemented")
    }

    override fun format(p0: Message?): String {
        TODO("Not yet implemented")
    }

    override fun parse(p0: String?): Message? {
        TODO("Not yet implemented")
    }

    override fun decode(p0: ByteBuffer?, p1: ProtocolVersion): Message? {
        TODO("Not yet implemented")
    }
}