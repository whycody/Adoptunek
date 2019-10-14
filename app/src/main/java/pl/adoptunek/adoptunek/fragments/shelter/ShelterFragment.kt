package pl.adoptunek.adoptunek.fragments.shelter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

import pl.adoptunek.adoptunek.R

class ShelterFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shelter, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.shelterMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!
        val olsztyn = LatLng(53.775711, 20.477980)
        val zoomLevel = 12.0f
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(olsztyn, zoomLevel))
    }
}
