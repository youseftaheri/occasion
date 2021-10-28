package com.iranwebyar.occasions.ui.occasionList.birthdayList

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iranwebyar.occasions.data.model.QuestionsPOJO
import com.iranwebyar.occasions.databinding.ItemOccasionViewBinding
import com.iranwebyar.occasions.ui.base.BaseAdapter
import com.iranwebyar.occasions.ui.base.BaseViewHolder
import com.iranwebyar.occasions.ui.occasionList.OccasionListActivity

class BirthdayListAdapter
constructor(birthdayItemViewModelList: List<QuestionsPOJO.Answer?>?) : BaseAdapter<QuestionsPOJO.Answer?>() {
    var publicParent: ViewGroup? = null

    override fun onBindViewHolderBase(holder: BaseViewHolder?, position: Int) {
        holder!!.onBind(position)
    }

    override fun onCreateViewHolderBase(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        publicParent = parent
        val mBirthdayViewBinding = ItemOccasionViewBinding
            .inflate(LayoutInflater.from(parent!!.context), parent, false)
        return BirthdayViewHolder(mBirthdayViewBinding)
    }

    inner class BirthdayViewHolder(var mBinding: ItemOccasionViewBinding) : BaseViewHolder(mBinding.root),
        BirthdayItemViewModel.AnswersItemViewModelListener {
        private var birthdayItemViewModel: BirthdayItemViewModel? = null
        override fun onBind(position: Int) {
            val session = dataList!![position]!!
            birthdayItemViewModel = BirthdayItemViewModel(session, this)
            mBinding.viewModel = birthdayItemViewModel
            mBinding.executePendingBindings()
        }
        override fun onDeleteClick() {
            dataList!![layoutPosition]!!.id?.let {
                (itemView.context as OccasionListActivity?)!!.deleteCard(
                    it
                )
            }
        }
        override fun setStatusColor(status: String?) {
//            mBinding.cvLabel.setCardBackgroundColor(
//                if(status == "تایید شده")
//                    Color.parseColor("#73FF94")
//                else
//                    Color.parseColor("#FD9C95"))
        }

        override fun onSelectClick() {

        }
    }

    init {
        dataList = birthdayItemViewModelList
        //       notifyDataSetChanged();
    }
}
