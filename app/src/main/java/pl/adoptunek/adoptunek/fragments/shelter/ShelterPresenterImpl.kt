package pl.adoptunek.adoptunek.fragments.shelter

import pl.adoptunek.adoptunek.Shelter
import pl.adoptunek.adoptunek.data.shelter.ShelterDaoImpl

class ShelterPresenterImpl(val view: ShelterContract.ShelterView): ShelterContract.ShelterPresenter,
    pl.adoptunek.adoptunek.data.shelter.ShelterContract.ShelterInterractor {

    private val shelterDao = ShelterDaoImpl(this)

    override fun onCreate() {
        shelterDao.getAllShelters()
    }

    override fun listWithSheltersIsReady(successfully: Boolean, sheltersList: List<Shelter>?) {
        if(!successfully) view.sheltersLoadingFailed()
    }

    override fun shelterIsReady(successfully: Boolean, shelter: Shelter?) {
        if(successfully&&shelter!!.location_latitude!=null&&shelter.location_longitude!=null)
            view.addShelterToMap(shelter)
    }

}