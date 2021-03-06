package com.cidra.hologram_beta.ui.screens.component

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.common.TagList
import com.cidra.hologramjetpackcompose.Common.Constants


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MaterialChipGroup(tagGroup: List<String>, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val idList = TagList.nameToId

    val intent = Intent(Intent.ACTION_VIEW)


    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Log.i("tagGroup", "$tagGroup")
        items(
            items = tagGroup,
            itemContent = {
                val chipLabel = selectChipLabel(tagText = it)
                Chip(
                    onClick = {
                        val categoryList = TagList.categoryName
                        if (categoryList.contains(chipLabel)) {
                            return@Chip
                        } else {
                            val channelUrl = Constants.YOUTUBE_CHANNEL_BASE_URL + idList["$it"]
                            Log.i("chip", it)
                            intent.data = Uri.parse(channelUrl)
                            context.startActivity(intent)
                        }
                    },

                    leadingIcon = {
                        val categoryList = TagList.categoryName
                        if (!categoryList.contains(chipLabel)) {

                            selectChipFanMarkIcon(it)?.let { iconId ->
                                Image(
                                    painter = painterResource(id = iconId),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .size(ChipDefaults.LeadingIconSize)
                                        .wrapContentSize(align = Alignment.Center)
                                )
                            }

                        } else {
                            selectChipCategoryIcon(it)?.let { imageVector ->
                                Icon(
                                    imageVector = imageVector,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(ChipDefaults.LeadingIconSize)
                                        .wrapContentSize(align = Alignment.Center)
                                )
                            }
                        }
                    },
                    border = ChipDefaults.outlinedBorder,
                    colors = ChipDefaults.outlinedChipColors(
                        disabledContentColor = Color.Transparent
                    ),
                    content = {
                        Text(
                            text = selectChipLabel(tagText = it),
                            style = MaterialTheme.typography.body2
//                                    .copy(fontSize = 12.sp)
                        )
                    }
                )

            }
        )
    }
}


@Composable
fun selectChipLabel(tagText: String): String {
    return when (tagText) {
        "game" -> stringResource(R.string.tag_category_game)
        "sing" -> stringResource(R.string.tag_category_sing)
        "live" -> stringResource(R.string.tag_category_live)
        "chat" -> stringResource(R.string.tag_category_chat)
        "birthday" -> stringResource(R.string.tag_category_birthday)
        "song" -> stringResource(R.string.tag_category_song)
        "drawing" -> stringResource(R.string.tag_category_drawing)
        "watchAlong" -> stringResource(R.string.tag_category_watch_along)
        "cover" -> stringResource(R.string.tag_category_cover)
        "hololive ??????????????? " -> stringResource(R.string.tag_member_hololive)
        else -> tagText
    }


}

fun selectChipCategoryIcon(chipLabel: String): ImageVector? {
    return when (chipLabel) {
        "game" -> Icons.Filled.SportsEsports
        "sing" -> Icons.Filled.MusicNote
        "chat" -> Icons.Filled.Chat
        "birthday" -> Icons.Filled.Cake
        "drawing" -> Icons.Filled.Draw
        "cooking" -> Icons.Filled.Restaurant
        "ASMR" -> Icons.Filled.Headphones
        else -> null
    }
}


fun selectChipFanMarkIcon(chipLabel: String): Int? {

    return when (chipLabel) {

        "???????????????" -> R.drawable.ic_sora
        "?????????" -> R.drawable.ic_roboko
        "???????????????" -> R.drawable.ic_sakuramiko
        "??????????????????", "Suisei" -> R.drawable.ic_suisei
        "AZKi" -> R.drawable.ic_azuki
        "????????????" -> R.drawable.ic_meru
        "??????????????????????????????", "????????????" -> R.drawable.ic_akirose
        "???????????????" -> R.drawable.ic_akaihaato
        "???????????????" -> R.drawable.ic_fubuki
        "???????????????" -> R.drawable.ic_matsuri
        "????????????" -> R.drawable.ic_aqua
        "???????????????" -> R.drawable.ic_shion
        "???????????????" -> R.drawable.ic_ayame
        "???????????????" -> R.drawable.ic_choko
        "???????????????" -> R.drawable.ic_subaru
        "????????????" -> R.drawable.ic_mio
        "???????????????" -> R.drawable.ic_okayu
        "???????????????" -> R.drawable.ic_korone
        "???????????????" -> R.drawable.ic_pekora
        "???????????????" -> R.drawable.ic_rushia
        "??????????????????" -> R.drawable.ic_flare
        "???????????????" -> R.drawable.ic_noel
        "???????????????" -> R.drawable.ic_houshoumarin
        "???????????????" -> R.drawable.ic_amanekanata
        "???????????????" -> R.drawable.ic_watame
        "????????????" -> R.drawable.ic_towa
        "???????????????" -> R.drawable.ic_luna
        "???????????????" -> R.drawable.ic_ramy
        "????????????" -> R.drawable.ic_nene
        "???????????????" -> R.drawable.ic_botan
        "???????????????" -> R.drawable.ic_poruka
        "??????????????????????????????" -> R.drawable.ic_laplusdarknesss_1
        "????????????" -> R.drawable.ic_takanelui
        "???????????????" -> R.drawable.ic_hakuikoyori
        "??????????????????" -> R.drawable.ic_sakamatakuroe
        "???????????????" -> R.drawable.ic_kazamairohach
        "IOFI", "Iofi" -> R.drawable.ic_iofi
        "Moona", "?????????" -> R.drawable.ic_moona
        "Risu" -> R.drawable.ic_risu
        "Ollie" -> R.drawable.ic_ollie
        "Anya" -> R.drawable.ic_anya
        "Reine" -> R.drawable.ic_reine
        "Kobo" -> R.drawable.ic_kobo
        "Kaela" -> R.drawable.ic_kaela
        "Zeta" -> R.drawable.ic_zeta
        "Calliope" -> R.drawable.ic_calliope
        "Kiara" -> R.drawable.ic_takanashikiara
        "Ina'nis" -> R.drawable.ic_inanis
        "Gura" -> R.drawable.ic_gura
        "Amelia" -> R.drawable.ic_ameliawatson
        "IRyS" -> R.drawable.ic_irys
        "Sana" -> R.drawable.ic_sana
        "Fauna" -> R.drawable.ic_fauna
        "Kronii" -> R.drawable.ic_kronii
        "Mumei" -> R.drawable.ic_mumei
        "Baelz" -> R.drawable.ic_baelz
        "???????????????" -> R.drawable.ic_miyabi
        "???????????????" -> R.drawable.ic_izuru
        "?????????????????????" -> R.drawable.ic_arurandeisu
        "??????" -> R.drawable.ic_rikka
        "????????????" -> R.drawable.ic_astel
        "????????????" -> R.drawable.ic_temma_1
        "???????????????" -> R.drawable.ic_roberu
        "???????????????" -> R.drawable.ic_shien
        "???????????????" -> R.drawable.ic_oga
        "???????????????" -> R.drawable.ic_fuma
        "????????????" -> R.drawable.ic_uyu
        "???????????????" -> R.drawable.ic_gamma
        "???????????????" -> R.drawable.ic_rio
        else -> null
    }
}





