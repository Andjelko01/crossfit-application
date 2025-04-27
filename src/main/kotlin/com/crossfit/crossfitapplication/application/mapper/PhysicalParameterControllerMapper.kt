package com.crossfit.crossfitapplication.application.mapper

import com.crossfit.crossfitapplication.application.request.measurements.create.MeasurementsCreateRequest
import com.crossfit.crossfitapplication.application.request.measurements.create.PhysicalParameterCreateRequest
import com.crossfit.crossfitapplication.application.request.measurements.create.SkinfoldsCreateRequest
import com.crossfit.crossfitapplication.application.request.measurements.update.MeasurementsUpdateRequest
import com.crossfit.crossfitapplication.application.request.measurements.update.PhysicalParameterUpdateRequest
import com.crossfit.crossfitapplication.application.request.measurements.update.SkinfoldsUpdateRequest
import com.crossfit.crossfitapplication.service.models.MeasurementsDto
import com.crossfit.crossfitapplication.service.models.PhysicalParameterDto
import com.crossfit.crossfitapplication.service.models.SkinfoldsDto
import org.springframework.stereotype.Component

@Component
class PhysicalParameterControllerMapper {

    fun toPhysicalParameterDto(request: PhysicalParameterCreateRequest): PhysicalParameterDto =
        PhysicalParameterDto(
            date = request.date,
            weight = request.weight,
            height = request.height,
            skinfolds = request.skinfolds?.let(this::toSkinfoldsDto),
            measurements = request.measurements?.let(this::toMeasurementsDto),
            memberKeycloakId = request.memberKeycloakId!!,
        )

    fun toPhysicalParameterDto(request: PhysicalParameterUpdateRequest, existingDto: PhysicalParameterDto): PhysicalParameterDto =
        existingDto.copy(
            date = request.date ?: existingDto.date,
            weight = request.weight ?: existingDto.weight,
            height = request.height ?: existingDto.height,
            skinfolds = request.skinfolds?.let { toSkinfoldsDto(it, existingDto.skinfolds ?: SkinfoldsDto()) } ?: existingDto.skinfolds,
            measurements = request.measurements?.let { toMeasurementsDto(it, existingDto.measurements ?: MeasurementsDto()) } ?: existingDto.measurements
        )

    fun toMeasurementsDto(request: MeasurementsCreateRequest): MeasurementsDto =
        MeasurementsDto(
            upperArm = request.upperArm,
            chest = request.chest,
            stomach = request.stomach,
            hips = request.hips,
            thigh = request.thigh
        )

    fun toMeasurementsDto(request: MeasurementsUpdateRequest, existingDto: MeasurementsDto): MeasurementsDto =
        existingDto.copy(
            id = request.id ?: existingDto.id,
            upperArm = request.upperArm ?: existingDto.upperArm,
            chest = request.chest ?: existingDto.chest,
            stomach = request.stomach ?: existingDto.stomach,
            hips = request.hips ?: existingDto.hips,
            thigh = request.thigh ?: existingDto.thigh
        )

    fun toSkinfoldsDto(request: SkinfoldsCreateRequest): SkinfoldsDto =
        SkinfoldsDto(
            biceps = request.biceps,
            triceps = request.triceps,
            shoulderBlade = request.shoulderBlade,
            back = request.back,
            stomach = request.stomach,
            glutes = request.glutes,
            frontThigh = request.frontThigh,
            rearThigh = request.rearThigh
        )

    fun toSkinfoldsDto(request: SkinfoldsUpdateRequest, existingDto: SkinfoldsDto): SkinfoldsDto =
        existingDto.copy(
            id = request.id ?: existingDto.id,
            biceps = request.biceps ?: existingDto.biceps,
            triceps = request.triceps ?: existingDto.triceps,
            shoulderBlade = request.shoulderBlade ?: existingDto.shoulderBlade,
            back = request.back ?: existingDto.back,
            stomach = request.stomach ?: existingDto.stomach,
            glutes = request.glutes ?: existingDto.glutes,
            frontThigh = request.frontThigh ?: existingDto.frontThigh,
            rearThigh = request.rearThigh ?: existingDto.rearThigh
        )
}