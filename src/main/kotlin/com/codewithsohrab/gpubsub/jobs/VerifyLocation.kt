package com.codewithsohrab.gpubsub.jobs

import com.codewithsohrab.gpubsub.services.LocationService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Configuration
@EnableScheduling
@Service
class VerifyLocation(private val locationService: LocationService) {
    private val logger = LoggerFactory.getLogger(VerifyLocation::class.java)

    /**
     * This method is used to check for those locations
     * which could get verified every 20 seconds
     */
    @Scheduled(fixedDelay = 20000)
    fun verifyLocations() {
        logger.info("checking for unverified locations")
        val ulocations = locationService.getUnverifiedLocations()
        if (ulocations.isNotEmpty()) {
            // loop through locations and see they can get verified
            // if a location has an address then we set to verified
            ulocations.forEach {
                if(it.address != null) {
                    locationService.verifyLocation(it.id)
                }
            }
        }
    }
}