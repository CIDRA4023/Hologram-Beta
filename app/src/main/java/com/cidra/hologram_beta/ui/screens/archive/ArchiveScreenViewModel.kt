package com.cidra.hologram_beta.ui.screens.archive

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.common.Resource
import com.cidra.hologram_beta.domain.model.ArchiveItem
import com.cidra.hologram_beta.domain.repository.FirebaseRepository
import com.cidra.hologram_beta.domain.repository.PreferencesRepository
import com.cidra.hologram_beta.ui.dateAgo
import com.cidra.hologram_beta.ui.sdf
import com.cidra.hologram_beta.ui.truncate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ArchiveScreenViewModel @Inject constructor(
    private val repository: FirebaseRepository,
    private val repoPreferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _archiveItem = mutableStateOf((ArchiveItemState()))
    val archiveItem: State<ArchiveItemState> = _archiveItem

    private var videoList = listOf<ArchiveItem>()

    init {
        fetchArchiveItem(0)
    }

    private fun fetchArchiveItem(amount: Int) {
        Log.i("archiveItem", "archive")
        repository.fetchArchiveItem().onEach { result ->
            Log.i("archiveItem", "archive")
            when (result) {
                is Resource.Success -> {
                    repoPreferencesRepository.getGroupFilter().collect() { settingValue ->

                        videoList = result.data ?: emptyList()


                        val filteredGroup =
                            filteredGroup(settingValue, filteredDay(dateAgo(amount)))

                        _archiveItem.value = ArchiveItemState(success = filteredGroup)
                        Log.i("archiveViewModelvideolist", "$videoList")

                        Log.i("archiveViewModel_archiveItem", "$_archiveItem")

                    }

                }
                is Resource.Error -> {
                    _archiveItem.value = ArchiveItemState(
                        error = result.message ?: ""
                    )
                }
                is Resource.Loading -> {
                    _archiveItem.value = ArchiveItemState(isLoading = true)

                }
            }
        }.launchIn(viewModelScope)

    }


    /**
     * SwipeRefreshLayout用更新
     */
    fun refresh(amount: Int) {
        fetchArchiveItem(amount)
    }

    private fun filteredGroup(settingValue: Int, result: List<ArchiveItem>): List<ArchiveItem> {
        val filterGroup = when (settingValue) {
            R.string.setting_filter_holo_JP -> "holoJp"
            R.string.setting_filter_holo_EN -> "holoEn"
            R.string.setting_filter_holo_ID -> "holoId"
            R.string.setting_filter_holo_stars -> "holoStars"
            else -> ""
        }

        return result.filter { it.tagGroup == filterGroup }
    }


    private fun filteredDay(date: Date) =
        videoList.filter { truncate(sdf(it.publishedAt)) == truncate(date) }

    fun filteredDayChip(date: Date) {
        viewModelScope.launch {
            repoPreferencesRepository.getGroupFilter().collect() {
                val filteredList = filteredGroup(it, filteredDay(date))
                _archiveItem.value = ArchiveItemState(success = filteredList)

            }
        }
    }

    fun getOpenApp() = repoPreferencesRepository.getOpenApp()

    fun sortByViewer() {
        val sortedList = _archiveItem.value.success
            .sortedByDescending {
                it.viewers
            }
        _archiveItem.value = ArchiveItemState(success = sortedList)
    }

    fun sorByUpdate() {
        val sortedList = _archiveItem.value.success
            .sortedByDescending {
                it.publishedAt
            }
        _archiveItem.value = ArchiveItemState(success = sortedList)
    }

    fun sortByGood() {
        val sortedList = _archiveItem.value.success
            .sortedByDescending {
                it.likeCount
            }
        _archiveItem.value = ArchiveItemState(success = sortedList)
    }

}


@Stable
data class ArchiveItemState(
    val isLoading: Boolean = false,
    val success: List<ArchiveItem> = emptyList(),
    val error: String = ""
)