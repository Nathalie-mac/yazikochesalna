package com.yazikochesalna.messagestorageservice.exception.customexceptions

class NullCassandraFiledException(val field: String) :
    RuntimeException("Required field '$field' cannot be null")