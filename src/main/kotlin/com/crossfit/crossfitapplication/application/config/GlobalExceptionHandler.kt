package com.crossfit.crossfitapplication.application.config

import com.crossfit.crossfitapplication.application.response.ControllerResponse
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.sql.SQLException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(SQLException::class)
    fun handleSQLException(ex: SQLException): ResponseEntity<ControllerResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ControllerResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR,
                    message = ex.message ?: "Database error occurred",
                    data = null,
                    error = SQLException::class.java.name
                )
            )
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(ex: DataAccessException): ResponseEntity<ControllerResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ControllerResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR,
                    message = ex.message ?: "Data access error",
                    data = null,
                    error = DataAccessException::class.java.name
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ControllerResponse<Nothing>> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ControllerResponse(
                    status = HttpStatus.INTERNAL_SERVER_ERROR,
                    message = ex.message ?: "Internal server error",
                    data = null,
                    error = Exception::class.java.name
                )
            )
    }
}
