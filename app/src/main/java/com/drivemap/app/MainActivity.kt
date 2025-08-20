package com.drivemap.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drivemap.app.databinding.ActivityMainBinding
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.Style

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding hasil dari res/layout/activity_main.xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lifecycle MapView
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { map ->
            map.uiSettings.apply {
                isAttributionEnabled = true
                isCompassEnabled = true
                isZoomControlsEnabled = false
            }

            // Posisi awal kamera (contoh: Jakarta)
            map.cameraPosition = CameraPosition.Builder()
                .target(LatLng(-6.1754, 106.8272))
                .zoom(9.0)
                .build()

            // Style sementara (demo). Nanti diganti ke style/tiles VPS sendiri
            map.setStyle(Style.Builder().fromUri("https://demotiles.maplibre.org/style.json")) {
                // TODO: tambah sources/layers heatmap & marker
            }
        }
    }

    override fun onStart() { super.onStart(); binding.mapView.onStart() }
    override fun onResume() { super.onResume(); binding.mapView.onResume() }
    override fun onPause() { binding.mapView.onPause(); super.onPause() }
    override fun onStop() { binding.mapView.onStop(); super.onStop() }
    override fun onLowMemory() { super.onLowMemory(); binding.mapView.onLowMemory() }
    override fun onDestroy() { binding.mapView.onDestroy(); super.onDestroy() }
}
