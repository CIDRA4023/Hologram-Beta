package com.cidra.hologram_beta.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getGroupFilter(): Flow<Int>
    suspend fun setGroupFilter(settingItem: Int)
    fun getTimeNotation(): Flow<Int>
    suspend fun setTimeNotation(settingItem: Int)
    fun getOpenApp(): Flow<Int>
    suspend fun setOpenApp(settingItem: Int)
}