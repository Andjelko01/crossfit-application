package com.crossfit.crossfitapplication.application.controller

import com.crossfit.crossfitapplication.application.request.member.MemberCreateRequest
import com.crossfit.crossfitapplication.application.response.ControllerResponse
import com.crossfit.crossfitapplication.application.response.errorResponse
import com.crossfit.crossfitapplication.application.response.successResponse
import com.crossfit.crossfitapplication.service.MemberService
import com.github.michaelbull.result.fold
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/member")
class MemberController {
    @Autowired
    private lateinit var memberService: MemberService

    @PostMapping("/create")
    fun createUser(@RequestBody request: MemberCreateRequest): ResponseEntity<ControllerResponse<String>> {
        return memberService.createMember(request).fold(
            success = { memberId ->
                successResponse(data = memberId, message = "User created successfully with id $memberId", status = HttpStatus.CREATED)
            },
            failure = { error ->
                errorResponse(error)
            },
        )
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<ControllerResponse<Unit>> {
        return memberService.deleteMember(id).fold(
            success = { deletedId ->
                successResponse(data = deletedId, message = "User deleted successfully with id $deletedId")
            },
            failure = { error ->
                errorResponse(error)
            },
        )
    }

    @PutMapping("/{id}")
    fun resetUserPassword(@PathVariable id: String, @RequestBody password: String): ResponseEntity<ControllerResponse<Any>> {
        return memberService.resetUserPassword(id, password).fold(
            success = {
                successResponse(data = Unit, message = "User password reset successfully with id $it")
            },
            failure = { error ->
                errorResponse(error)
            },
        )
    }
}
