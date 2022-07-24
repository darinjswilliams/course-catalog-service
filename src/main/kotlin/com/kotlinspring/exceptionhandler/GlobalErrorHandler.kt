package com.kotlinspring.exceptionhandler

import com.kotlinspring.exception.InstructorNotValidException
import mu.KLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

//The Advice annotation is going to be acting as a proxy to track any kind of exceptions that is throw
// from the controller
@Component
@ControllerAdvice
class GlobalErrorHandler : ResponseEntityExceptionHandler() {

    companion object : KLogging()

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {

        //Display message in the console
        logger.error("MethoArgumentNotValidException observed : ${ex.message}", ex)

        val errors = ex.bindingResult.allErrors
            //this get the message value defined on the courseDTO method
            //courseDTO.name must not be blank"
            .map { error -> error.defaultMessage!! }
            .sorted()

        logger.info("errors : $errors")

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errors.joinToString(", ") {it})
    }

    @ExceptionHandler(InstructorNotValidException::class)
    fun handleInstructorNotValidExceptions(ex: InstructorNotValidException, request: WebRequest): ResponseEntity<Any>{
        logger.error("Exception observed: ${ex.message}", ex)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ex.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<Any>{
        logger.error("Exception observed: ${ex.message}", ex)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ex.message)
    }
}