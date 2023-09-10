package com.codewithsohrab.gpubsub.controller

import com.codewithsohrab.gpubsub.entities.Location
import com.codewithsohrab.gpubsub.models.LocationVM
import com.codewithsohrab.gpubsub.models.UpdateLocationVM
import com.codewithsohrab.gpubsub.services.LocationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/locations")
class LocationController(private val locationService: LocationService) {

    @GetMapping
    fun getLocations() : ResponseEntity<List<Location>> {
        return locationService.getLocations()
    }

    @PostMapping
    fun createLocation(@RequestBody location: LocationVM): ResponseEntity<Any> {
        return locationService.createLocation(location)
    }

    @PutMapping
    fun updateLocation(@RequestBody location: UpdateLocationVM): ResponseEntity<Any> {
        return locationService.updateLocation(location)
    }

    @DeleteMapping("{id}")
    fun deleteLocation(@PathVariable id: String): ResponseEntity<Any> {
        return locationService.deleteLocation(id.toLong())
    }
}