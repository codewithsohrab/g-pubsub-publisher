package com.codewithsohrab.gpubsub.models

data class UpdateLocationVM(
    var id: Long,
    var name: String,
    var addressId: Long?
)