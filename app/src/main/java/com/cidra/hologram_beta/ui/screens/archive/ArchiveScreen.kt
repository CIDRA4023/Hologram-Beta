package com.cidra.hologram_beta.ui.screens.archive

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.leinardi.android.speeddial.compose.FabWithLabel
import com.leinardi.android.speeddial.compose.SpeedDial
import com.leinardi.android.speeddial.compose.SpeedDialOverlay
import com.leinardi.android.speeddial.compose.SpeedDialState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class, ExperimentalAnimationApi::class
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
//        val showFAB by remember {
//            derivedStateOf {
//                listState.firstVisibleItemIndex > 0
//            }
//        }
//        AnimatedVisibility(
//            visible = showFAB,
//            modifier = Modifier
//                .constrainAs(speedDial) {
//                    bottom.linkTo(chipGroup.top, 16.dp)
//                    end.linkTo(parent.end, 16.dp)
//                },
//            enter = fadeIn(),
//            exit = fadeOut()
//        ) {
//
//
//        }

        var speedDialState by rememberSaveable { mutableStateOf(SpeedDialState.Collapsed) }
        var overlayVisible: Boolean by rememberSaveable { mutableStateOf(speedDialState.isExpanded()) }

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
                .constrainAs(speedDial) {
                    bottom.linkTo(chipGroup.top, 16.dp)
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
                            viewModel.sortByViewer()
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
                            viewModel.sorByUpdate()
                            delay(500)
                            listState.animateScrollToItem(0)
                        }
                    },
                    labelContent = { Text(text = stringResource(id = R.string.archive_fab_label_update)) },
                ) {
                    Icon(Icons.Filled.Schedule, null)
                }
            }
            item {
                FabWithLabel(
                    onClick = {
                        scope.launch {
                            overlayVisible = false
                            speedDialState = speedDialState.toggle()
                            viewModel.sortByGood()
                            delay(500)
                            listState.animateScrollToItem(0)
                        }
                    },
                    labelContent = { Text(text = stringResource(id = R.string.archive_fab_label_good)) },
                ) {
                    Icon(Icons.Filled.ThumbUp, null)
                }
            }
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




