package com.cidra.hologram_beta.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.data.PreferencesKeys
import com.cidra.hologram_beta.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PreferencesRepository {

    override fun getGroupFilter(): Flow<Int> =
        dataStore.data.map {
            it[PreferencesKeys.FILTER_ITEM] ?: R.string.setting_filter_holo_JP
        }

    override suspend fun setGroupFilter(settingItem: Int) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[PreferencesKeys.FILTER_ITEM] = settingItem
        }
    }

    override fun getTimeNotation(): Flow<Int> =
        dataStore.data.map {
            it[PreferencesKeys.TIME_NOTATION] ?: R.string.setting_time_notation_12hour
        }

    override suspend fun setTimeNotation(settingItem: Int) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[PreferencesKeys.TIME_NOTATION] = settingItem
        }
    }

    override fun getOpenApp(): Flow<Int> =
        dataStore.data.map {
            it[PreferencesKeys.OPEN_APP] ?: R.string.setting_open_app_youtube
        }

    override suspend fun setOpenApp(settingItem: Int) {
        dataStore.edit { preferences: MutablePreferences ->
            preferences[PreferencesKeys.OPEN_APP] = settingItem
        }
    }

}