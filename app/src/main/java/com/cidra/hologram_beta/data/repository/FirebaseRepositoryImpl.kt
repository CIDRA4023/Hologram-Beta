package com.cidra.hologram_beta.data.repository

import android.util.Log
import com.cidra.hologram_beta.common.Resource
import com.cidra.hologram_beta.domain.model.ArchiveItem
import com.cidra.hologram_beta.domain.model.LiveItem
import com.cidra.hologram_beta.domain.model.ScheduleItem
import com.cidra.hologram_beta.domain.repository.FirebaseRepository
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class FirebaseRepositoryImpl @Inject constructor() : FirebaseRepository {
    private val database = Firebase.database
    private val ref = database.getReference("/video")

    override fun fetchLiveItem() = callbackFlow<Resource<List<LiveItem>>> {
        val liveItemList = mutableListOf<LiveItem>()

        val videoItems = ref.orderByChild("eventType").equalTo("live").get()



        trySend(Resource.Loading())
        videoItems.addOnSuccessListener { snapShot ->
            snapShot.children.forEach {

                val currentViewers = when (it.child("likeCount").value.toString()) {
                    "プレミアム公開中" -> 99999999 // 並び替え時に先頭表示かつcurrentViewerで分岐するため
                    else -> it.child("currentViewers").value.toString()
                }.toString()

                val tagList = listOf(it.child("tag").child("category").value.toString()) +
                        it.child("tag").child("member").value.toString().split(",")
                val filterNotNullTagList = tagList.filter { tagLabel ->
                    tagLabel != "null"
                }
                val tagGroup = it.child("tag").child("group").value.toString()

                val singleItem = LiveItem(
                    videoId = it.key.toString(),
                    title = it.child("title").value.toString(),
                    currentViewers = currentViewers,
                    thumbnailUrl = it.child("thumbnailUrl").value.toString(),
                    startTime = it.child("startTime").value.toString(),
                    channelName = it.child("channelName").value.toString(),
                    channelIconUrl = it.child("channelIconUrl").value.toString(),
                    tagList = filterNotNullTagList,
                    tagGroup = tagGroup
                )
                Log.i("liveItem", it.child("title").value.toString())
                liveItemList.add(singleItem)
            }
            trySend(Resource.Success(liveItemList))
        }.addOnFailureListener {
            trySend(Resource.Error("${it.message}"))

        }
        awaitClose { }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchScheduleItem() = callbackFlow<Resource<List<ScheduleItem>>> {
        val scheduleItem = mutableListOf<ScheduleItem>()

        val videoItems = ref.orderByChild("eventType").equalTo("upcoming").get()


        trySend(Resource.Loading())

        videoItems.addOnSuccessListener { snapShot ->
            snapShot.children.forEach {
                val tagList = listOf(it.child("tag").child("category").value.toString()) +
                        it.child("tag").child("member").value.toString().split(",")
                val filterNotNullTagList = tagList.filter { tagLabel ->
                    tagLabel != "null"
                }
                val tagGroup = it.child("tag").child("group").value.toString()

                val singleItem = ScheduleItem(
                    it.key.toString(),
                    it.child("title").value.toString(),
                    it.child("thumbnailUrl").value.toString(),
                    it.child("scheduledStartTime").value.toString(),
                    it.child("channelName").value.toString(),
                    it.child("channelIconUrl").value.toString(),
                    tagList = filterNotNullTagList,
                    tagGroup = tagGroup
                )
                scheduleItem.add(singleItem)
            }
            // 配信予定順に並びかえ
            scheduleItem.sortBy { it.scheduledStartTime }
            trySend(Resource.Success(scheduleItem))
        }.addOnFailureListener {
            trySend(Resource.Error("${it.message}"))
        }
        awaitClose { }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchArchiveItem() = callbackFlow<Resource<List<ArchiveItem>>> {
        val archiveItem = mutableListOf<ArchiveItem>()

        val videoItems = ref.orderByChild("eventType").equalTo("none").get()


        trySend(Resource.Loading())

        videoItems.addOnSuccessListener { snapShot ->
            snapShot.children.forEach {
                val tagList = listOf(it.child("tag").child("category").value.toString()) +
                        it.child("tag").child("member").value.toString().split(",")
                val filterNotNullTagList = tagList.filter { tagLabel ->
                    tagLabel != "null"
                }

                val tagGroup = it.child("tag").child("group").value.toString()

                /**
                 * 評価非表示：０
                 * 評価表示：そのまま表示
                 */
                val likeCount = when (it.child("likeCount").value.toString()) {
                    "None" -> 0
                    else -> it.child("likeCount").value.toString().toInt()
                }

                val singleItem = ArchiveItem(
                    it.key.toString(),
                    it.child("title").value.toString(),
                    it.child("thumbnailUrl").value.toString(),
                    it.child("publishedAt").value.toString(),
                    it.child("viewCount").value.toString().toInt(),
                    likeCount,
                    it.child("channelName").value.toString(),
                    it.child("channelIconUrl").value.toString(),
                    it.child("duration").value.toString(),
                    filterNotNullTagList,
                    tagGroup
                )

                archiveItem.add(singleItem)
            }
            archiveItem.sortByDescending { it.publishedAt }
            trySend(Resource.Success(archiveItem))
        }.addOnFailureListener {
            trySend(Resource.Error("${it.message}"))
        }
        awaitClose { }
    }
}