package com.cidra.hologram_beta.ui.screens.schedule

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.ui.screens.schedule.component.HeaderItem
import com.cidra.hologram_beta.ui.screens.schedule.component.ScheduleListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(viewModel: ScheduleScreenViewModel = hiltViewModel()) {

    val state = viewModel.scheduleItemState.value
    val scheduleState = state.state
    Log.i("ScheduleState", scheduleState.toString())

    val timeNotation =
        viewModel.getTimeNotation().collectAsState(initial = R.string.setting_time_notation_12hour)


    var isRefreshing by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
                viewModel.refresh()
                isRefreshing = false
            }
        ) {
            when (scheduleState) {
                is ScheduleState.Today -> {
                    LazyColumn() {
                        stickyHeader {
                            HeaderItem(day = "今日")
                        }
                        items(
                            items = scheduleState.data,
                            itemContent = { item ->
                                ScheduleListItem(item = item, settingValue = timeNotation.value)
                            }
                        )
                    }


                }
                is ScheduleState.Tomorrow -> {
                    LazyColumn() {
                        stickyHeader {
                            HeaderItem(day = "明日")
                        }
                        items(
                            items = scheduleState.data,
                            itemContent = { item ->
                                ScheduleListItem(item = item, settingValue = timeNotation.value)
                            }
                        )
                    }

                }
                is ScheduleState.TodayTomorrow -> {

                    LazyColumn() {
                        stickyHeader {
                            HeaderItem(day = "今日")
                        }
                        items(
                            items = scheduleState.data,
                            itemContent = { item ->
                                ScheduleListItem(item = item, settingValue = timeNotation.value)
                            }
                        )
                        stickyHeader {
                            HeaderItem(day = "明日")
                        }
                        items(
                            items = scheduleState.data2,
                            itemContent = { item ->
                                ScheduleListItem(item = item, settingValue = timeNotation.value)
                            }
                        )
                    }

                }
                is ScheduleState.Empty -> {
                    Box() {
                        Text(
                            text = "配信予定はありません",
                            color = MaterialTheme.colors.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }

        Box() {
            if (state.error.isNotBlank()) {
                Text(
                    text = "通信エラー",
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center)
                )
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(alignment = Alignment.Center)
                )

            }
        }

    }

}






