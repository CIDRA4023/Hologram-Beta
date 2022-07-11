package com.cidra.hologram_beta.ui.screens.archive.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.domain.model.ArchiveItem
import com.cidra.hologram_beta.ui.*
import com.cidra.hologram_beta.ui.screens.component.MaterialChipGroup
import com.cidra.hologramjetpackcompose.Common.Constants
import java.util.*
import kotlin.time.Duration.Companion.days


@Composable
fun ArchiveListItem(
    item: ArchiveItem,
    openApp: Int,
    modifier: Modifier
) {
    val context = LocalContext.current

    Surface(
        modifier = modifier.clickable { openApp(item.videoId, openApp, context) },
    ) {
        Column() {
            Row(
                modifier = modifier
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    AsyncImage(
                        model = item.thumbnailUrl,
                        contentDescription = "thumbnail image",
                        modifier = Modifier.aspectRatio(ratio = 1.7778f),
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(vertical = 2.dp, horizontal = 4.dp)
                    )
                    {
                        durationFormatter(item.duration)?.let {
                            Text(
                                text = it,
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }

                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.subtitle1.copy(
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AsyncImage(
                            model = item.channelIconUrl,
                            contentDescription = "channel icon image",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(20.dp)
                        )
                        Text(
                            text = item.channelName,
                            modifier = Modifier
                                .alpha((ContentAlpha.medium))
                                .align(alignment = Alignment.CenterVertically),
                            style = MaterialTheme.typography.body2.copy(fontSize = 12.sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    val now = Date().time
                    val dateToLong = dateTimeToLong(item.publishedAt)
                    val elapsed = now - dateToLong
                    val liveStartTime = dateTimeToLong(item.publishedAt)

                    val relativeTime = if (elapsed < 1.days.inWholeMilliseconds) {
                        context.getString(R.string.separate_string) + relativeTimeFormatter(
                            context,
                            now,
                            liveStartTime
                        )
                    } else {
                        ""
                    }

                    val viewers = archiveViewersFormatter(item.viewers.toString())
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = viewers + relativeTime,
                            modifier = Modifier.alpha((ContentAlpha.medium)),
                            style = MaterialTheme.typography.body2,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                    }
                }
            }
            MaterialChipGroup(
                tagGroup = item.tagList,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun ArchiveListItemPrev() {
//    ArchiveListItem()
//}