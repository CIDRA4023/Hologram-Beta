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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        "hololive ホロライブ " -> stringResource(R.string.tag_member_hololive)
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

        "ときのそら" -> R.drawable.ic_sora
        "ロボ子" -> R.drawable.ic_roboko
        "さくらみこ" -> R.drawable.ic_sakuramiko
        "星街すいせい", "Suisei" -> R.drawable.ic_suisei
        "AZKi" -> R.drawable.ic_azuki
        "夜空メル" -> R.drawable.ic_meru
        "アキ・ローゼンタール", "アキロゼ" -> R.drawable.ic_akirose
        "赤井はあと" -> R.drawable.ic_akaihaato
        "白上フブキ" -> R.drawable.ic_fubuki
        "夏色まつり" -> R.drawable.ic_matsuri
        "湊あくあ" -> R.drawable.ic_aqua
        "紫咲シオン" -> R.drawable.ic_shion
        "百鬼あやめ" -> R.drawable.ic_ayame
        "癒月ちょこ" -> R.drawable.ic_choko
        "大空スバル" -> R.drawable.ic_subaru
        "大神ミオ" -> R.drawable.ic_mio
        "猫又おかゆ" -> R.drawable.ic_okayu
        "戌神ころね" -> R.drawable.ic_korone
        "兎田ぺこら" -> R.drawable.ic_pekora
        "潤羽るしあ" -> R.drawable.ic_rushia
        "不知火フレア" -> R.drawable.ic_flare
        "白銀ノエル" -> R.drawable.ic_noel
        "宝鐘マリン" -> R.drawable.ic_houshoumarin
        "天音かなた" -> R.drawable.ic_amanekanata
        "角巻わため" -> R.drawable.ic_watame
        "常闇トワ" -> R.drawable.ic_towa
        "姫森ルーナ" -> R.drawable.ic_luna
        "雪花ラミィ" -> R.drawable.ic_ramy
        "桃鈴ねね" -> R.drawable.ic_nene
        "獅白ぼたん" -> R.drawable.ic_botan
        "尾丸ポルカ" -> R.drawable.ic_poruka
        "ラプラス・ダークネス" -> R.drawable.ic_laplusdarknesss_1
        "鷹嶺ルイ" -> R.drawable.ic_takanelui
        "博衣こより" -> R.drawable.ic_hakuikoyori
        "沙花叉クロヱ" -> R.drawable.ic_sakamatakuroe
        "風真いろは" -> R.drawable.ic_kazamairohach
        "IOFI", "Iofi" -> R.drawable.ic_iofi
        "Moona", "ムーナ" -> R.drawable.ic_moona
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
        "花咲みやび" -> R.drawable.ic_miyabi
        "奏手イヅル" -> R.drawable.ic_izuru
        "アルランディス" -> R.drawable.ic_arurandeisu
        "律可" -> R.drawable.ic_rikka
        "アステル" -> R.drawable.ic_astel
        "岸堂天真" -> R.drawable.ic_temma_1
        "夕刻ロベル" -> R.drawable.ic_roberu
        "影山シエン" -> R.drawable.ic_shien
        "荒咬オウガ" -> R.drawable.ic_oga
        "夜十神封魔" -> R.drawable.ic_fuma
        "羽継烏有" -> R.drawable.ic_uyu
        "緋崎ガンマ" -> R.drawable.ic_gamma
        "水無世燐央" -> R.drawable.ic_rio
        else -> null
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun ChipIcon() {

    Chip(
        onClick = { },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_botan),
                modifier = Modifier.size(20.dp),
                contentDescription = null
            )
        },
        border = ChipDefaults.outlinedBorder,
        colors = ChipDefaults.outlinedChipColors(
            disabledContentColor = Color.Transparent
        ),

        ) {
        Text(
            text = "獅白ぼたん",
            style = MaterialTheme.typography.body2
                .copy(fontSize = 12.sp)
        )
    }
}





