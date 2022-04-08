package com.example.overplaytask.useCases

import javax.inject.Inject
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

interface CalculateDistanceUseCase {
    fun getDistanceInKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double
}

class CalculateDistanceUseCaseImpl @Inject constructor() : CalculateDistanceUseCase {

    override fun getDistanceInKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val theta = lon1 - lon2
        var dist = (
            sin(deg2rad(lat1)) *
                sin(deg2rad(lat2)) +
                (
                    cos(deg2rad(lat1)) *
                        cos(deg2rad(lat2)) *
                        cos(deg2rad(theta))
                    )
            )
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return (dist * 1.609344) * 1000
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}
