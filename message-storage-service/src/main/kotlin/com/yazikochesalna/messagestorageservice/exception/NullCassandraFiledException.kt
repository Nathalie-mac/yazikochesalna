package com.yazikochesalna.messagestorageservice.exception

class NullCassandraFiledException(field: String): IllegalStateException("Required table field '$field' is null")
