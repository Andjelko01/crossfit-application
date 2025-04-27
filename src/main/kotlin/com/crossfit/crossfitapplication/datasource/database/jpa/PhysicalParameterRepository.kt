package com.crossfit.crossfitapplication.datasource.database.jpa

import com.crossfit.crossfitapplication.datasource.database.models.entites.member.measurements.PhysicalParameter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PhysicalParameterRepository : JpaRepository<PhysicalParameter, Long>
