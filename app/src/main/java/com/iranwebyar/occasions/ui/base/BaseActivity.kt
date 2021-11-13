package com.iranwebyar.occasions.ui.base

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.multidex.MultiDex
import com.iranwebyar.occasions.MyApplication
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.utils.*
import com.iranwebyar.occasions.utils.CommonUtils.showLoadingDialog
import com.iranwebyar.occasions.utils.NetworkUtils.isNetworkConnected
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> : AppCompatActivity(), BaseFragment.Callback {
    private var mProgressDialogg: ProgressDialog? = null

    @JvmField
    @Inject
    var utils: CommonUtils? = null
    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null
    @JvmField
    var mContext: Context? = null


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

    override fun onFragmentAttached() {}
    override fun onFragmentDetached(tag: String?) {}
    override fun attachBaseContext(newBase: Context) {
        val context = (newBase.applicationContext as MyApplication)
//            .setLanguage(newBase, "en")
            .setLanguage(newBase, "fa")
        super.attachBaseContext(ViewPumpContextWrapper.wrap(context))
        MultiDex.install(this)
        //ACRA.init(this);
    }

    companion object {
        const val PERMISSION_REQUEST_STORAGE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath(Const.FONT_FILE)
                            .setFontAttrId(R.attr.fontPath)
                            .build())
                )
                .build())
        mContext = this@BaseActivity
        performDataBinding()
    }


    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String?): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }

    fun hideKeyboard() {
//        val view = this.currentFocus
//        if (view != null) {
//            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm?.hideSoftInputFromWindow(view.windowToken, 0)
//        }

        // Check if no view has focus:
        val view = this.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun hideLoading() {
        if (mProgressDialogg != null && mProgressDialogg!!.isShowing) {
            //   mProgressDialog.cancel();
            mProgressDialogg!!.dismiss()
        }
    }

    fun handleError(exception: String?) {
        MyToast.show(this, exception, true)
    }

    val isNetworkConnected: Boolean
        get() = isNetworkConnected(applicationContext)


    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String?>?, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions!!, requestCode)
        }
    }

    fun showLoading() {
        hideLoading()
        //        mProgressDialog.show();
        mProgressDialogg = showLoadingDialog(this)
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mViewModel = if (mViewModel == null) viewModel else mViewModel
        viewDataBinding!!.setVariable(bindingVariable, mViewModel)
        viewDataBinding!!.executePendingBindings()
    }

    fun startNewActivity(activity: Class<*>?, isFinishAll: Boolean, isCurrentFinish: Boolean) {
        val i = Intent(mContext, activity)
        if (isFinishAll) i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
        if (isCurrentFinish) finish()
        utils!!.gotoNextActivityAnimation(mContext!!)
    }

    fun animateViewGroupChildren(parent: ViewGroup, animId: Int) {
        val animZoomIn: Animation = AnimationUtils.loadAnimation(this, animId)
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

    fun animateView(view: View, animId: Int) {
        val animZoomIn: Animation = AnimationUtils.loadAnimation(this, animId)
                view.startAnimation(animZoomIn)
    }

}