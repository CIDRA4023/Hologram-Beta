package com.cidra.hologram_beta.ui.screens.component


import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class ModifierCacheHolder {
    private val map = mutableMapOf<String, Modifier>()

    @SuppressLint("ModifierFactoryExtensionFunction", "ComposableModifierFactory")
    @Composable
    fun getOrCreate(
        tag: String,
        creator: @Composable () -> Modifier,
    ): Modifier = map[tag] ?: creator.invoke().also { map[tag] = it }
}