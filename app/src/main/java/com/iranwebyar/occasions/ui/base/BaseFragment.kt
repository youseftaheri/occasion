package com.iranwebyar.occasions.ui.base

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iranwebyar.occasions.utils.CommonUtils
import com.iranwebyar.occasions.utils.CommonUtils.showLoadingDialog
import dagger.hilt.android.internal.managers.FragmentComponentManager
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>?> : Fragment() {
    var baseActivity: BaseActivity<*, *>? = null
        private set
    private var mRootView: View? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null
    private var mProgressDialogg: ProgressDialog? = null

    @JvmField
    @Inject
    var utils: CommonUtils? = null

    val viewModel: V by lazy { ViewModelProvider(this).get(getViewModelClass()) }
    abstract fun getViewModelClass(): Class<V>

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
//    abstract val viewModel: V

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity = context
            baseActivity = activity
            activity.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = viewModel
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mRootView = viewDataBinding!!.getRoot()
        return mRootView
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.lifecycleOwner = this
        viewDataBinding!!.executePendingBindings()
    }

    fun hideKeyboard() {
        baseActivity?.hideKeyboard()
    }

    val isNetworkConnected: Boolean
        get() = baseActivity != null && baseActivity?.isNetworkConnected!!

    interface Callback {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String?)
    }

    fun startNewActivity(activity: Class<*>?, isFinishAll: Boolean, isCurrentFinish: Boolean) {
        val i = Intent(context, activity)
        if (isFinishAll) i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
        if (isCurrentFinish) baseActivity!!.finish()
        utils!!.gotoNextActivityAnimation(FragmentComponentManager.findActivity(requireContext()) as Activity)
    }


    fun showLoading() {
        hideLoading()
        //        mProgressDialog.show();
        mProgressDialogg = showLoadingDialog(context)
    }

    fun hideLoading() {
        if (mProgressDialogg != null && mProgressDialogg!!.isShowing) {
            //   mProgressDialog.cancel();
            mProgressDialogg!!.dismiss()
        }
    }

    fun animateViewGroupChildren(parent: ViewGroup, animId: Int) {
        val animZoomIn: Animation = AnimationUtils.loadAnimation(activity, animId)
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is ViewGroup) {
                animateViewGroupChildren(child, animId)
                // DO SOMETHING WITH VIEWGROUP, AFTER CHILDREN HAS BEEN LOOPED
            } else {
            child.startAnimation(animZoomIn)
            }
        }
    }

    fun animateViewGroupContainers(parent: ViewGroup, animId: Int) {
        val animZoomIn: Animation = AnimationUtils.loadAnimation(activity, animId)
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            child.startAnimation(animZoomIn)
        }
    }

    fun animateView(view: View, animId: Int) {
        val animZoomIn: Animation = AnimationUtils.loadAnimation(activity, animId)
        view.startAnimation(animZoomIn)
    }
}