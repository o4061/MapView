package com.userfaltakas.mapview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.userfaltakas.mapview.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ovh.plrapps.mapview.MapViewConfiguration
import ovh.plrapps.mapview.core.TileStreamProvider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rootView = window.decorView.rootView
        configureMapView(rootView)
    }

    private fun configureMapView(view: View) {
        val tileStreamProvider = TileStreamProvider { row, col, zoomLvl ->
            try {
                view.context.assets?.open("tiles/esp/map_gr.png")
            } catch (e: Exception) {
                null
            }
        }
        val tileSize = 3284
        val config = MapViewConfiguration(
            1, 3284, 1522, tileSize, tileStreamProvider
        ).setMaxScale(3f).setMinScale(1f)

        binding.mapView.configure(config)
        binding.mapView.scale = 2f



        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                Log.d("scale", binding.mapView.scale.toString())
            }
        }
    }
}


