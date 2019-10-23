package pl.adoptunek.adoptunek.fragments.home

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post

import pl.adoptunek.adoptunek.data.pet.PetContract
import pl.adoptunek.adoptunek.data.pet.PetDaoImpl
import pl.adoptunek.adoptunek.data.post.PostDaoImpl
import pl.adoptunek.adoptunek.data.post.PostInterractor
import pl.adoptunek.adoptunek.fragments.home.post.recycler.PostRecyclerAdapter
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kotlinx.android.synthetic.main.fragment_home.view.*
import pl.adoptunek.adoptunek.R

class HomeFragment : Fragment(), PostInterractor, PetContract.PetObjectsInterractor {

    private lateinit var presenter: HomeContract.HomePresenter
    private lateinit var adapter: PostRecyclerAdapter
    private lateinit var recycler: RecyclerView
    private val postDao = PostDaoImpl(this)
    private val petDao = PetDaoImpl(this)
    private lateinit var firstPet: ImageView
    private lateinit var secondPet: ImageView
    private lateinit var thirdPet: ImageView
    private lateinit var firstPetName: TextView
    private lateinit var secondPetName: TextView
    private lateinit var thirdPetName: TextView
    private lateinit var petOfWeekView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recycler = view.postsRecycle
        firstPet = view.findViewById(R.id.firstPet)
        secondPet = view.findViewById(R.id.secondPet)
        thirdPet = view.findViewById(R.id.thirdPet)
        firstPetName = view.findViewById(R.id.firstPetName)
        secondPetName = view.findViewById(R.id.secondPetName)
        thirdPetName = view.findViewById(R.id.thirdPetName)
        petOfWeekView = view.findViewById(R.id.petOfWeekView)
        loadDrawableToImage(context!!.getDrawable(R.drawable.dog_of_week)!!, petOfWeekView)
        loadDrawableToImage(context!!.getDrawable(R.drawable.dog_default_black)!!, firstPet)
        loadDrawableToImage(context!!.getDrawable(R.drawable.dog_default_black)!!, secondPet)
        loadDrawableToImage(context!!.getDrawable(R.drawable.dog_default_black)!!, thirdPet)
        view.postsRecycle.layoutManager = LinearLayoutManager(activity)
        postDao.getPosts(10)
        petDao.getPetsOfWeek()
        return view
    }

    override fun listOfPostsIsReady(list: List<Post>) {
        presenter = HomePresenterImpl(list, context!!)
        adapter = PostRecyclerAdapter(presenter, activity!!)
        recycler.adapter = adapter
    }

    override fun listWithPetsIsReady(successfully: Boolean, petList: List<Pet>?) {
        if(successfully) loadPetsOfWeek(petList!!)
    }

    override fun petDocumentIsReady(successfully: Boolean, pet: Pet?) {

    }

    private fun loadPetsOfWeek(petlist: List<Pet>){
        loadUriToImage(Uri.parse(petlist[0].profile_image_uri!!), firstPet)
        loadUriToImage(Uri.parse(petlist[1].profile_image_uri!!), secondPet)
        loadUriToImage(Uri.parse(petlist[2].profile_image_uri!!), thirdPet)
        setupText(petlist)
    }

    private fun setupText(petlist: List<Pet>){
        firstPetName.text = petlist[0].name
        secondPetName.text = petlist[1].name
        thirdPetName.text = petlist[2].name
    }

    private fun loadDrawableToImage(drawable: Drawable, view: ImageView){
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(16))
        Glide.with(activity!!)
            .load(drawable)
            .apply(requestOptions)
            .transition(withCrossFade())
            .into(view)
    }

    private fun loadUriToImage(uri: Uri, view: ImageView){
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(16))
        Glide.with(activity!!)
            .load(uri)
            .apply(requestOptions)
            .transition(withCrossFade())
            .into(view)
    }

    companion object{
        val PET = "pet"
    }
}
