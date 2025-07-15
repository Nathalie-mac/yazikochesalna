package com.yazikochesalna.messagestorageservice.exception.customexceptions

class ErrorKafkaDeserializatonException(val field: String) :
    RuntimeException("Deserialization error in field: '$field'")
