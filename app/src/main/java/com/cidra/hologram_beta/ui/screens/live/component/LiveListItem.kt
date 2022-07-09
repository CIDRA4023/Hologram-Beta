package com.cidra.hologram_beta.ui.screens.live.component

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.domain.model.LiveItem
import com.cidra.hologram_beta.ui.dateTimeToLong
import com.cidra.hologram_beta.ui.liveViewersFormatter
import com.cidra.hologram_beta.ui.relativeTimeFormatter
import com.cidra.hologram_beta.ui.screens.component.MaterialChipGroup
import com.cidra.hologramjetpackcompose.Common.Constants
import java.util.*

@Composable
fun LiveListItem(
    item: LiveItem,
    settingValue: Int
) {


    val context = LocalContext.current

    val intent = Intent(ACTION_VIEW)
    intent.data = Uri.parse(Constants.YOUTUBE_WATCH_BASE_URL + item.videoId)

    Card(
        shape = RoundedCornerShape(0)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { openApp(item, settingValue, context) }
        ) {
            val (thumbnailImage, channelImage, title, subtitle, spacer, tagChip) = createRefs()

            AsyncImage(
                model = item.thumbnailUrl,
                contentDescription = "Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(ratio = 1.7778f)
                    .constrainAs(thumbnailImage) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )

            AsyncImage(
                model = item.channelIconUrl,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .constrainAs(channelImage) {
                        start.linkTo(parent.start, margin = 8.dp)
                        top.linkTo(thumbnailImage.bottom, margin = 16.dp)
                        end.linkTo(title.start)
                    }
            )

            Text(
                text = item.title,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier
                    .constrainAs(title) {
                        start.linkTo(channelImage.end)
                        end.linkTo(parent.end)
                        top.linkTo(channelImage.top)
                        bottom.linkTo(subtitle.top)
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = 10.dp, end = 16.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            val now = Date().time
            val liveStartTime = dateTimeToLong(item.startTime)
            val relativeTime = relativeTimeFormatter(context, now, liveStartTime)

            val currentViewers = liveViewersFormatter(item.currentViewers)

            Text(
                text = relativeTime
                        + context.getString(R.string.live_item_start_suffix)
                        + context.getString(R.string.separate_string)
                        + currentViewers,
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 12.sp,
                ),
                modifier = Modifier
                    .alpha(ContentAlpha.medium)
                    .constrainAs(subtitle) {
                        top.linkTo(title.bottom)
                        start.linkTo(title.start)
                        end.linkTo(title.end)
                        width = Dimension.fillToConstraints
                    }
                    .padding(top = 4.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(50.dp)
                    .constrainAs(spacer) {
                        top.linkTo(subtitle.bottom)
                        start.linkTo(parent.start)
                    }
            )

            MaterialChipGroup(
                tagGroup = item.tagList,
                modifier = Modifier
                    .constrainAs(tagChip) {
                        start.linkTo(channelImage.start)
                        top.linkTo(subtitle.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )

        }
    }
}

//@Composable
private fun openApp(item: LiveItem, settingValue: Int, context: Context) {

    val intent = Intent(ACTION_VIEW)
    val url = Constants.YOUTUBE_WATCH_BASE_URL + item.videoId
    intent.data = Uri.parse(url)

    when (settingValue) {
        R.string.setting_open_app_youtube -> {
            context.startActivity(intent)
        }
        R.string.setting_open_app_web_view -> {
//            WebViewScreens(url = url)
        }
        R.string.setting_open_app_browser -> {
            val browserIntent = Intent(ACTION_VIEW, Uri.parse("https://"))
            val defaultResInfo =
                context.packageManager.resolveActivity(
                    browserIntent,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            if (defaultResInfo != null) {
                intent.setPackage(defaultResInfo.activityInfo.packageName)
                context.startActivity(intent)
            }
        }
    }
}

@Preview
@Composable
fun ListItemPreview() {
    val i = LiveItem(
        "0",
        "aaaaaaaaaaaaaaaaaaaaaaaa",
        "https://i.ytimg.com/vi/RGsmcgWw7Z8/sddefault.jpg",
        "1111",
        "11111",
        "aaa",
        "https://i.ytimg.com/vi/RGsmcgWw7Z8/sddefault.jpg",
        listOf("aaa", "bbb"),
        "ttt"
    )
//    LiveListItem(item = i)
}
