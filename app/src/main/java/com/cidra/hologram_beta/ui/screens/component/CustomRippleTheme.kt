package com.cidra.hologram_beta.ui.screens.component

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object CustomRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color {
        return MaterialTheme.colors.primary
    }

    @Composable
    override fun rippleAlpha(): RippleAlpha {
        return RippleTheme.defaultRippleAlpha(
            contentColor = LocalContentColor.current,
            lightTheme = MaterialTheme.colors.isLight
        )
    }
}