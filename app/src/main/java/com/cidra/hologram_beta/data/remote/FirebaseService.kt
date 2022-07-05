package com.cidra.hologram_beta.data.remote


import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object FirebaseReference {
    private val database = Firebase.database
    private val ref = database.getReference("/video")
}