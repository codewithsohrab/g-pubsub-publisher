package com.codewithsohrab.gpubsub.models

data class EventPayload(
    val type: String,
    val content: Any
)