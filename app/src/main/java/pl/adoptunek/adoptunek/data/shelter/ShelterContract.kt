package pl.adoptunek.adoptunek.data.shelter

import pl.adoptunek.adoptunek.Shelter

interface ShelterContract {

    interface ShelterInterractor {

        fun listWithSheltersIsReady(successfully: Boolean = true, sheltersList: List<Shelter>? = null)

        fun shelterIsReady(successfully: Boolean = true, shelter: Shelter? = null)

    }

    interface ShelterDao {

        fun getAllShelters()

    }
}