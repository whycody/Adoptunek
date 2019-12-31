package pl.adoptunek.adoptunek.fragments.home

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, PostInterractor, PetContract.PetOfWeekInterractor {

    private lateinit var presenter: HomeContract.HomePresenter
    private lateinit var adapter: PostRecyclerAdapter
    private lateinit var nestedScroll: NestedScrollView
    private lateinit var recycler: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var refreshLayout: SwipeRefreshLayout
    private val postDao = PostDaoImpl(this)
    private val petDao = PetDaoImpl(null, this, null)
    private var petsOfWeekList: List<Pet>? = null
    private lateinit var firstPet: ImageView
    private lateinit var secondPet: ImageView
    private lateinit var thirdPet: ImageView
    private lateinit var firstPetNameView: TextView
    private lateinit var secondPetNameView: TextView
    private lateinit var thirdPetNameView: TextView
    private lateinit var petOfWeekView: ImageView
    private var isLoading = false
    private var endOfPosts = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recycler = view.postsRecycle
        nestedScroll = view.postNested
        loadingProgressBar = view.loadingProgress
        firstPet = view.findViewById(R.id.firstPet)
        secondPet = view.findViewById(R.id.secondPet)
        thirdPet = view.findViewById(R.id.thirdPet)
        firstPetNameView = view.findViewById(R.id.firstPetName)
        secondPetNameView = view.findViewById(R.id.secondPetName)
        thirdPetNameView = view.findViewById(R.id.thirdPetName)
        petOfWeekView = view.findViewById(R.id.petOfWeekView)
        refreshLayout = view.findViewById(R.id.refreshLayout)
        refreshLayout.setOnRefreshListener(this)
        view.postsRecycle.layoutManager = LinearLayoutManager(activity)
        addScrollListenerToRecycler()
        loadPetOfWeekImages()
        postDao.getPosts()
        petDao.getPetsOfWeek()
        return view
    }

    private fun addScrollListenerToRecycler(){
        val nestedListener =
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if(!v.canScrollVertically(1) && !isLoading && !endOfPosts){
                    isLoading = true
                    postDao.loadMorePosts()
                    loadingProgressBar.visibility = View.VISIBLE
                }
            }
        nestedScroll.setOnScrollChangeListener(nestedListener)
    }

    private fun loadPetOfWeekImages(){
        loadDrawableToImage(context!!.getDrawable(R.drawable.dog_of_week)!!, petOfWeekView)
        loadDrawableToImage(context!!.getDrawable(R.drawable.dog_default_black)!!, firstPet)
        loadDrawableToImage(context!!.getDrawable(R.drawable.dog_default_black)!!, secondPet)
        loadDrawableToImage(context!!.getDrawable(R.drawable.dog_default_black)!!, thirdPet)
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

    override fun onRefresh() {
        postDao.getPosts(true)
    }

    override fun listOfPostsIsReady(list: List<Post>) {
        if(refreshLayout.isRefreshing){
            refreshLayout.isRefreshing = false
            endOfPosts = false
        }
        loadingProgressBar.visibility = View.GONE
        presenter = HomePresenterImpl(list, context!!)
        adapter = PostRecyclerAdapter(presenter, activity!!)
        recycler.adapter = adapter
    }

    override fun listOfPostsIsUpdated(list: List<Post>, finishedLoading: Boolean) {
        presenter.refreshListOfPosts(list)
        adapter.notifyItemInserted(list.size-1)
        isLoading = !finishedLoading
        if(finishedLoading) loadingProgressBar.visibility = View.INVISIBLE
    }

    override fun endListOfPosts() {
        endOfPosts = true
        loadingProgressBar.visibility = View.INVISIBLE
    }

    override fun listWithWeekPetsIsReady(successfully: Boolean, petList: List<Pet>?) {
        if(successfully) loadPetsOfWeek(petList!!)
    }

    private fun loadPetsOfWeek(petsOfWeekList: List<Pet>){
        loadUriToImage(Uri.parse(petsOfWeekList[0].profile_image_uri!!), firstPet)
        loadUriToImage(Uri.parse(petsOfWeekList[1].profile_image_uri!!), secondPet)
        loadUriToImage(Uri.parse(petsOfWeekList[2].profile_image_uri!!), thirdPet)
        setupTextOfWeekPets(petsOfWeekList)
        this.petsOfWeekList = petsOfWeekList
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

    private fun setupTextOfWeekPets(petlist: List<Pet>){
        firstPetNameView.text = petlist[0].name
        secondPetNameView.text = petlist[1].name
        thirdPetNameView.text = petlist[2].name
    }

    companion object{
        val PET = "pet"
    }
}
