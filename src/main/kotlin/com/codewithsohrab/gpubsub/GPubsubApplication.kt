package com.codewithsohrab.gpubsub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GPubsubApplication

fun main(args: Array<String>) {
	runApplication<GPubsubApplication>(*args)
}
