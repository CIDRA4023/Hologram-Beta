package com.cidra.hologram_beta.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.cidra.hologram_beta.BuildConfig
import com.cidra.hologram_beta.ui.screens.archive.component.adBanner

data class ThisAppItem(
    val id: Int,
    val title: String,
    val url: String
)

@Composable
fun ThisAppScreen(navController: NavController, navBackStackEntry: NavBackStackEntry?) {

    val version = BuildConfig.VERSION_NAME
    val context = LocalContext.current


    val sectionItem = listOf(
        ThisAppItem(
            1,
            "利用規約",
            "https://www.notion.so/c1fd899e71474ce0806476aa054a7d76"
        ),
        ThisAppItem(
            2,
            "プライバシーポリシー",
            "https://hexagonal-sort-b18.notion.site/566d5a5dc25b4ff88147fb43900f0515"
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

            val uriHandler = LocalUriHandler.current

            Column(modifier = Modifier.padding(it)) {
                sectionItem.forEach {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.background)
                            .clickable {
                                when (it.id) {
                                    1, 2 -> {
                                        uriHandler.openUri(it.url)
                                    }
                                    3 -> {
                                        navController.navigate("license_screen")
                                    }
                                }
                            }
                            .padding(all = 16.dp)
                    ) {

                        Text(
                            text = it.title,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            adBanner()
        }
    }

}

@Composable
fun ThisAppTopBar(navController: NavController, navBackStackEntry: NavBackStackEntry?) {
    TopAppBar(
        title = { Text(text = "このアプリについて") },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() })
            { Icon(Icons.Filled.ArrowBack, null) }
        },
        elevation = 0.dp
    )
}
