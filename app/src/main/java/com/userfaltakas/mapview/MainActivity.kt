package com.userfaltakas.mapview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.userfaltakas.mapview.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ovh.plrapps.mapview.MapView
import ovh.plrapps.mapview.MapViewConfiguration
import ovh.plrapps.mapview.api.addMarker
import ovh.plrapps.mapview.api.removeMarker
import ovh.plrapps.mapview.api.setMarkerTapListener
import ovh.plrapps.mapview.core.TileStreamProvider
import ovh.plrapps.mapview.markers.MarkerTapListener

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
                view.context.assets?.open("tiles/esp/map1.jpg")
            } catch (e: Exception) {
                null
            }
        }
        val tileSize = 500
        val config = MapViewConfiguration(
            1, 500, 291, tileSize, tileStreamProvider
        ).setMaxScale(3f).setMinScale(2f)


        binding.mapView.configure(config)
        binding.mapView.scale = 2f


        initMarkers()

        CoroutineScope(Dispatchers.Default).launch {

            var flag = true
            while (true) {
                withContext(Dispatchers.Main) {
                    if (binding.mapView.scale > 2.5f && flag) {
                        binding.mapView.apply {
                            removeMarker(view.findViewWithTag("#3"))
                            removeMarker(view.findViewWithTag("#2"))
                            removeMarker(view.findViewWithTag("#1"))

                            defineBounds(0.0, 0.0, 1.0, 1.0)
                            addNewMarker(0.8, 0.6, "#1")
                            addNewMarker(0.2, 0.3, "#2")
                            addNewMarker(0.9, 0.1, "#3")
                        }
                        flag = false
                    } else if (binding.mapView.scale <= 2.5f && !flag) {
                        binding.mapView.apply {
                            removeMarker(view.findViewWithTag("#3"))
                            removeMarker(view.findViewWithTag("#2"))
                            removeMarker(view.findViewWithTag("#1"))

                            defineBounds(0.0, 0.0, 1.0, 1.0)
                            addNewMarker(0.5, 0.5, "#1")
                            addNewMarker(0.4, 0.3, "#2")
                            addNewMarker(0.6, 0.4, "#3")
                        }
                        flag = true
                    }
                }
            }
        }

        binding.mapView.setMarkerTapListener(object : MarkerTapListener {
            override fun onMarkerTap(view: View, x: Int, y: Int) {
                if (view is MapMarker) {
                    Toast.makeText(this@MainActivity, view.tag.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun initMarkers() {

        binding.mapView.apply {
            defineBounds(0.0, 0.0, 1.0, 1.0)

            addNewMarker(0.5, 0.5, "#1")
            addNewMarker(0.4, 0.3, "#2")
            addNewMarker(0.6, 0.4, "#3")
        }
    }

    private fun MapView.addNewMarker(x: Double, y: Double, name: String) {
        val marker = MapMarker(context, x, y, name).apply {
            setImageResource(R.drawable.marker)
            tag = name
        }
        addMarker(marker, x, y)
    }
}





