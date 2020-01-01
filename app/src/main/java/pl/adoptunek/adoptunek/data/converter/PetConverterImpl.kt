package pl.adoptunek.adoptunek.data.converter

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post
import pl.adoptunek.adoptunek.Shelter
import pl.adoptunek.adoptunek.data.post.PostDaoImpl
import pl.adoptunek.adoptunek.fragments.home.time.helper.TimeHelperImpl

class PetConverterImpl(val interractor: PetConverterInterractor? = null): PetConverter {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.reference
    private val timeHelper = TimeHelperImpl()

    override fun getPostFromPetID(id: String, fullData: Boolean, withPhotos: Boolean) {
        firestore.collection("animals").document(id).get().addOnCompleteListener{ task ->
            if(task.isSuccessful) {
                val pet = task.result!!.toObject(Pet::class.java)
                pet!!.id = task.result!!.id
                getPostFromPetObject(pet, fullData, withPhotos)
            }
        }
    }

    override fun getPostFromPetObject(pet: Pet, fullData: Boolean, withPhotos: Boolean) {
        val post = Post()
        post.pet = pet
        post.idOfAnimal = pet.id
        loadPetInfoToPostObject(post, pet)
        loadDataOfPetToPost(pet, post, fullData)
        getShelter(pet, post, withPhotos)
    }

    override fun addBasicDataToPost(post: Post) {
        post.dataOfAnimal = mutableListOf()
        addBasicDataOfPet(post.pet!!, post.dataOfAnimal!! as MutableList<Pair<String, String>>)
    }

    override fun addFullDataToPost(post: Post) {
        addFullDataOfPet(post.pet!!, post.dataOfAnimal!! as MutableList<Pair<String, String>>)
    }

    private fun loadPetInfoToPostObject(post: Post, pet: Pet){
        post.idOfShelter = pet.shelter
        post.petName = pet.name
        post.timeAgo = timeHelper.howLongAgo(pet.add_date!!,
            TimeHelperImpl.POST_HOW_LONG_AGO)
    }

    private fun loadDataOfPetToPost(pet: Pet, post: Post, fullData: Boolean){
        val dataOfAnimalList = mutableListOf<Pair<String, String>>()
        addBasicDataOfPet(pet, dataOfAnimalList)
        if(fullData) addFullDataOfPet(pet, dataOfAnimalList)
        post.dataOfAnimal = dataOfAnimalList
    }

    private fun addBasicDataOfPet(pet: Pet, dataOfAnimalList: MutableList<Pair<String, String>>){
        if(pet.name!=null) dataOfAnimalList.add(Pair("Imię", pet.name))
        if(pet.sex!=null) dataOfAnimalList.add(Pair("Płeć", pet.sex))
        if(pet.birth_date!=null) dataOfAnimalList.add(Pair("Wiek",
            timeHelper.howLongAgo(pet.birth_date!!, TimeHelperImpl.PET_HOW_LONG_IS_WAITING)))
    }

    private fun addFullDataOfPet(pet: Pet, dataOfAnimalList: MutableList<Pair<String, String>>){
        if(pet.in_shelter!=null) dataOfAnimalList.add(Pair("Czeka", timeHelper.howLongAgo(pet.birth_date!!,
            TimeHelperImpl.PET_HOW_LONG_IS_WAITING)))
        if(pet.siblings!=null) dataOfAnimalList.add(Pair("Rodzeństwo", pet.siblings!!))
        if(pet.full_health!=null) dataOfAnimalList.add(Pair("W pełni zdrowia", pet.full_health!!))
        if(pet.character!=null) dataOfAnimalList.add(Pair("Charakter", pet.character!!))
        if(pet.breed!=null) dataOfAnimalList.add(Pair("Rasa", pet.breed))
        if(pet.coat!=null) dataOfAnimalList.add(Pair("Umaszczenie", pet.coat!!))
    }

    private fun getShelter(pet: Pet, post: Post, withPhotos: Boolean){
        firestore.collection("shelters").document(pet.shelter!!).get()
            .addOnCompleteListener{ res ->
                if(res.isSuccessful){
                    val shelter = res.result!!.toObject(Shelter::class.java)
                    post.shelterName = "Schronisko \"${shelter!!.name}\""
                }
                gotShelter(post, res.isSuccessful, withPhotos)
            }
    }

    private fun gotShelter(post: Post, successfull: Boolean, withPhotos: Boolean){
        if(successfull){
            if(withPhotos) getPetUri(post)
        }
        if(!withPhotos || !successfull) interractor?.convertedIntoPostObject(post, successfull)
    }

    private fun getPetUri(post: Post){
        val petPath = "animals_photos/${post.idOfAnimal}/profile.jpg"
        storageReference.child(petPath).downloadUrl.addOnSuccessListener{ petImage ->
            post.petUri = petImage.toString()
            getShelterUri(post)
        }
    }

    private fun getShelterUri(post: Post){
        val shelterPath = "shelter/${post.idOfShelter}/profile.png"
        storageReference.child(shelterPath).downloadUrl.addOnCompleteListener{ task ->
            if(!task.isSuccessful) getDefaultShelterUri(post)
            else{
                post.shelterUri = task.result!!.toString()
                interractor?.convertedIntoPostObject(post, task.isSuccessful)
            }
        }
    }

    private fun getDefaultShelterUri(post: Post){
        storageReference.child(PostDaoImpl.DEFAULT_SHELTER_PATH)
            .downloadUrl.addOnCompleteListener{ task ->
            if(task.isSuccessful) post.shelterUri = task.result!!.toString()
            interractor?.convertedIntoPostObject(post, task.isSuccessful)
        }
    }
}