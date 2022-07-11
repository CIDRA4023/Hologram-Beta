package com.cidra.hologram_beta.ui.screens.schedule

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.common.Resource
import com.cidra.hologram_beta.domain.model.ScheduleItem
import com.cidra.hologram_beta.domain.repository.FirebaseRepository
import com.cidra.hologram_beta.domain.repository.PreferencesRepository
import com.cidra.hologram_beta.ui.sdf
import com.cidra.hologram_beta.ui.tomorrow
import com.cidra.hologram_beta.ui.truncate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ScheduleScreenViewModel @Inject constructor(
    private val repository: FirebaseRepository,
    private val repoPreferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _scheduleItemState = mutableStateOf(ScheduleItemState())
    val scheduleItemState: State<ScheduleItemState> = _scheduleItemState

    init {
        Log.i("ViewModel", "Schedule")
        fetchScheduleItem()
    }

    private fun fetchScheduleItem() {

        repository.fetchScheduleItem().onEach { result ->

            when (result) {
                is Resource.Success -> {

                    // dataStoreから設定値を取得
                    repoPreferencesRepository.getGroupFilter().collect { settingValue ->
                        val filteredState = filterDayList(settingValue, result.data ?: emptyList())
                        _scheduleItemState.value = ScheduleItemState(state = filteredState)
                    }

                }
                is Resource.Error -> {
                    _scheduleItemState.value = ScheduleItemState(
                        error = result.message ?: ""
                    )
                }
                is Resource.Loading -> {
                    _scheduleItemState.value = ScheduleItemState(isLoading = true)

                }
            }
        }.launchIn(viewModelScope)
    }

    fun refresh() {
        fetchScheduleItem()
    }

    private fun filteredGroup(settingValue: Int, result: List<ScheduleItem>): List<ScheduleItem> {
        val filterGroup = when (settingValue) {
            R.string.setting_filter_holo_JP -> "holoJp"
            R.string.setting_filter_holo_EN -> "holoEn"
            R.string.setting_filter_holo_ID -> "holoId"
            R.string.setting_filter_holo_stars -> "holoStars"
            else -> ""
        }

        return result.filter { it.tagGroup == filterGroup }
    }


    private fun filterDayList(settingValue: Int, result: List<ScheduleItem>): ScheduleState {

        val filteredGroup = filteredGroup(settingValue, result)

        val todayItem =
            filteredGroup.filter { truncate(sdf(it.scheduledStartTime)) == truncate(Date()) }
        val tomorrowItem = filteredGroup.filter {
            truncate(sdf(it.scheduledStartTime)) == truncate(
                tomorrow()
            )
        }

        /**
         * 今日と明日の配信予定の有無で条件分岐
         */
        return if (todayItem.isNotEmpty() && tomorrowItem.isNotEmpty()) {
            ScheduleState.TodayTomorrow(todayItem, tomorrowItem)
        } else if (todayItem.isEmpty()) {
            ScheduleState.Tomorrow(tomorrowItem)
        } else if (tomorrowItem.isEmpty()) {
            ScheduleState.Today(todayItem)
        } else {
            ScheduleState.Empty()
        }

    }

    fun getTimeNotation() = repoPreferencesRepository.getTimeNotation()

    fun getOpenApp() = repoPreferencesRepository.getOpenApp()
}

@Stable
sealed class ScheduleState() {
    data class Today(val data: List<ScheduleItem>) : ScheduleState()
    data class Tomorrow(val data: List<ScheduleItem>) : ScheduleState()
    data class TodayTomorrow(val data: List<ScheduleItem>, val data2: List<ScheduleItem>) :
        ScheduleState()

    data class Empty(val data: List<ScheduleItem> = emptyList()) : ScheduleState()
}

@Stable
data class ScheduleItemState(
    val isLoading: Boolean = false,
    val state: ScheduleState = ScheduleState.Empty(),
    val error: String = ""
)