package pl.adoptunek.adoptunek.data.pet

import pl.adoptunek.adoptunek.Pet

interface PetContract {

    interface PetInterractor{

        fun listWithPetsIsReady(successfully: Boolean = true, petList: List<Pet>? = null)
    }

    interface PetDao{

        fun getPetsOfWeek()
    }
}