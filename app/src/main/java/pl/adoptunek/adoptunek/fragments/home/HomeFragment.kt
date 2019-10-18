package pl.adoptunek.adoptunek.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_home.view.*
import pl.adoptunek.adoptunek.Pet
import pl.adoptunek.adoptunek.Post

import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.data.pet.PetContract
import pl.adoptunek.adoptunek.data.pet.PetDaoImpl
import pl.adoptunek.adoptunek.data.post.PostDaoImpl
import pl.adoptunek.adoptunek.data.post.PostInterractor
import pl.adoptunek.adoptunek.fragments.home.post.recycler.PostRecyclerAdapter

class HomeFragment : Fragment(), PostInterractor, PetContract.PetInterractor {

    private lateinit var presenter: HomeContract.HomePresenter
    private lateinit var adapter: PostRecyclerAdapter
    private lateinit var recycler: RecyclerView
    private val postDao = PostDaoImpl(this)
    private val petDao = PetDaoImpl(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recycler = view.postsRecycle
        postDao.getPosts(10)
        petDao.getPetsOfWeek()
        view.postsRecycle.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun listOfPostsIsReady(list: List<Post>) {
        presenter = HomePresenterImpl(list)
        adapter = PostRecyclerAdapter(presenter, activity!!)
        recycler.adapter = adapter
    }

    override fun listWithPetsIsReady(successfully: Boolean, petList: List<Pet>?) {
        if(successfully) loadPetsOfWeek(petList!!)
    }

    private fun loadPetsOfWeek(petlist: List<Pet>){
        Glide.with(activity!!).load(petlist[0].profile_image).into(view!!.findViewById(R.id.firstPet))
        Glide.with(activity!!).load(petlist[1].profile_image).into(view!!.findViewById(R.id.secondPet))
        Glide.with(activity!!).load(petlist[2].profile_image).into(view!!.findViewById(R.id.thirdPet))
    }
}
