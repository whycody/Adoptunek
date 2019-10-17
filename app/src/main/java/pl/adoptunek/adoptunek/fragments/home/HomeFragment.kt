package pl.adoptunek.adoptunek.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.view.*
import pl.adoptunek.adoptunek.Post

import pl.adoptunek.adoptunek.R
import pl.adoptunek.adoptunek.data.PostDaoImpl
import pl.adoptunek.adoptunek.data.PostInterractor
import pl.adoptunek.adoptunek.fragments.home.post.recycler.PostRecyclerAdapter

class HomeFragment : Fragment(), PostInterractor {
    private lateinit var presenter: HomeContract.HomePresenter
    private lateinit var adapter: PostRecyclerAdapter
    private lateinit var recycler: RecyclerView
    private val postDao = PostDaoImpl(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recycler = view.postsRecycle
        postDao.getPosts(10)
        view.postsRecycle.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun listIsReady(list: List<Post>) {
        Log.d("MOJTAG", "Lista gotowa")
        presenter = HomePresenterImpl(list)
        adapter = PostRecyclerAdapter(presenter, activity!!)
        recycler.adapter = adapter
    }

}
