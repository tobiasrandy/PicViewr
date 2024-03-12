package com.app.picviewr.fragment

import Photo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.picviewr.module.HomeActivity
import com.app.picviewr.adapter.PhotoAdapter
import com.app.picviewr.api.ApiService
import com.app.picviewr.databinding.FragmentHomeBinding
import com.app.picviewr.presenter.HomePresenter
import com.app.picviewr.util.EndlessRecyclerViewListener
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.adapter.rxjava2.HttpException
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var presenter : HomePresenter

    private lateinit var adapter: PhotoAdapter
    private lateinit var scrollListener: EndlessRecyclerViewListener
    private var photoList: ArrayList<Photo> = ArrayList()

    @Inject
    lateinit var apiService: ApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        presenter = HomePresenter(this, apiService)

        adapter = PhotoAdapter(requireContext(), photoList)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        initGridLayoutManager(binding.rvPictures, adapter, staggeredGridLayoutManager)

        scrollListener = object : EndlessRecyclerViewListener(staggeredGridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                presenter.getPhotoList(page + 1 )
            }
        }

        binding.rvPictures.addOnScrollListener(scrollListener)
        binding.swipeRefresh.setOnRefreshListener {
            presenter.getPhotoList(1)
        }

        presenter.getPhotoList(1)

        return binding.root
    }

    private fun initGridLayoutManager(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>?,
        gridLayoutManager: StaggeredGridLayoutManager?
    ) {
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    fun updateView(list: ArrayList<Photo>, page: Int) {
        if (page == 1) {
            scrollListener.resetState()
            photoList.clear()
        }
        photoList.addAll(list)
        adapter.notifyDataSetChanged()

        showEmptyState(photoList.isEmpty())
        binding.swipeRefresh.isRefreshing = false
    }

    fun handleError(t: Throwable) {
        binding.swipeRefresh.isRefreshing = false
        val errorMessage = when (t) {
            is IOException -> "Network error. Please check your internet connection and try again."
            is HttpException -> "An unexpected error occurred. Please try again."
            else -> t.localizedMessage
        }

        showEmptyState(photoList.isEmpty())
        (activity as HomeActivity).showSnackbar(binding.root, errorMessage, true)
    }

    private fun showEmptyState(isEmpty: Boolean) {
        binding.tvEmpty.visibility = if(isEmpty) View.VISIBLE else View.GONE
        binding.rvPictures.visibility = if(isEmpty) View.GONE else View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }
}