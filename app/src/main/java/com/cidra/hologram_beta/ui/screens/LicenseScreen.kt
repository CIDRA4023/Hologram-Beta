package com.cidra.hologram_beta.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cidra.hologram_beta.R

data class LicenseItem(
    val id: Int,
    val title: String,
    val copyright: String,
    val desc: String,
    val url: String
)

@Composable
fun LicenseScreen(navController: NavController) {
    val sectionItem = listOf(
        LicenseItem(
            0,
            "Coil",
            "",
            stringResource(id = R.string.license_desc_coil),
            "https://github.com/coil-kt/coil"
        ),
        LicenseItem(
            1,
            "SpeedDialFloatingActionButton",
            "© 2021 ch4rl3x",
            stringResource(id = R.string.license_desc_SpeedDialFloatingActionButton),
            "https://github.com/ch4rl3x/SpeedDialFloatingActionButton"
        ),
        LicenseItem(
            3,
            "Twemoji",
            "© 2019 Twitter, Inc and other contributors",
            stringResource(id = R.string.license_desc_twemoji),
            "https://github.com/twitter/twemoji"
        ),

        )

    data class LicenseItem(
        val id: Int,
        val title: String,
        val desc: String,
        val url: String
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ライセンス") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                },
                elevation = 0.dp
            )
        }
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            sectionItem.forEach {
                LicenseSectionItem(licenceItem = it)
                Divider()
            }
        }
    }
}

@Composable
fun LicenseSectionItem(licenceItem: LicenseItem) {
    val uriHandler = LocalUriHandler.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            uriHandler.openUri(licenceItem.url)
        }
        .padding(16.dp)
    ) {
        Text(
            text = licenceItem.title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(8.dp)
        )
        if (licenceItem.copyright.isNotEmpty()) {
            Text(
                licenceItem.copyright,
                modifier = Modifier
                    .padding(4.dp)
                    .alpha(ContentAlpha.medium)
            )
        }
        Text(
            licenceItem.desc,
            modifier = Modifier
                .padding(8.dp)
                .alpha(ContentAlpha.medium)
        )
    }
}
