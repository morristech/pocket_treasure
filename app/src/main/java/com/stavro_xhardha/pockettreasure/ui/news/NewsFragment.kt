package com.stavro_xhardha.pockettreasure.ui.news


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.*
import kotlinx.android.synthetic.main.fragment_news.*
import javax.inject.Inject


class NewsFragment : BaseFragment(), NewsAdapterContract {

    @Inject
    lateinit var newsViewModelFactory: NewsViewModelFactory

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun initializeComponents() {
        newsAdapter = NewsAdapter(this)
    }

    override fun initViewModel() {
        newsViewModel = ViewModelProviders.of(this, newsViewModelFactory).get(NewsViewModel::class.java)
    }

    override fun performDi() {
        DaggerNewsComponent.builder().pocketTreasureComponent(PocketTreasureApplication.getPocketTreasureComponent())
            .build().inject(this)
    }

    override fun observeTheLiveData() {
        newsViewModel.networkCurrentNetworkStatus.observe(this, Observer {
            if (isDebugMode) {
                when (it) {
                    //todo fix handle the errors on the view
                    CurrentNetworkStatus.FAILED -> Log.d(APPLICATION_TAG, "FAILED")
                    CurrentNetworkStatus.SUCCESS -> Log.d(APPLICATION_TAG, "SUCCESS")
                    CurrentNetworkStatus.LOADING -> Log.d(APPLICATION_TAG, "LOADING")
                    else -> Log.d(APPLICATION_TAG, "TOTAL ERROR")
                }
            }
        })
        newsViewModel.primaryNetworkStatus.observe(this, Observer {
            when (it) {
                InitialNetworkState.ERROR -> {
                    llError.visibility = View.VISIBLE
                    rvNews.visibility = View.GONE
                    pbNews.visibility = View.GONE
                    if (isDebugMode)
                        Log.d(APPLICATION_TAG, "INITIAL_FAILED")
                }
                InitialNetworkState.SUCCESS -> {
                    llError.visibility = View.GONE
                    rvNews.visibility = View.VISIBLE
                    pbNews.visibility = View.GONE
                    if (isDebugMode)
                        Log.d(APPLICATION_TAG, "INITIAL_SUCCESS")
                }
                InitialNetworkState.LOADING -> {
                    llError.visibility = View.GONE
                    rvNews.visibility = View.GONE
                    pbNews.visibility = View.VISIBLE
                    if (isDebugMode)
                        Log.d(APPLICATION_TAG, "INITIAL_LOADING")
                }
                else -> Log.d(APPLICATION_TAG, "TOTAL ERROR")
            }
        })

        newsViewModel.getNewsPost().observe(this, Observer {
            newsAdapter.submitList(it)
            rvNews.adapter = newsAdapter
        })
    }

    override fun handleOnBackPressed(view: View) {
        getBackToHomeFragment(view, requireActivity(), this)
    }

    override fun onCurrentNewsClick(url: String) {
        if (isDebugMode)
            Log.d(APPLICATION_TAG, url)
        if (url.isNotEmpty()) {
            val action = NewsFragmentDirections.actionNewsFragmentToArticleWebViewFragment(url)
            findNavController().navigate(action)
        } else {
            Toast.makeText(activity, "Url is empty", Toast.LENGTH_LONG).show()
        }
    }

    override fun onShareClick(url: String, title: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(Intent.createChooser(sharingIntent, activity!!.resources.getString(R.string.share_via)))
    }

    override fun showErrorMessage() {
        Toast.makeText(activity, "Error Loading Data", Toast.LENGTH_LONG).show() //todo please refactor this cases
    }
}