package com.crossfit.crossfitapplication.application.response

import com.crossfit.crossfitapplication.application.exceptions.MembershipDateValidationException
import com.crossfit.crossfitapplication.application.exceptions.PhysicalParameterValidationException
import com.crossfit.crossfitapplication.service.error.ServiceError
import com.crossfit.crossfitapplication.service.models.PhysicalParameterDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

fun <T> successResponse(data: T? = null, message: String? = null, status: HttpStatus = HttpStatus.OK, error: String? = null): ResponseEntity<ControllerResponse<T>> {
    return ResponseEntity.status(status).body(ControllerResponse(status, message, data, error))
}

fun <T> errorResponse(error: ServiceError): ResponseEntity<ControllerResponse<T>> {
    return ResponseEntity.status(error.httpStatus!!.value()).body(
        ControllerResponse(
            status = error.httpStatus,
            message = error.errorMessage,
            error = error.retryPolicy?.name,
        ),
    )
}

@ExceptionHandler(MembershipDateValidationException::class)
fun handleMembershipDateValidationException(ex: MembershipDateValidationException): ResponseEntity<ControllerResponse<MembershipResponse>> {
    return ResponseEntity.badRequest().body(
        ControllerResponse(
            status = HttpStatus.BAD_REQUEST,
            message = ex.message,
            error = "Validation Error",
        ),
    )
}

@ExceptionHandler(PhysicalParameterValidationException::class)
fun handlePhysicalParameterValidationException(ex: PhysicalParameterValidationException): ResponseEntity<ControllerResponse<PhysicalParameterDto>> {
    return ResponseEntity.badRequest().body(
        ControllerResponse(
            status = HttpStatus.BAD_REQUEST,
            message = ex.message,
            error = "Validation Error",
        ),
    )
}
