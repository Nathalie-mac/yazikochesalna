package com.yazikochesalna.messagestorageservice.exception

import com.yazikochesalna.messagestorageservice.exception.customexceptions.CoroutinesException
import com.yazikochesalna.messagestorageservice.exception.customexceptions.ErrorKafkaDeserializatonException
import com.yazikochesalna.messagestorageservice.exception.customexceptions.NullCassandraFiledException
import com.yazikochesalna.messagestorageservice.service.ChatServiceClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {
    private val log: Logger = LoggerFactory.getLogger(ChatServiceClient::class.java)

    @ExceptionHandler(RuntimeException::class)
    fun handleException(ex: RuntimeException,  request: WebRequest): ResponseEntity<ExceptionDTO> {

        log.error("Error: ${ex.javaClass.simpleName}, Message: ${ex.message}\n" +
            "Path: ${request.getDescription(false)}\nStackTrace:${ex.stackTraceToString()}")

        val details = mutableMapOf(
            "path" to request.getDescription(false),
            "timestamp" to LocalDateTime.now()
        )

        when (ex) {
            is ErrorKafkaDeserializatonException -> details["field"] = ex.field
            is NullCassandraFiledException -> details["field"] = ex.field
        }

        return ResponseEntity(
            ExceptionDTO(ex.message ?: "Unexpected error", details),
            getStatus(ex.javaClass)
        )
    }

    private fun getStatus(exceptionClass: Class<out Throwable?>): HttpStatus =
        exceptionClass.getAnnotation(
            ResponseStatus::class.java
        ).let { responseStatus ->
            responseStatus?.value ?: HttpStatus.INTERNAL_SERVER_ERROR
        }


    @ExceptionHandler(CoroutinesException::class)
    fun handleMessagesProcessingException(
        ex: CoroutinesException,
        request: WebRequest
    ): ResponseEntity<ExceptionDTO> {
        log.error("Messages fetching via coroutines failed", ex)

//        val details = mutableMapOf(
//            "path" to request.getDescription(false),
//            "timestamp" to LocalDateTime.now()
//        ).apply {
//            ex.chatId?.let { put("chatId", it.toString()) }
//        }

        return ResponseEntity(
            ExceptionDTO(
                problem = ex.message ?: "Messages fetching via coroutines failed"
                //details = details
            ),
            getStatus(ex.javaClass)
        )
    }


}

