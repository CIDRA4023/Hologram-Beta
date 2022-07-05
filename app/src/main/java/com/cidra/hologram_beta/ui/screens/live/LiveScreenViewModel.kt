package com.cidra.hologram_beta.ui.screens.live

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.common.Resource
import com.cidra.hologram_beta.domain.model.LiveItem
import com.cidra.hologram_beta.domain.repository.FirebaseRepository
import com.cidra.hologram_beta.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LiveScreenViewModel @Inject constructor(
    private val repoFirebase: FirebaseRepository,
    private val repoPreferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _liveItem = mutableStateOf(LiveItemState())
    val liveItem: State<LiveItemState> = _liveItem

    init {
        Log.i("ViewModel", "Live")
        fetchLiveItem()
    }

    private fun fetchLiveItem() {
        Log.i("liveItem", "fetch")
        repoFirebase.fetchLiveItem().onEach { result ->
            Log.i("liveItem", "fetch2")
            when (result) {
                is Resource.Success -> {
                    repoPreferencesRepository.getGroupFilter().collect() { settingValue ->
                        val filteredList = filteredGroup(settingValue, result.data ?: emptyList())
                        _liveItem.value = LiveItemState(live = filteredList)
                    }

                }
                is Resource.Error -> {
                    _liveItem.value = LiveItemState(
                        error = result.message ?: ""
                    )
                }
                is Resource.Loading -> {
                    _liveItem.value = LiveItemState(isLoading = true)

                }
            }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        fetchLiveItem()
    }

    private fun filteredGroup(settingValue: Int, result: List<LiveItem>): List<LiveItem> {
        val filterGroup = when (settingValue) {
            R.string.setting_filter_holo_JP -> "holoJp"
            R.string.setting_filter_holo_EN -> "holoEn"
            R.string.setting_filter_holo_ID -> "holoId"
            R.string.setting_filter_holo_stars -> "holoStars"
            else -> ""
        }
        return result.filter { it.tagGroup == filterGroup }
    }


    fun getOpenApp() = repoPreferencesRepository.getOpenApp()

}


@Stable
data class LiveItemState(
    val isLoading: Boolean = false,
    val live: List<LiveItem> = emptyList(),
    val error: String = ""
)

