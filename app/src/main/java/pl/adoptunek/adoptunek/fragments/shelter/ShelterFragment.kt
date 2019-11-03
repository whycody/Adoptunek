package pl.adoptunek.adoptunek.fragments.shelter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.Shelter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class ShelterFragment : Fragment(), OnMapReadyCallback, ShelterContract.ShelterView {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val presenter = ShelterPresenterImpl(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_shelter, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.shelterMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        presenter.onCreate()
        return view
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!
        val zoomLevel = 12.0f
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if(location!=null){
                val latitude = location.latitude
                val longitude = location.longitude
                val lastLocationLatLng = LatLng(latitude, longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocationLatLng, zoomLevel))
            }else moveMapToOlsztyn(zoomLevel)
        }.addOnFailureListener{ moveMapToOlsztyn(zoomLevel) }
        googleMap.isMyLocationEnabled = true
    }

    private fun moveMapToOlsztyn(zoomLevel: Float){
        val olsztyn = LatLng(53.775711, 20.477980)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(olsztyn, zoomLevel))
    }

    override fun addShelterToMap(shelter: Shelter) {
        val latLng = LatLng(shelter.location_latitude!!, shelter.location_longitude!!)
        val shelterMarker = googleMap.addMarker(MarkerOptions().position(latLng))
        shelterMarker.tag = shelter.id
        shelterMarker.title = shelter.name
        val bitmapdraw = activity!!.getDrawable(R.drawable.ic_shelter_marker) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, 63, 100, false)
        shelterMarker.setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker))
    }

    override fun sheltersLoadingFailed() {
        Toast.makeText(activity, "Nie udało załadować się schronisk", Toast.LENGTH_SHORT).show()
    }
}
