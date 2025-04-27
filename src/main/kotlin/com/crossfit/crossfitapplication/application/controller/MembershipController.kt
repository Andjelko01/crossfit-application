package com.crossfit.crossfitapplication.application.controller

import com.crossfit.crossfitapplication.application.exceptions.MembershipDateValidationException
import com.crossfit.crossfitapplication.application.mapper.ResponseMapper
import com.crossfit.crossfitapplication.application.mapper.toMembershipDto
import com.crossfit.crossfitapplication.application.request.membership.MembershipCreateRequest
import com.crossfit.crossfitapplication.application.response.*
import com.crossfit.crossfitapplication.service.MembershipService
import com.crossfit.crossfitapplication.service.common.SecurityHelper
import com.github.michaelbull.result.fold
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/memberships")
class MembershipController(private val securityHelper: SecurityHelper, private val membershipService: MembershipService, private val responseMapper: ResponseMapper) {

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    fun createMembership(@RequestBody membershipCreateRequest: MembershipCreateRequest): ResponseEntity<ControllerResponse<MembershipResponse>> {
        validateMembershipDates(membershipCreateRequest)

        val memberKeycloakId = securityHelper.getCurrentUserKeycloakId()
        val membership = membershipCreateRequest.toMembershipDto()

        return membershipService.createMembership(membership, memberKeycloakId).fold(
            success = {
                successResponse(data = responseMapper.membershipDtoToMembershipResponse(it), message = "Membership created successfully", status = HttpStatus.CREATED)
            },
            failure = { error ->
                errorResponse(error)
            },
        )
    }

    fun validateMembershipDates(membershipCreateRequest: MembershipCreateRequest) {
        if (membershipCreateRequest.endDate.isBefore(membershipCreateRequest.startDate)) {
            throw MembershipDateValidationException("End date must be after start date.")
        }
    }
}
