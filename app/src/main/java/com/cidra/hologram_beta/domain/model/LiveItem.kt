package com.cidra.hologram_beta.domain.model

data class LiveItem(
    val videoId: String,
    val title: String,
    val thumbnailUrl: String,
    val startTime: String = "",
    val currentViewers: String = "",
    val channelName: String,
    val channelIconUrl: String,
    val tagList: List<String> = emptyList(),
    val tagGroup: String = ""
)