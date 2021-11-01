package com.iranwebyar.occasions.ui.occasionList.birthdayList

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.iranwebyar.occasions.BR
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.databinding.FragmentBirthdayListBinding
import com.iranwebyar.occasions.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BirthdayListFragment : BaseFragment<FragmentBirthdayListBinding, BirthdayListViewModel>(), BirthdayListNavigator {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_birthday_list

    override fun getViewModelClass() = BirthdayListViewModel::class.java

    var mFragmentBirthdayListBinding: FragmentBirthdayListBinding? = null
    var mLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentBirthdayListBinding = viewDataBinding
        mLayoutManager = LinearLayoutManager(this.requireContext())
        setCards()
//        viewModel.fetchData
//        mFragmentBirthdayListBinding!!.swipeRefresh.setOnRefreshListener {
//            mFragmentBirthdayListBinding!!.swipeRefresh.isRefreshing = false
//            viewModel.fetchData
//        }
    }

    fun setCards() {
        mLayoutManager!!.orientation = LinearLayoutManager.VERTICAL
        mFragmentBirthdayListBinding!!.recyclerView.layoutManager = mLayoutManager
        mFragmentBirthdayListBinding!!.recyclerView.itemAnimator = DefaultItemAnimator()
        mFragmentBirthdayListBinding!!.recyclerView.adapter = BirthdayListAdapter(viewModel.getCardItemsLiveData().value)
    }

    companion object {
        fun newInstance(): BirthdayListFragment {
            val args = Bundle()
            val fragment = BirthdayListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
