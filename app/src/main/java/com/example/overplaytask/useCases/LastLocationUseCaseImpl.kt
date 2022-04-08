package com.example.overplaytask.useCases

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

interface LastLocationUseCase {
    fun execute(): Location?
    fun initialize()
    fun getLoactionUodates() : Flow<Boolean>
}

class LastLocationUseCaseImpl @Inject constructor(
    private val context: Context,
    private val calculateDistanceUseCase: CalculateDistanceUseCase
) : LastLocationUseCase {

    private var lastLocation: Location? = null
    private var initialLocation: Location? = null
    private val distanceFlow = MutableSharedFlow<Boolean>()

    private var locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.let {
                lastLocation = it.lastLocation
                sendDistance()
            }
        }
    }

    private fun sendDistance() {

        GlobalScope.launch {
            lastLocation?: return@launch
            initialLocation?: return@launch
            val distance = calculateDistanceUseCase.getDistanceInKm(initialLocation!!.latitude,initialLocation!!.longitude
                , lastLocation!!.latitude,lastLocation!!.longitude)
            if (distance > 10.0){
                distanceFlow.emit(true)
                initialLocation = lastLocation
            }
            delay(1000)
            distanceFlow.emit(false)

        }
    }

    var request: LocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = (3 * 1000).toLong()
    }

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override fun initialize() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        if (!isLocationEnabled()) return
        fusedLocationClient.removeLocationUpdates(locationCallback)
        fusedLocationClient.requestLocationUpdates(request, locationCallback, null)
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { location ->
                    initialLocation = location
                }
            }
        }
    }

    override fun getLoactionUodates(): Flow<Boolean> {
        return distanceFlow
    }

    private fun isLocationPermissionGranted(): Boolean {
        return !(
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            )
    }

    private fun isLocationEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            val mode: Int = Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }


    override fun execute(): Location? {
        return lastLocation
    }
}
