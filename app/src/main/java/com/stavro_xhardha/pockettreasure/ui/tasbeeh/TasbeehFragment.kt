package com.stavro_xhardha.pockettreasure.ui.tasbeeh


import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.stavro_xhardha.pockettreasure.BaseFragment
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.getBackToHomeFragment
import kotlinx.android.synthetic.main.fragment_tasbeeh.*

class TasbeehFragment : BaseFragment() {

    private lateinit var tasbeehViewModel: TasbeehViewModel
    private lateinit var adapter: TasbeehAdapter

    override fun initializeComponents() {
        adapter = TasbeehAdapter()
        rvTasbeeh.adapter = adapter
    }

    override fun initViewModel() {
        tasbeehViewModel = ViewModelProviders.of(this).get(TasbeehViewModel::class.java)
    }

    override fun performDi() {
    }

    override fun observeTheLiveData() {
        tasbeehViewModel.tasbeehList.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_tasbeeh, container, false)
    }

    override fun handleOnBackPressed(view: View) {
        getBackToHomeFragment(view, requireActivity(), this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.reset, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_refresh) {
            tasbeehViewModel.initList()
            adapter.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }
}