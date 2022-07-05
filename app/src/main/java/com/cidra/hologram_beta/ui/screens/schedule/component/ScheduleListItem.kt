package com.cidra.hologram_beta.ui.screens.schedule.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.cidra.hologram_beta.domain.model.ScheduleItem
import com.cidra.hologram_beta.ui.screens.component.MaterialChipGroup
import com.cidra.hologram_beta.ui.timeNotationFormatter
import com.cidra.hologramjetpackcompose.Common.Constants

@Composable
fun ScheduleListItem(
    item: ScheduleItem,
    settingValue: Int
) {

    val context = LocalContext.current

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(Constants.YOUTUBE_WATCH_BASE_URL + item.videoId)

    Surface() {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            val (scheduleTime, card, line, icon) = createRefs()

            Card(modifier = Modifier
                .clickable { context.startActivity(intent) }
                .constrainAs(card) {
                    top.linkTo(scheduleTime.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                    start.linkTo(scheduleTime.end, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                },
                border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f)),
                elevation = 0.dp
            ) {
                CardContent(item)
            }
            Text(
                text = timeNotationFormatter(item.scheduledStartTime, settingValue),
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.constrainAs(scheduleTime) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 16.dp)
                    bottom.linkTo(line.top)
                }
            )

            Divider(
                modifier = Modifier
                    .constrainAs(line) {
                        top.linkTo(scheduleTime.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(scheduleTime.start)
                        end.linkTo(scheduleTime.end)
                        width = Dimension.value(2.dp)
                        height = Dimension.fillToConstraints
                    },
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            )


            AsyncImage(
                model = item.channelIconUrl,
                contentDescription = "sakuramiko",
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        1.dp,
                        MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                        CircleShape
                    )
                    .size(48.dp)
                    .constrainAs(icon) {
                        top.linkTo(scheduleTime.bottom, 16.dp)
                        start.linkTo(scheduleTime.start)
                        end.linkTo(scheduleTime.end)
                    }
            )
        }
    }
}

@Composable
fun CardContent(scheduleItem: ScheduleItem) {
    Column() {
        AsyncImage(
            model = scheduleItem.thumbnailUrl,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1.7778f),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
        Text(
            text = scheduleItem.title,
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
            )
        )
        MaterialChipGroup(
            tagGroup = scheduleItem.tagList,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
        )
    }
}