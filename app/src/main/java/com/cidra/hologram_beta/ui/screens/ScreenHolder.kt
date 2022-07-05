package com.cidra.hologram_beta.ui.screens

import com.cidra.hologram_beta.R


sealed class ScreenHolder(val tabName: Int) {
    object Live : ScreenHolder(tabName = R.string.tab_name_live)
    object Schedule : ScreenHolder(tabName = R.string.tab_name_schedule)
    object Archive : ScreenHolder(tabName = R.string.tab_name_archive)
}
