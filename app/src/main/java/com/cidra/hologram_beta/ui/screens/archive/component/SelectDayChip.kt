package com.cidra.hologram_beta.ui.screens.archive.component

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cidra.hologram_beta.ui.dateAgo
import com.cidra.hologram_beta.ui.screens.archive.ArchiveScreenViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val selectedColor =
        if (selected) MaterialTheme.colors.primary.copy(alpha = 0.16f) else MaterialTheme.colors.onSurface.copy(
            alpha = 0.12f
        ).compositeOver(MaterialTheme.colors.surface)
    Surface(
        onClick = onClick,
        modifier = Modifier.height(32.dp),
        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
        color = selectedColor,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 14.sp,
                    color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
                )
            )
        }
    }
}

@Composable
fun ChipGroup(
    chipContentMap: MutableMap<Int, String>,
    selectedChip: MutableState<Int>,
    viewModel: ArchiveScreenViewModel
) {
    if (chipContentMap.isNotEmpty()) {
        /*
        selectedItem:現在の選択状態
         */
        val selectedItem = remember {
            mutableStateOf(chipContentMap[0])
        }

        chipContentMap.forEach { (amount, item) ->
//            Chip(
//                onClick = {
//                    selectedItem.value = item
//                    selectedChip.value = amount
//                    Log.i("chip", "$amount")
//                    viewModel.filterItem(dateAgo(amount))
//                },
//                enabled = selectedItem.value == item
//            ) {
//                Text(text = item)
//            }
            SelectableChip(
                text = item,
                selected = selectedItem.value == item,
                onClick = {
                    selectedItem.value = item
                    selectedChip.value = amount
                    Log.i("chip", "$amount")
                    viewModel.filteredDayChip(dateAgo(amount))
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun SelectDayChipPrev() {

    val chipState = remember {
        mutableStateOf(false)
    }
//    val interactionSource = remember { MutableInteractionSource() }
//    val isPressed by interactionSource.collectIsPressedAsState()
    val color = if (chipState.value) Color.Blue else Color.Green
    Chip(
        onClick = { chipState.value = true },
        colors = ChipDefaults.chipColors(backgroundColor = color),
        enabled = chipState.value,
        content = { Text(text = "優木せつ菜さんかわいい") })
}
