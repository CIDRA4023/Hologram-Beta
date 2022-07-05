package com.cidra.hologram_beta.domain.repository

import com.cidra.hologram_beta.common.Resource
import com.cidra.hologram_beta.domain.model.ArchiveItem
import com.cidra.hologram_beta.domain.model.LiveItem
import com.cidra.hologram_beta.domain.model.ScheduleItem
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {
    fun fetchLiveItem(): Flow<Resource<List<LiveItem>>>
    fun fetchScheduleItem(): Flow<Resource<List<ScheduleItem>>>
    fun fetchArchiveItem(): Flow<Resource<List<ArchiveItem>>>
}