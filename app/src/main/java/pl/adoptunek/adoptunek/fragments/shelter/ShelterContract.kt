package pl.adoptunek.adoptunek.fragments.shelter

import pl.adoptunek.adoptunek.Shelter

interface ShelterContract {

    interface ShelterView {

        fun addShelterToMap(shelter: Shelter)

        fun sheltersLoadingFailed()

    }

    interface ShelterPresenter {

        fun onCreate()

    }
}