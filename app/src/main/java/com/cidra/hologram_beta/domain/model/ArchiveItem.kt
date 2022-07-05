package com.cidra.hologram_beta.domain.model

import androidx.compose.runtime.Stable

@Stable
data class ArchiveItem(
    val videoId: String,
    val title: String,
    val thumbnailUrl: String,
    val publishedAt: String,
    val viewers: Int,
    val likeCount: Int,
    val channelName: String,
    val channelIconUrl: String,
    val duration: String,
    val tagList: List<String>,
    val tagGroup: String
)