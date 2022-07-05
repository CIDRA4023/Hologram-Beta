package com.cidra.hologram_beta.ui.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cidra.hologram_beta.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: PreferencesRepository
) : ViewModel() {

    fun getGroupFilter(): Flow<Int> = repository.getGroupFilter()

    fun setGroupFilter(settingItem: Int) {
        viewModelScope.launch {
            repository.setGroupFilter(settingItem)
        }
    }

    fun getTimeNotation(): Flow<Int> = repository.getTimeNotation()

    fun setTimeNotation(settingItem: Int) {
        viewModelScope.launch {
            repository.setTimeNotation(settingItem)
        }
    }

    fun getOpenApp(): Flow<Int> = repository.getOpenApp()

    fun setOpenApp(settingItem: Int) {
        viewModelScope.launch {
            repository.setOpenApp(settingItem)
        }
    }
}