package com.cidra.hologram_beta.ui.screens.archive

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.cidra.hologram_beta.R

import com.cidra.hologram_beta.ui.dateAgo
import com.cidra.hologram_beta.ui.screens.archive.component.ArchiveListItem
import com.cidra.hologram_beta.ui.screens.archive.component.ChipGroup
import com.cidra.hologram_beta.ui.screens.component.LazyBox
import com.cidra.hologram_beta.ui.screens.component.ModifierCacheHolder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import de.charlex.compose.SpeedDialData
import de.charlex.compose.SpeedDialFloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ArchiveScreen(viewModel: ArchiveScreenViewModel = hiltViewModel()) {

    val state = viewModel.archiveItem.value

    val sdfD = SimpleDateFormat("dd", Locale.getDefault())

    val selectedChip = remember { mutableStateOf(0) }
    Log.i("selectedChip", "${selectedChip.value}")


    val chipContentMap = mutableMapOf(
        0 to "今日",
        -1 to "昨日",
        -2 to sdfD.format(dateAgo(-2)),
        -3 to sdfD.format(dateAgo(-3)),
        -4 to sdfD.format(dateAgo(-4)),
        -5 to sdfD.format(dateAgo(-5)),
        -6 to sdfD.format(dateAgo(-6)),
        -7 to sdfD.format(dateAgo(-7))
    )

    val success = state.success


    Log.i("ViewModel", "Archive")

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (listItem, chipGroup, speedDial) = createRefs()

        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        var isRefreshing by remember { mutableStateOf(false) }

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
                viewModel.refresh(selectedChip.value)
                isRefreshing = false
            },
            modifier = Modifier.constrainAs(listItem) {
                top.linkTo(parent.top)
                bottom.linkTo(chipGroup.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
        ) {

            val modifierCacheHolder = remember {
                ModifierCacheHolder()
            }


            LazyBox(
                delayMilliSec = 10,
                placeHolder = {
//                    Spacer(
//                        modifier = modifierCacheHolder.getOrCreate(tag = "MainRowPlaceHolder") {
//                            Modifier.size(8.dp * 2 + 120.dp * 9f / 16)
//                        },
//                    )
                },
//                modifier = Modifier.constrainAs(listItem) {
//                    top.linkTo(parent.top)
//                    start.linkTo(parent.start)
//
//                }
            ) {
                LazyColumn(
                    state = listState
                ) {
                    items(
                        items = success,
                        key = { it.videoId }
                    ) { success ->
                        ArchiveListItem(item = success, modifier = Modifier.animateItemPlacement())
                    }
                }
            }


        }


        Row(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .padding(horizontal = 8.dp)
                .height(54.dp)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .constrainAs(chipGroup) {
//                    start.linkTo(parent.start)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ChipGroup(chipContentMap, selectedChip, viewModel)
        }
        val showFAB by remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }

        AnimatedVisibility(
            visible = showFAB,
            modifier = Modifier
                .constrainAs(speedDial) {
                    bottom.linkTo(chipGroup.top, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                },
            enter = fadeIn(),
            exit = fadeOut()
        ) {

            SpeedDialFAB(
                modifier = Modifier
                    .wrapContentSize(),
                viewModel,
                listState
            )
        }

    }


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
            CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpeedDialFAB(modifier: Modifier, viewModel: ArchiveScreenViewModel, listState: LazyListState) {
    val scope = rememberCoroutineScope()

    SpeedDialFloatingActionButton(
        modifier = modifier,
        initialExpanded = false,
        onClick = { speedDialData: SpeedDialData? ->
            if (speedDialData != null) {
                when (speedDialData.name) {
                    "fab_viewer" -> {
                        scope.launch {
                            viewModel.sortByViewer()
                            delay(500)
                            listState.animateScrollToItem(0)
                        }
                        Log.i("speedDial", "speed1")
                    }
                    "fab_update" -> {
                        scope.launch {
                            viewModel.sorByUpdate()
                            delay(500)
                            listState.animateScrollToItem(0)
                        }
                        Log.i("speedDial", "speed2")
                    }
                    "fab_good" -> {
                        scope.launch {
                            viewModel.sortByGood()
                            delay(500)
                            listState.animateScrollToItem(0)
                        }
                        Log.i("speedDial", "speed3")
                    }
                }

            }
        },
        animationDuration = 200,
        animationDelayPerSelection = 50,
        speedDialData = listOf(
            SpeedDialData(
                name = "fab_viewer",
                label = "再生回数",
                painter = painterResource(id = R.drawable.ic_baseline_visibility_24)
            ),
            SpeedDialData(
                name = "fab_update",
                label = "投稿時間",
                painter = painterResource(id = R.drawable.ic_baseline_schedule_24)
            ),
            SpeedDialData(
                name = "fab_good",
                label = "いいね",
                painterResource = R.drawable.ic_baseline_thumb_up_24
            )
        ),
        showLabels = true,
        fabBackgroundColor = MaterialTheme.colors.secondary,
        fabContentColor = MaterialTheme.colors.onSecondary,
        speedDialBackgroundColor = MaterialTheme.colors.secondary,
        speedDialContentColor = MaterialTheme.colors.onSecondary


    )

}



