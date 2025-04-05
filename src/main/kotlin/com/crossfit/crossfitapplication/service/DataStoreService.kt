package com.crossfit.crossfitapplication.service

import com.crossfit.crossfitapplication.datasource.database.jpa.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DataStoreService {
    @Autowired
    private lateinit var memberRepository: MemberRepository

    fun storeMember() {
    }
}
