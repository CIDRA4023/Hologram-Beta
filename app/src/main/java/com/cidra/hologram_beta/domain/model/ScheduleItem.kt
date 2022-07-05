package com.cidra.hologram_beta.domain.model


data class ScheduleItem(
    val videoId: String,
    val title: String,
    val thumbnailUrl: String,
    val scheduledStartTime: String,
    val channelName: String,
    val channelIconUrl: String,
    val tagList: List<String> = emptyList(),
    val tagGroup: String = ""
)