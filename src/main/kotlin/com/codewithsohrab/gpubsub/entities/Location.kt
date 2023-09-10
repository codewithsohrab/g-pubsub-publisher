package com.codewithsohrab.gpubsub.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class Location(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var name: String,
    var deleted: Boolean? = false,
    var verified: Boolean? = false,
    @Column(name="address_id")
    var addressId: Long?,
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="address_id", insertable = false, updatable = false)
    var address: Address?
){
    constructor(): this(0L, "", false, false,null, null)
}

