package com.crossfit.crossfitapplication.application.controller

import com.crossfit.crossfitapplication.application.exceptions.PhysicalParameterValidationException
import com.crossfit.crossfitapplication.application.mapper.PhysicalParameterControllerMapper
import com.crossfit.crossfitapplication.application.request.measurements.create.PhysicalParameterCreateRequest
import com.crossfit.crossfitapplication.application.response.ControllerResponse
import com.crossfit.crossfitapplication.application.response.errorResponse
import com.crossfit.crossfitapplication.application.response.successResponse
import com.crossfit.crossfitapplication.service.PhysicalParameterService
import com.crossfit.crossfitapplication.service.common.SecurityHelper
import com.crossfit.crossfitapplication.service.models.PhysicalParameterDto
import com.github.michaelbull.result.fold
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/physical-parameters")
class PhysicalParameterController(
    private val physicalParameterService: PhysicalParameterService,
    private val physicalParameterControllerMapper: PhysicalParameterControllerMapper,
) {

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    fun createPhysicalParameter(@Valid @RequestBody request: PhysicalParameterCreateRequest): ResponseEntity<ControllerResponse<PhysicalParameterDto>> {
        validatePhysicalParameter(request)

        val physicalParameterDto = physicalParameterControllerMapper.toPhysicalParameterDto(request)

        return physicalParameterService.createPhysicalParameter(physicalParameterDto).fold(
            success = { createdDto ->
                successResponse(data = createdDto, message = "Physical parameter created successfully", status = HttpStatus.CREATED)
            },
            failure = { error ->
                errorResponse(error)
            }
        )
    }

    private fun validatePhysicalParameter(request: PhysicalParameterCreateRequest) {
        if (request.date == null) {
            throw PhysicalParameterValidationException("Date cannot be null.")
        }
        if (request.weight == null || request.weight <= 0) {
            throw PhysicalParameterValidationException("Weight must be a positive value.")
        }
        if (request.height == null || request.height <= 0) {
            throw PhysicalParameterValidationException("Height must be a positive value.")
        }
        if (request.memberKeycloakId == null) {
            throw PhysicalParameterValidationException("Member ID cannot be null.")
        }
    }

}