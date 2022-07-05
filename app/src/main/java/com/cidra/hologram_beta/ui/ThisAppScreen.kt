package com.cidra.hologram_beta.ui


import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cidra.hologram_beta.BuildConfig
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class ThisAppItem(
    val id: Int,
    val title: String,
//    val IconResource: ImageVector,
    val url: String
)

@Composable
fun ThisAppScreen(navController: NavController, navBackStackEntry: NavBackStackEntry?) {

    val visibility = remember { mutableStateOf(false) }

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_app") {
        composable(
            "main_app",
        ) { backStackEntry ->
//                    val url = backStackEntry.arguments?.getString("url") ?: ""
            MainThisAppContent(navController = navController, navBackStackEntry)
        }
        composable(
            "web/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        )
        { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            WebViewScreen(navController = navController, url)
        }
    }


}

@Composable
fun MainThisAppContent(navController: NavController, navBackStackEntry: NavBackStackEntry?) {

    val version = BuildConfig.VERSION_NAME
    val context = LocalContext.current


    val sectionItem = listOf(
        ThisAppItem(
            1,
            "利用規約",
            "https://github.com/CIDRA4023/Hologram/blob/master/Terms.md"
        ),
        ThisAppItem(
            2,
            "プライバシーポリシー",
            "https://github.com/CIDRA4023/Hologram/blob/master/PrivacyPolicy.md"
        ),
        ThisAppItem(
            3,
            "ライセンス",
            ""
        )

    )
    Scaffold(
        topBar = { ThisAppTopBar(navController, navBackStackEntry) }
    ) {

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                    Image(imageVector = R.mipmap.ic_launcher, contentDescription = )
//                Icon(
//                    painter = painterResource(id = R.mipmap.ic_launcher),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(64.dp)
//                        .clip(CircleShape)
//                )
                Column {
                    Text(text = "Hologram")
                    Text(text = "version: $version", color = Color.Gray)
                }
            }

            Column(modifier = Modifier.padding(it)) {
                sectionItem.forEach {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.background)
                            .clickable {
                                if (it.id == 1) {
                                    val parsedUrl =
                                        URLEncoder.encode(it.url, StandardCharsets.UTF_8.toString())
                                    navController.navigate("web/${parsedUrl}")
                                } else if (it.id == 3) {

                                }

                            }
                            .padding(all = 16.dp)
                    ) {

                        Text(
                            text = it.title,
                            color = MaterialTheme.colors.onSurface
                        )
//                        Icon(
//                            it.IconResource,
//                            null,
//                            modifier = Modifier.align(Alignment.CenterEnd)
//                        )

                    }
                }


//            Text(
//                text = version,
//                color = Color.Gray,
//                fontSize = 18.sp,
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .padding(bottom = 20.dp)
//                    .clickable {
////                        interactionSource = remember { MutableInteractionSource() },
////                        indication = rememberRipple(
////                            color = MaterialTheme.colors.primary,
////                            bounded = false,
////                            radius = 50.dp
////                        )
//
//                    }
//            )
            }
        }


    }


}

@Composable
fun WebViewScreen(navController: NavController, url: String) {

    val visibility = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "利用規約") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
                elevation = 0.dp
            )
        }
    ) { paddingValue ->


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            AndroidView(
                factory = {
                    WebView(it).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                    }
                },
                update = { webView ->
                    webView.webViewClient = object : WebViewClient() {
                        override fun onPageStarted(
                            view: WebView,
                            url: String,
                            favicon: Bitmap?,
                        ) {
                            visibility.value = true
                        }
                    }
                    webView.loadUrl(url)
                }
            )

        }
    }


}


@Composable
fun ThisAppTopBar(navController: NavController, navBackStackEntry: NavBackStackEntry?) {
    TopAppBar(
        title = { Text(text = "このアプリについて") },
        navigationIcon = {
            IconButton(onClick = {
                if (navBackStackEntry?.destination?.route != "main") {
                    navController.popBackStack()
                }
//                navController.popBackStack()
            }) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        elevation = 0.dp
    )
}
//
//@Preview
//@Composable
//fun ThisAppPrev() {
//    val context = LocalContext.current
//    ThisAppScreen(navController = NavController(context))
//}