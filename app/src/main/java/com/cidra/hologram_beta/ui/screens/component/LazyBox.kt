package com.cidra.hologram_beta.ui.screens.component


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun LazyBox(
    modifier: Modifier = Modifier,
    delayMilliSec: Long,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    placeHolder: @Composable (BoxScope.() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
    ) {
        var showContent by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.Default) {
                delay(delayMilliSec)
                showContent = true
            }
        }

        if (showContent) {
            content.invoke(this)
        } else {
            placeHolder?.invoke(this)
        }
    }
}