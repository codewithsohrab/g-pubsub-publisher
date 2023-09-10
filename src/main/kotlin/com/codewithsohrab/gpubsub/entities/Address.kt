package com.codewithsohrab.gpubsub.entities

import javax.persistence.*

@Entity
data class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @Column(name="apartment_nr")
    var apartmentNr: Int,
    var street: String,
    var city: String
) {
    constructor():this(0,0, "", "",)
}