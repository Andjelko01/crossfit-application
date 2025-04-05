package com.crossfit.crossfitapplication.application.controller

import com.crossfit.crossfitapplication.application.controller.request.MemberCreateRequest
import com.crossfit.crossfitapplication.application.controller.response.ControllerResponse
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
    fun createUser(@RequestBody request: MemberCreateRequest): ResponseEntity<ControllerResponse<Any>> {
        memberService.createMember(request).fold(
            success = {
                return ResponseEntity.ok(ControllerResponse(HttpStatus.OK, "User created successfully with id $it"))
            },
            failure = { error ->
                return ResponseEntity.status(error.httpStatus!!.value()).body(
                    ControllerResponse(
                        status = error.httpStatus,
                        message = error.errorMessage,
                        error = error.retryPolicy.toString(),
                    ),
                )
            },
        )
    }

//    @DeleteMapping("/{id}")
//    fun deleteUser(
//        @PathVariable id: String,
//    ): ResponseEntity<ControllerResponse<Any>> {
//        memberService.deleteMember(id).fold(
//            success = {
//                return ResponseEntity.ok(ControllerResponse(HttpStatus.OK, "User deleted successfully with id ${it}"))
//            },
//            failure = { error ->
//                return ResponseEntity.status(error.httpStatus!!.value()).body(
//                    ControllerResponse(
//                        status = error.httpStatus,
//                        message = error.errorMessage,
//                        error = error.retryPolicy!!.name,
//                    ),
//                )
//            },
//        )
//    }
}
