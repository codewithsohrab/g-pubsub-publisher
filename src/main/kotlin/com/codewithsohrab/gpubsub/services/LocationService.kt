package com.codewithsohrab.gpubsub.services

import com.codewithsohrab.gpubsub.entities.Address
import com.codewithsohrab.gpubsub.entities.Location
import com.codewithsohrab.gpubsub.models.EventPayload
import com.codewithsohrab.gpubsub.models.LocationVM
import com.codewithsohrab.gpubsub.models.UpdateLocationVM
import com.google.gson.GsonBuilder
import org.slf4j.LoggerFactory
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Service
class LocationService(@PersistenceContext private val entityManager: EntityManager,
    private val pubSubTemplate: PubSubTemplate) {
    private val logger  =  LoggerFactory.getLogger(LocationService::class.java)


    fun getLocations() : ResponseEntity<List<Location>> {
        val data = entityManager.createQuery("select l from Location l where l.deleted=false", Location::class.java)
            .resultList
        logger.info("Listing locations")
        return ResponseEntity(data, HttpStatus.OK)
    }

    fun getUnverifiedLocations(): List<Location> {
        return entityManager.createQuery(
            "select l from Location l where l.deleted = false and l.verified = false",
            Location::class.java
        )
            .resultList
    }

    @Transactional
    fun createLocation(location: LocationVM) : ResponseEntity<Any> {
        val loc = Location()
        loc.name = location.name

        if(location.addressId != null &&
            location.address != null) {
            logger.error("You cannot pass a new address and old one together.")
            return ResponseEntity("you should pass either addressId or address", HttpStatus.BAD_REQUEST)
        } else {
            if (location.addressId != null) {
                loc.addressId = location.addressId
            } else if(location.address != null) {
                val addr = Address()
                addr.city = location.address?.city.toString()
                addr.apartmentNr = location.address?.apartmentNr!!
                addr.street = location.address?.street.toString()

                // save the address before creating the location
                entityManager.persist(addr)
                entityManager.flush()

                loc.address = addr
                loc.addressId = addr.id // set address_id in location entity from new generated address
            }
            entityManager.persist(loc)
            logger.info("Location created.")
            val eventPayload = EventPayload("NEW_LOCATION", GsonBuilder().create().toJson(loc))
            val serializedPayload = GsonBuilder().create().toJson(eventPayload)
            pubSubTemplate.publish("test-01", serializedPayload)
            return ResponseEntity(loc, HttpStatus.OK)
        }
    }

    @Transactional
    fun updateLocation(location: UpdateLocationVM) : ResponseEntity<Any> {
        val q = "select l from Location l where l.deleted = false and l.id =:id"
        val loc = entityManager.createQuery(q, Location::class.java)
            .setParameter("id", location.id)
            .resultList
        return if (loc.isNotEmpty()) {
            loc[0].name = location.name
            if(location.addressId != null)
                loc[0].addressId = location.addressId
            entityManager.merge(loc[0])
            logger.info("Location updated.")
            val eventPayload = EventPayload("UPDATED_LOCATION", GsonBuilder().create().toJson(loc[0]))
            val serializedPayload = GsonBuilder().create().toJson(eventPayload)
            pubSubTemplate.publish("test-01", serializedPayload)
            ResponseEntity(loc[0], HttpStatus.OK)
        } else {
            logger.error("Location not found!")
            ResponseEntity("Location not found!", HttpStatus.BAD_REQUEST)
        }
    }

    @Transactional
    fun verifyLocation(id: Long) : ResponseEntity<Any> {
        val loc = entityManager
            .createQuery("select l from Location l where l.deleted = false and l.id=:id", Location::class.java)
            .setParameter("id", id)
            .resultList
        return if (loc.isNotEmpty()) {
            loc[0].verified = true
            entityManager.merge(loc[0])
            logger.info("Location \"${loc[0].name}\" verified.")
            val eventPayload = EventPayload("VERIFIED_LOCATION", GsonBuilder().create().toJson(loc[0]))
            val serializedPayload = GsonBuilder().create().toJson(eventPayload)
            pubSubTemplate.publish("test-01", serializedPayload)
            ResponseEntity(loc[0], HttpStatus.OK)
        } else {
            logger.error("Location not found!")
            return ResponseEntity("Location not found!", HttpStatus.BAD_REQUEST)
        }
    }

    @Transactional
    fun deleteLocation(id: Long) : ResponseEntity<Any> {
        val loc = entityManager
            .createQuery("select l from Location l where l.deleted = false and l.id=:id", Location::class.java)
            .setParameter("id", id)
            .resultList
        return if (loc.isNotEmpty()) {
            loc[0].deleted = true
            entityManager.merge(loc[0])
            logger.info("Location deleted.")
            val eventPayload = EventPayload("DELETE_LOCATION", GsonBuilder().create().toJson(loc[0]))
            val serializedPayload = GsonBuilder().create().toJson(eventPayload)
            pubSubTemplate.publish("test-01", serializedPayload)
            ResponseEntity(loc[0], HttpStatus.OK)
        } else {
            logger.error("Location not found!")
            return ResponseEntity("Location not found!", HttpStatus.BAD_REQUEST)
        }
    }
}