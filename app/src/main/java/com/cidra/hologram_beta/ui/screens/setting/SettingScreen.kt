package com.cidra.hologram_beta.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.ui.screens.archive.component.adBanner


data class SettingItem(
    val id: Int,
    val title: String,
    val settingValue: Int,
    val icon: ImageVector
)

@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    navController: NavController,
    navBackStackEntry: NavBackStackEntry?
) {
    Scaffold(
        topBar = { SettingTopBar(navController, navBackStackEntry) },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                SettingContent(viewModel)
            }
        }
    )
}

@Composable
fun SettingTopBar(navController: NavController, navBackStackEntry: NavBackStackEntry?) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = { Text("設定") },
        navigationIcon = {
            IconButton(onClick = {
                if (navBackStackEntry?.destination?.route != "main") {
                    navController.popBackStack()
                }
            }
            ) { Icon(Icons.Filled.ArrowBack, null) }
        },
        elevation = 0.dp
    )
}

@Composable
fun SettingContent(viewModel: SettingViewModel) {

    val setGroupFilterValue =
        viewModel.getGroupFilter().collectAsState(initial = R.string.setting_filter_holo_JP)
    val setTimeNotationValue =
        viewModel.getTimeNotation().collectAsState(initial = R.string.setting_time_notation_12hour)
    val setOpenAppValue =
        viewModel.getOpenApp().collectAsState(initial = R.string.setting_open_app_youtube)

    val sectionItem = listOf(
        SettingItem(
            1,
            "グループフィルター",
            setGroupFilterValue.value,
            Icons.Filled.FilterAlt
        ),
        SettingItem(
            2,
            "時刻表記",
            setTimeNotationValue.value,
            Icons.Filled.Schedule
        ),
        SettingItem(
            3,
            "YouTubeを開くアプリの選択",
            setOpenAppValue.value,
            Icons.Filled.Launch
        )
    )
    val groupFilterDialog = remember { mutableStateOf(false) }
    val setTimeDialog = remember { mutableStateOf(false) }
    val openAppDialog = remember { mutableStateOf(false) }


    Column {
        Text(
            text = "表示設定",
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(16.dp)
        )
        sectionItem.forEach {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.background)
                    .clickable {
                        when (it.id) {
                            1 -> {
                                groupFilterDialog.value = true
                            }
                            2 -> {
                                setTimeDialog.value = true
                            }
                            3 -> {
                                openAppDialog.value = true
                            }
                        }
                    }
                    .padding(all = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        it.icon,
                        null,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Column {

                        Text(
                            text = it.title,
                            color = MaterialTheme.colors.onSurface
                        )

                        Text(
                            text = stringResource(id = it.settingValue),
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.alpha(ContentAlpha.medium)
                        )
                    }

                }
            }

            if (groupFilterDialog.value) {
                FilterGroupDialog(
                    viewModel = viewModel,
                    setGroupFilterValue.value,
                    openDialog = groupFilterDialog
                )
            }

            if (setTimeDialog.value) {
                TimeNotationDialog(
                    viewModel = viewModel,
                    setTimeNotationValue.value,
                    openDialog = setTimeDialog
                )
            }

            if (openAppDialog.value) {
                OpenAppDialog(
                    viewModel = viewModel,
                    setOpenAppValue.value,
                    openDialog = openAppDialog
                )
            }

        }
    }
    adBanner()


}

@Composable
fun FilterGroupDialog(
    viewModel: SettingViewModel,
    settingValue: Int,
    openDialog: MutableState<Boolean>
) {
    val buttonList = listOf(
        R.string.setting_filter_holo_JP,
        R.string.setting_filter_holo_ID,
        R.string.setting_filter_holo_EN,
        R.string.setting_filter_holo_stars
    )
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "表示内容の切り替え") },
            text = { FilterRadioButtonGroup(buttonList, viewModel, settingValue, openDialog) },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(stringResource(id = R.string.setting_dialog_cancel_label))
                }
            }
        )
    }
}

@Composable
fun TimeNotationDialog(
    viewModel: SettingViewModel,
    settingValue: Int,
    openDialog: MutableState<Boolean>
) {
    val buttonList = listOf(
        R.string.setting_time_notation_12hour,
        R.string.setting_time_notation_24hour
    )
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "時刻表記の切り替え") },
            text = {
                TimeNotationRadioButtonGroup(
                    buttonList,
                    viewModel,
                    settingValue,
                    openDialog
                )
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(stringResource(id = R.string.setting_dialog_cancel_label))
                }
            }
        )
    }
}

@Composable
fun OpenAppDialog(
    viewModel: SettingViewModel,
    settingValue: Int,
    openDialog: MutableState<Boolean>
) {
    val buttonList = listOf(
        R.string.setting_open_app_youtube,
        R.string.setting_open_app_browser,
        R.string.setting_open_app_web_view
    )
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text(text = "表示内容の切り替え") },
            text = { OpenAppRadioButtonGroup(buttonList, viewModel, settingValue, openDialog) },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) {
                    Text(stringResource(id = R.string.setting_dialog_cancel_label))
                }
            }
        )
    }
}

@Composable
fun FilterRadioButtonGroup(
    buttonList: List<Int>,
    viewModel: SettingViewModel,
    settingGroup: Int,
    openDialog: MutableState<Boolean>
) {
    Column {
        buttonList.forEachIndexed { index, listItem ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = settingGroup == buttonList[index],
                    onClick = {
                        viewModel.setGroupFilter(buttonList[index])
                        openDialog.value = false
                    }
                )
                Text(stringResource(id = listItem))
            }
        }
    }
}

@Composable
fun TimeNotationRadioButtonGroup(
    buttonList: List<Int>,
    viewModel: SettingViewModel,
    settingGroup: Int,
    openDialog: MutableState<Boolean>
) {
    Column {
        buttonList.forEachIndexed { index, listItem ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = settingGroup == buttonList[index],
                    onClick = {
                        viewModel.setTimeNotation(buttonList[index])
                        openDialog.value = false
                    }

                )
                Text(stringResource(id = listItem))
            }
        }
    }
}

@Composable
fun OpenAppRadioButtonGroup(
    buttonList: List<Int>,
    viewModel: SettingViewModel,
    settingGroup: Int,
    openDialog: MutableState<Boolean>
) {
    Column {
        buttonList.forEachIndexed { index, listItem ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = settingGroup == buttonList[index],
                    onClick = {
                        viewModel.setOpenApp(buttonList[index])
                        openDialog.value = false
                    }

                )
                Text(stringResource(id = listItem))
            }
        }
    }
}
