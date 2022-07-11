package com.cidra.hologram_beta.ui.screens.schedule

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.ui.screens.archive.component.adBanner
import com.cidra.hologram_beta.ui.screens.schedule.component.HeaderItem
import com.cidra.hologram_beta.ui.screens.schedule.component.ScheduleListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(viewModel: ScheduleScreenViewModel = hiltViewModel()) {

    val state = viewModel.scheduleItemState.value
    val scheduleState = state.state
    Log.i("ScheduleState", scheduleState.toString())

    val timeNotation =
        viewModel.getTimeNotation().collectAsState(initial = R.string.setting_time_notation_12hour)

    val openAppValue =
        viewModel.getOpenApp().collectAsState(initial = R.string.setting_open_app_youtube)


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
            Box(modifier = Modifier.fillMaxSize()) {
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

            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (banner, speedDialFab) = createRefs()

                when (scheduleState) {
                    is ScheduleState.Today -> {
                        val listState = rememberLazyListState()
                        LazyColumn(state = listState) {
                            stickyHeader {
                                HeaderItem(day = "今日")
                            }
                            items(
                                items = scheduleState.data,
                                itemContent = { item ->
                                    ScheduleListItem(item = item, timeNotation = timeNotation.value, openApp = openAppValue.value)
                                }
                            )
                            item {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                )
                            }
                        }
                        val scope = rememberCoroutineScope()
                        FloatingActionButton(
                            modifier = Modifier
                                .constrainAs(speedDialFab) {
                                    bottom.linkTo(banner.top, 16.dp)
                                    end.linkTo(parent.end, 16.dp)
                                },
                            onClick = {
                                scope.launch {
                                    listState.animateScrollToItem(0)
                                }
                            }
                        ) {
                            Icon(Icons.Filled.ArrowUpward, null)
                        }
                        adBanner(
                            modifier = Modifier
                                .constrainAs(banner) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                }
                        )
                    }
                    is ScheduleState.Tomorrow -> {
                        val listState = rememberLazyListState()
                        LazyColumn(state = listState) {
                            stickyHeader {
                                HeaderItem(day = "明日")
                            }
                            items(
                                items = scheduleState.data,
                                itemContent = { item ->
                                    ScheduleListItem(item = item, timeNotation = timeNotation.value, openApp = openAppValue.value)
                                }
                            )
                            item {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                )
                            }
                        }
                        val scope = rememberCoroutineScope()

                        FloatingActionButton(
                            modifier = Modifier
                                .constrainAs(speedDialFab) {
                                    bottom.linkTo(banner.top, 16.dp)
                                    end.linkTo(parent.end, 16.dp)
                                },
                            onClick = {
                                scope.launch {
                                    listState.animateScrollToItem(0)
                                }
                            }
                        ) {
                            Icon(Icons.Filled.ArrowUpward, null)
                        }

                        adBanner(
                            modifier = Modifier
                                .constrainAs(banner) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                }
                        )

                    }
                    is ScheduleState.TodayTomorrow -> {
                        val listState = rememberLazyListState()
                        LazyColumn(state = listState) {
                            stickyHeader {
                                HeaderItem(day = "今日")
                            }
                            items(
                                items = scheduleState.data,
                                itemContent = { item ->
                                    ScheduleListItem(item = item, timeNotation = timeNotation.value, openApp = openAppValue.value)
                                }
                            )
                            stickyHeader {
                                HeaderItem(day = "明日")
                            }
                            items(
                                items = scheduleState.data2,
                                itemContent = { item ->
                                    ScheduleListItem(item = item, timeNotation = timeNotation.value, openApp = openAppValue.value)
                                }
                            )
                            item {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                )
                            }
                        }

                        val scope = rememberCoroutineScope()

                        FloatingActionButton(
                            modifier = Modifier
                                .constrainAs(speedDialFab) {
                                    bottom.linkTo(banner.top, 16.dp)
                                    end.linkTo(parent.end, 16.dp)
                                },
                            onClick = {
                                scope.launch {
                                    listState.animateScrollToItem(0)
                                }
                            }
                        ) {
                            Icon(Icons.Filled.ArrowUpward, null)
                        }
                        adBanner(
                            modifier = Modifier
                                .constrainAs(banner) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                }
                        )
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

                        adBanner(
                            modifier = Modifier
                                .constrainAs(banner) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                }
                        )
                    }
                }
            }
        }
    }
}






