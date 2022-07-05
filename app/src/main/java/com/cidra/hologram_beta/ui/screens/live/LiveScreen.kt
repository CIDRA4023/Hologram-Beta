package com.cidra.hologram_beta.ui.screens.live

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.ui.screens.live.component.LiveListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


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
            if (state.live.isNotEmpty()) {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = state.live,
                        itemContent = { live ->
                            LiveListItem(item = live, settingValue = openAppValue.value)
                        }
                    )
                }
            } else if (state.isLoading.not() && state.live.isEmpty()) {
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
                }
            }

        }


        Box() {
            if (state.error.isNotBlank()) {
                Text(
                    text = "通信エラー",
                    color = androidx.compose.material.MaterialTheme.colors.error,
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

}
