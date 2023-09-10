package com.codewithsohrab.gpubsub.models

data class LocationVM(
    var id: Long?,
    var name: String,
    var deleted: Boolean?,
    var verified: Boolean?,
    var addressId: Long?,
    var address: AddressVM?
)