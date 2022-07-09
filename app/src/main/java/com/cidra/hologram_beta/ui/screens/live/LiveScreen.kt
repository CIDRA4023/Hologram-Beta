package com.cidra.hologram_beta.ui.screens.live

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.ui.screens.archive.component.adBanner
import com.cidra.hologram_beta.ui.screens.live.component.LiveListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.leinardi.android.speeddial.compose.FabWithLabel
import com.leinardi.android.speeddial.compose.SpeedDial
import com.leinardi.android.speeddial.compose.SpeedDialOverlay
import com.leinardi.android.speeddial.compose.SpeedDialState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun LiveScreen(viewModel: LiveScreenViewModel = hiltViewModel()) {

    val state = viewModel.liveItem.value

    val openAppValue =
        viewModel.getOpenApp().collectAsState(initial = R.string.setting_open_app_youtube)



    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        var isRefreshing by remember { mutableStateOf(false) }

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
                viewModel.refresh()
                isRefreshing = false
            },
            modifier = Modifier.fillMaxSize()
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

                if (state.success.isNotEmpty()) {
                    val listState = rememberLazyListState()

                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(
                            items = state.success,
                            key = { it.videoId },
                            itemContent = { live ->
                                LiveListItem(
                                    item = live,
                                    settingValue = openAppValue.value,
                                    modifier = Modifier.animateItemPlacement()
                                )
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
                    var speedDialState by rememberSaveable { mutableStateOf(SpeedDialState.Collapsed) }
                    var overlayVisible: Boolean by rememberSaveable { mutableStateOf(speedDialState.isExpanded()) }
                    val scope = rememberCoroutineScope()

                    SpeedDialOverlay(
                        visible = overlayVisible,
                        onClick = {
                            overlayVisible = false
                            speedDialState = speedDialState.toggle()
                        },
                        color = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.medium)
                    )

                    SpeedDial(
                        state = speedDialState,
                        onFabClick = { expanded ->
                            speedDialState = speedDialState.toggle()
                            overlayVisible = speedDialState.isExpanded()
                        },
                        modifier = Modifier
                            .constrainAs(speedDialFab) {
                                bottom.linkTo(banner.top, 16.dp)
                                end.linkTo(parent.end, 16.dp)
                            },
                        fabClosedContent = { Icon(Icons.Filled.Sort, null) }
                    ) {
                        item {
                            FabWithLabel(
                                onClick = {
                                    scope.launch {
                                        overlayVisible = false
                                        speedDialState = speedDialState.toggle()
                                        viewModel.sortByCurrentViewer()
                                        delay(500)
                                        listState.animateScrollToItem(0)
                                    }
                                },
                                labelContent = { Text(text = stringResource(id = R.string.archive_fab_label_viewers)) },
                            ) {
                                Icon(Icons.Filled.Visibility, null)
                            }
                        }
                        item {
                            FabWithLabel(
                                onClick = {
                                    scope.launch {
                                        overlayVisible = false
                                        speedDialState = speedDialState.toggle()
                                        viewModel.sorByStartTime()
                                        delay(500)
                                        listState.animateScrollToItem(0)
                                    }
                                },
                                labelContent = { Text(text = stringResource(id = R.string.archive_fab_label_update)) },
                            ) {
                                Icon(Icons.Filled.Schedule, null)
                            }
                        }
                    }


                } else if (state.isLoading.not() && state.success.isEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.live_item_empty),
                                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        item {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            )
                        }
                    }
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
