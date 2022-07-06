package com.cidra.hologram_beta.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cidra.hologram_beta.R
import com.cidra.hologram_beta.ui.screens.ScreenHolder
import com.cidra.hologram_beta.ui.screens.archive.ArchiveScreen
import com.cidra.hologram_beta.ui.screens.component.CustomRippleTheme
import com.cidra.hologram_beta.ui.screens.live.LiveScreen
import com.cidra.hologram_beta.ui.screens.schedule.ScheduleScreen
import com.cidra.hologram_beta.ui.screens.setting.SettingScreen
import com.cidra.hologram_beta.ui.screens.setting.SettingViewModel
import com.cidra.hologram_beta.ui.theme.HologramBeta
import com.google.accompanist.pager.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.ads.MobileAds
import com.google.firebase.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appUpdateService: AppUpdateService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appUpdateService.runAppUpdate(this)

        setContent {
            HologramBeta {
                CompositionLocalProvider(LocalRippleTheme provides CustomRippleTheme) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    NavHost(navController = navController, startDestination = "main") {
                        composable(route = "main") {
                            MainTabScreen(navController)
                        }
                        composable(route = "setting") {
                            val viewModel = hiltViewModel<SettingViewModel>()
                            SettingScreen(viewModel, navController, navBackStackEntry)
                        }
                        composable(route = "this_app") {
                            ThisAppScreen(navController, navBackStackEntry)
                        }
                    }
                }
            }
        }

        //initialize the mobile ads sdk
        MobileAds.initialize(this) {}
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainTabScreen(navController: NavController) {

    val pagerState = rememberPagerState(initialPage = 0)

    Scaffold(topBar = { MainTopBar(navController) }) {
        val systemUiController = rememberSystemUiController()
        val isLightTheme = MaterialTheme.colors.isLight
        val statusBarColor =
            if (isLightTheme) MaterialTheme.colors.primary else MaterialTheme.colors.background

        SideEffect {
            systemUiController.setStatusBarColor(statusBarColor)
        }


        Column(modifier = Modifier.padding(it)) {
            Tabs(pagerState = pagerState)
            TabContent(pagerState = pagerState)
        }
    }
}

@Composable
fun MainTopBar(navController: NavController) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    val menuMargin = 8.dp
    val isLightTheme = MaterialTheme.colors.isLight
    val backgroundColor =
        if (isLightTheme) MaterialTheme.colors.primary else MaterialTheme.colors.background

    val context = LocalContext.current

    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
//        modifier = Modifier.background(Color.Black),
        backgroundColor = backgroundColor,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                Icon(Icons.Filled.MoreVert, null)
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(onClick = { navController.navigate("setting") }) {
                    Icon(Icons.Filled.Settings, null)
                    Spacer(modifier = Modifier.width(menuMargin))
                    Text(text = "設定")
                }
                DropdownMenuItem(onClick = { navController.navigate("this_app") }) {
                    Icon(Icons.Filled.Info, null)
                    Spacer(modifier = Modifier.width(menuMargin))
                    Text(text = "このアプリについて")
                }
                DropdownMenuItem(onClick = { openPlayStore(context) }) {
                    Icon(Icons.Filled.RateReview, null)
                    Spacer(modifier = Modifier.width(menuMargin))
                    Text(text = "レビューの投稿")
                }
                DropdownMenuItem(onClick = { openIntentChooser(context) }) {
                    Icon(Icons.Filled.Share, null)
                    Spacer(modifier = Modifier.width(menuMargin))
                    Text(text = "このアプリを紹介")
                }

            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabList = listOf(
        ScreenHolder.Live.tabName,
        ScreenHolder.Schedule.tabName,
        ScreenHolder.Archive.tabName
    )
    val scope = rememberCoroutineScope()
    val isLightTheme = MaterialTheme.colors.isLight
    val backgroundColor =
        if (isLightTheme) MaterialTheme.colors.primary else MaterialTheme.colors.background
    val contentColor =
        if (isLightTheme) MaterialTheme.colors.background else MaterialTheme.colors.primary



    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = backgroundColor,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        contentColor = contentColor

    ) {
        tabList.forEachIndexed { index, s ->

            Tab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(text = stringResource(id = s))
//                        color = if (pagerState.currentPage == index) Color.White else Color.LightGray)
                }
            )
        }

    }
}

private fun openIntentChooser(context: Context) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, R.string.share_app_desc)
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(
            Intent.createChooser(
                intent,
                null
            )
        )
    }
}


private fun openPlayStore(context: Context) {
    val packageName = BuildConfig.APPLICATION_ID
    val uri: Uri = Uri.parse("market://details?id=$packageName")

    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    goToMarket.addFlags(
        Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK
    )
    try {
        context.startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabContent(pagerState: PagerState) {
    HorizontalPager(count = 3, state = pagerState) {
        when (it) {
            0 -> {
                LiveScreen()
            }
            1 -> {
                ScheduleScreen()
            }
            2 -> ArchiveScreen()
        }
    }
}
