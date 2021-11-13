package com.iranwebyar.occasions.ui.occasionList

import android.annotation.SuppressLint
import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.ui.occasionList.birthdayList.BirthdayListFragment
import com.iranwebyar.occasions.ui.occasionList.otherList.OtherListFragment
import com.iranwebyar.occasions.ui.occasionList.personalList.PersonalListFragment
import java.util.*

@SuppressLint("WrongConstant")
class OccasionListPagerAdapter
    constructor(fragmentManager: FragmentManager?, var context: Activity) : FragmentStatePagerAdapter(fragmentManager!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var mTabCount = 0
    override fun getCount(): Int {
        return mTabCount
    }

    fun setCount(count: Int) {
        mTabCount = count
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OtherListFragment.newInstance()
            1 -> PersonalListFragment.newInstance()
            2 -> BirthdayListFragment.newInstance()
            else -> null
        }!!
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val mFragmentTitleList: MutableList<String> = ArrayList()
        mFragmentTitleList.add(context.getString(R.string.others))
        mFragmentTitleList.add(context.getString(R.string.personalPlan))
        mFragmentTitleList.add(context.getString(R.string.birthday))
        return mFragmentTitleList[position]
    }

}