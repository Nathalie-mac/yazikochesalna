package com.yazikochesalna.messagestorageservice.exception

import java.io.IOException

class ErrorKafkaDeserializatonException(field: String): ClassCastException("Error in message from Kafka deserialization: $field")