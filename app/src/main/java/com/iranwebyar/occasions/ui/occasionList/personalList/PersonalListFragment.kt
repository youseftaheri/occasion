package com.iranwebyar.occasions.ui.occasionList.personalList

import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Context
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
import com.iranwebyar.occasions.data.model.OccasionsPOJO
import com.iranwebyar.occasions.data.model.db.Occasion
import com.iranwebyar.occasions.databinding.FragmentPersonalListBinding
import com.iranwebyar.occasions.ui.base.BaseFragment
import com.iranwebyar.occasions.ui.occasionList.OccasionListActivity
import com.iranwebyar.occasions.utils.AlarmUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalListFragment : BaseFragment<FragmentPersonalListBinding, PersonalListViewModel>(),
    PersonalListNavigator, PersonalListFragmentCallback {
    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.fragment_personal_list

    override fun getViewModelClass() = PersonalListViewModel::class.java

    var mFragmentPersonalListBinding: FragmentPersonalListBinding? = null
    var mLayoutManager: LinearLayoutManager? = null
    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentPersonalListBinding = viewDataBinding
        mLayoutManager = LinearLayoutManager(this.requireContext())
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        viewModel.fetchData
//        setCards()

//        viewModel.fetchData
//        mFragmentPersonalListBinding!!.swipeRefresh.setOnRefreshListener {
//            mFragmentPersonalListBinding!!.swipeRefresh.isRefreshing = false
//            viewModel.fetchData
//        }
    }

    override fun setCards() {
        mLayoutManager!!.orientation = LinearLayoutManager.VERTICAL
        mFragmentPersonalListBinding!!.recyclerView.layoutManager = mLayoutManager
        mFragmentPersonalListBinding!!.recyclerView.itemAnimator = DefaultItemAnimator()
        mFragmentPersonalListBinding!!.recyclerView.adapter = PersonalListAdapter(viewModel.getCardItemsLiveData().value, this)
        mFragmentPersonalListBinding!!.tvEmpty.visibility = if(viewModel.getCardItemsLiveData().value!!.isEmpty()) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun newInstance(): PersonalListFragment {
            val args = Bundle()
            val fragment = PersonalListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun editOccasion(occasion: OccasionsPOJO.Occasion) {
        (activity as? OccasionListActivity)!!.onEditClick(occasion)
    }

    override fun deleteCard(id: Long) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val alertDialog: AlertDialog
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val layoutView: View = inflater.inflate(R.layout.customized_allert, null)
        val title: TextView = layoutView.findViewById(R.id.title)
        val positiveButton: Button = layoutView.findViewById(R.id.btPositive)
        val negativeButton: Button = layoutView.findViewById(R.id.btNegative)
        title.text = getString(R.string.sureToDelete)
        positiveButton.text = getString(R.string.yes)
        negativeButton.text = getString(R.string.no)
        dialogBuilder.setView(layoutView)
        alertDialog = dialogBuilder.create()
        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(false)
        alertDialog.show()
        positiveButton.setOnClickListener(View.OnClickListener {
            run {
                viewModel.cancelAlarm(id)
                alertDialog.dismiss()
            } })
        negativeButton.setOnClickListener(View.OnClickListener { alertDialog.dismiss() })
    }

    override fun cancelAlarm(occasion: Occasion?) {
        AlarmUtil.cancelAlarm(
            requireContext(),
            occasion!!,
            alarmManager
        )
    }

    override fun successfulOccasionDelete() {
        setCards()
//        startNewActivity(OccasionListActivity::class.java, isFinishAll = false, isCurrentFinish = true)
    }
}
