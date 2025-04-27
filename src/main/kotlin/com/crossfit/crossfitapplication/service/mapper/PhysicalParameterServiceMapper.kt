package com.crossfit.crossfitapplication.service.mapper

import com.crossfit.crossfitapplication.datasource.database.models.entites.member.Member
import com.crossfit.crossfitapplication.datasource.database.models.entites.member.measurements.Measurements
import com.crossfit.crossfitapplication.datasource.database.models.entites.member.measurements.PhysicalParameter
import com.crossfit.crossfitapplication.datasource.database.models.entites.member.measurements.Skinfolds
import com.crossfit.crossfitapplication.service.models.MeasurementsDto
import com.crossfit.crossfitapplication.service.models.PhysicalParameterDto
import com.crossfit.crossfitapplication.service.models.SkinfoldsDto
import org.springframework.stereotype.Component

@Component
class PhysicalParameterServiceMapper {

    fun toPhysicalParameterEntity(physicalParameterDto: PhysicalParameterDto, member: Member): PhysicalParameter =
        PhysicalParameter(
            id = physicalParameterDto.id,
            date = physicalParameterDto.date,
            weight = physicalParameterDto.weight,
            height = physicalParameterDto.height,
            skinfolds = physicalParameterDto.skinfolds?.let(this::toSkinfoldsEntity),
            measurements = physicalParameterDto.measurements?.let(this::toMeasurementsEntity),
            member = member
        )

    fun toMeasurementsEntity(measurementsDto: MeasurementsDto): Measurements =
        Measurements(
            id = measurementsDto.id,
            upperArm = measurementsDto.upperArm,
            chest = measurementsDto.chest,
            stomach = measurementsDto.stomach,
            hips = measurementsDto.hips,
            thigh = measurementsDto.thigh
        )

    fun toSkinfoldsEntity(skinfoldsDto: SkinfoldsDto): Skinfolds =
        Skinfolds(
            id = skinfoldsDto.id,
            biceps = skinfoldsDto.biceps,
            triceps = skinfoldsDto.triceps,
            shoulderBlade = skinfoldsDto.shoulderBlade,
            back = skinfoldsDto.back,
            stomach = skinfoldsDto.stomach,
            glutes = skinfoldsDto.glutes,
            frontThigh = skinfoldsDto.frontThigh,
            rearThigh = skinfoldsDto.rearThigh
        )

    fun toPhysicalParameterDto(entity: PhysicalParameter): PhysicalParameterDto =
        PhysicalParameterDto(
            id = entity.id,
            date = entity.date,
            weight = entity.weight,
            height = entity.height,
            skinfolds = entity.skinfolds?.let(this::toSkinfoldsDto),
            measurements = entity.measurements?.let(this::toMeasurementsDto),
            memberKeycloakId = entity.member.keycloakId.toString()
        )

    fun toMeasurementsDto(entity: Measurements): MeasurementsDto =
        MeasurementsDto(
            id = entity.id,
            upperArm = entity.upperArm,
            chest = entity.chest,
            stomach = entity.stomach,
            hips = entity.hips,
            thigh = entity.thigh
        )

    fun toSkinfoldsDto(entity: Skinfolds): SkinfoldsDto =
        SkinfoldsDto(
            id = entity.id,
            biceps = entity.biceps,
            triceps = entity.triceps,
            shoulderBlade = entity.shoulderBlade,
            back = entity.back,
            stomach = entity.stomach,
            glutes = entity.glutes,
            frontThigh = entity.frontThigh,
            rearThigh = entity.rearThigh
        )
}