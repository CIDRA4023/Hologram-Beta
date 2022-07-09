package com.cidra.hologram_beta.ui.screens.schedule.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HeaderItem(day: String) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)

            ) {
                Divider(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight(),
                    color = MaterialTheme.colors.secondary
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "${day}の配信",
                    style = MaterialTheme.typography.h5
                )
            }
        }

    }

}
