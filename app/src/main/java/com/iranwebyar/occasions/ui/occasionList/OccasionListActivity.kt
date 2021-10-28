package com.iranwebyar.occasions.ui.occasionList

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager.widget.ViewPager.*
import com.iranwebyar.occasions.BR
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.databinding.ActivityOccasionListBinding
import com.iranwebyar.occasions.utils.Const
import com.iranwebyar.occasions.ui.base.BaseActivity
import com.iranwebyar.occasions.ui.newOccasion.NewOccasionActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OccasionListActivity : BaseActivity<ActivityOccasionListBinding, OccasionListViewModel>(), OccasionListNavigator{
    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_occasion_list

    override fun getViewModelClass() = OccasionListViewModel::class.java

    var mPagerAdapter: OccasionListPagerAdapter? = null

    private var mActivityOccasionListBinding: ActivityOccasionListBinding? = null
    private var indicatorWidth = 0
    private var indicator2Width = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mActivityOccasionListBinding = viewDataBinding
        mPagerAdapter = OccasionListPagerAdapter(this.supportFragmentManager, this)

        //to prevent animation lag
        mActivityOccasionListBinding!!.root.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        mActivityOccasionListBinding!!.tabs.layoutDirection = View.LAYOUT_DIRECTION_LTR
        viewModel.entry
    }

    override fun setUp() {
        changeTabsFont()
        //Set up the view pager and fragments
        mPagerAdapter!!.count = 3
        mActivityOccasionListBinding!!.viewPager.adapter = mPagerAdapter
        mActivityOccasionListBinding!!.tabs.addTab(mActivityOccasionListBinding!!.tabs.newTab().setText(getString(R.string.others)))
        mActivityOccasionListBinding!!.tabs.addTab(mActivityOccasionListBinding!!.tabs.newTab().setText(getString(R.string.personalPlan)))
        mActivityOccasionListBinding!!.tabs.addTab(mActivityOccasionListBinding!!.tabs.newTab().setText(getString(R.string.birthday)))
        mActivityOccasionListBinding!!.viewPager.offscreenPageLimit = mActivityOccasionListBinding!!.tabs.tabCount
        mActivityOccasionListBinding!!.viewPager.currentItem = mActivityOccasionListBinding!!.tabs.tabCount
        mActivityOccasionListBinding!!.tabs.setupWithViewPager(mActivityOccasionListBinding!!.viewPager)

        //Determine indicator width at runtime
        mActivityOccasionListBinding!!.tabs.post(Runnable {
            indicatorWidth = mActivityOccasionListBinding!!.tabs.width / mActivityOccasionListBinding!!.tabs.tabCount

            //Assign new width
            val indicatorParams = mActivityOccasionListBinding!!.indicator.layoutParams as FrameLayout.LayoutParams
            indicatorParams.width = indicatorWidth
            mActivityOccasionListBinding!!.indicator.layoutParams = indicatorParams
            val param = mActivityOccasionListBinding!!.tabs[0]
//            mActivityOccasionListBinding!!.tabs.layoutParams = indicatorParams
        })

        mActivityOccasionListBinding!!.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            //To move the indicator as the user scroll, we will need the scroll offset values
            //positionOffset is a value from [0..1] which represents how far the page has been scrolled
            //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener
            override fun onPageScrolled(i: Int, positionOffset: Float, positionOffsetPx: Int) {
                val params = mActivityOccasionListBinding!!.indicator.layoutParams

                //Multiply positionOffset with indicatorWidth to get translation
                val positionIndex: Int
                if(i == 0) positionIndex = 1 else positionIndex = 0
                val translationOffset: Float = (-positionOffset + positionIndex) * indicatorWidth
//                params.leftMargin = translationOffset.toInt()
//                mActivityOccasionListBinding!!.indicator.layoutParams = params

                val param = mActivityOccasionListBinding!!.indicator.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(0,0,translationOffset.toInt(),0)
                mActivityOccasionListBinding!!.indicator.layoutParams = param

//                mOccasionListViewModel!!.setLeftMargin(translationOffset.toInt())
            }

            override fun onPageSelected(i: Int) {}
            override fun onPageScrollStateChanged(i: Int) {}
        })
    }

    private fun changeTabsFont() {
        val face = Typeface.createFromAsset(assets, Const.FONT_FILE)
//        mActivityOccasionListBinding!!.tabs.setSelectedTabIndicatorColor(
//            ContextCompat.getColor(
//                mContext!!,
//                R.color.colorAccent
//            )
//        )
        mActivityOccasionListBinding!!.tabs.setTabTextColors(
            ContextCompat.getColor(
                mContext!!,
                R.color.colorGray
            ), ContextCompat.getColor(mContext!!, R.color.colorWhite)
        )
        mActivityOccasionListBinding!!.tabs.setSelectedTabIndicatorColor(
            ContextCompat.getColor(
                mContext!!,
                R.color.colorPrimary
            )
        )
        val vg = mActivityOccasionListBinding!!.tabs.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildCount = vgTab.childCount
            for (i in 0 until tabChildCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    tabViewChild.typeface = face
                }
            }
        }
    }

    override fun onAddCardClick() {
        startNewActivity(NewOccasionActivity::class.java, isFinishAll = false, isCurrentFinish = false)
    }

    fun deleteCard(cardId: String) {
        val inflater = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val alertDialog: AlertDialog
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(mContext)
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
                viewModel.deleteCard(cardId)
            } })
        negativeButton.setOnClickListener(View.OnClickListener { alertDialog.dismiss() })
    }

    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, OccasionListActivity::class.java)
        }
    }
}
