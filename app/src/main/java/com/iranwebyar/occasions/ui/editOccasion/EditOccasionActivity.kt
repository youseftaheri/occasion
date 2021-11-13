package com.iranwebyar.occasions.ui.editOccasion

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import com.iranwebyar.occasions.BR
import com.iranwebyar.occasions.R
import com.iranwebyar.occasions.databinding.ActivityEditOccasionBinding
import com.iranwebyar.occasions.ui.base.BaseActivity
import com.iranwebyar.occasions.ui.imagePicker.ImagePickerActivity
import com.iranwebyar.occasions.ui.occasionList.OccasionListActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*
import ir.hamsaa.persiandatepicker.util.PersianCalendarUtils
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import android.R.attr.typeface
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.ContentValues.TAG
import android.graphics.Color
import android.util.Log
import com.iranwebyar.occasions.data.model.OccasionsPOJO
import com.iranwebyar.occasions.data.model.db.Occasion
import com.iranwebyar.occasions.utils.*
import org.jetbrains.annotations.NotNull

@AndroidEntryPoint
class EditOccasionActivity : BaseActivity<ActivityEditOccasionBinding, EditOccasionViewModel>(),
    EditOccasionNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.activity_edit_occasion

    override fun getViewModelClass() = EditOccasionViewModel::class.java
    private var mActivityEditOccasionBinding: ActivityEditOccasionBinding? = null
    private val REQUEST_IMAGE = 100
    private var selectedFilePath: String? = null
    private var imageUrl: String? = null
    private var selectedOccasionToEdit: OccasionsPOJO.Occasion? = null
    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        mActivityEditOccasionBinding = viewDataBinding
        // Clearing older images from cache directory
        // don't call this line if you want to choose multiple images in the same activity
        // call this once the bitmap(s) usage is over
        ImagePickerActivity.clearCache(this)
        alarmManager = this.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        viewModel.fetchData()
    }

    override fun back() {
        onBackPressed()
    }

    override fun successfulOccasionAdd(occasion: Occasion?) {
        AlarmUtil.createAlarm(
            applicationContext,
            occasion!!,
            alarmManager
        )
        onOccasionClick()
    }

    override fun setData(selectedOccasion: OccasionsPOJO.Occasion?) {
        selectedOccasionToEdit = selectedOccasion
        mActivityEditOccasionBinding!!.tvDate.text = selectedOccasion!!.date
        mActivityEditOccasionBinding!!.tvTime.text = selectedOccasion.time
        mActivityEditOccasionBinding!!.etTitle.setText(selectedOccasion.title)
        mActivityEditOccasionBinding!!.spinnerOccasion.setSelection(
            when(selectedOccasion.type){
                getString(R.string.birthday) -> 0
                getString(R.string.personal) -> 1
                else -> 2
            }
        )
        CommonUtils.loadImageWithoutCash(
            this,
            Uri.fromFile(File(selectedOccasion.image!!)).toString(),
            mActivityEditOccasionBinding!!.ivPicture,
            R.drawable.ic_cam_gray
        )
        imageUrl = selectedOccasion.image
    }

    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, EditOccasionActivity::class.java)
        }
    }

    override fun selectOccasion() {
        viewModel.selectOccasion(mActivityEditOccasionBinding!!.spinnerOccasion.selectedItem.toString())
    }

    override fun onSubmitClick() {
        mActivityEditOccasionBinding!!.tilTitle.error = ""
        when {
            Objects.requireNonNull(mActivityEditOccasionBinding!!.etTitle.text).toString().trim { it <= ' ' }.isEmpty() ->
                mActivityEditOccasionBinding!!.tilTitle.error = getString(R.string.empty_title)
            Objects.requireNonNull(mActivityEditOccasionBinding!!.tvDate.text).toString().trim { it <= ' ' }.isEmpty() ->
                MyToast.show(this, getString(R.string.empty_date), true)
            Objects.requireNonNull(mActivityEditOccasionBinding!!.tvTime.text).toString().trim { it <= ' ' }.isEmpty() ->
                MyToast.show(this, getString(R.string.empty_time), true)
            else -> {
                if(imageUrl == null){
                    imageUrl = mActivityEditOccasionBinding!!.spinnerOccasion.selectedItem.toString()
                }
                viewModel.requestEditOccasion(
                    mActivityEditOccasionBinding!!.etTitle.text.toString().trim { it <= ' ' },
                    mActivityEditOccasionBinding!!.spinnerOccasion.selectedItem.toString(),
                    mActivityEditOccasionBinding!!.tvDate.text.toString().trim { it <= ' ' },
                    mActivityEditOccasionBinding!!.tvTime.text.toString().trim { it <= ' ' },
                    imageUrl, alarm = true, notification = true, selectedOccasionToEdit!!.id!!
                )
            }
        }
    }

    override fun onDateClick() {
        showDatePicker()
    }

    override fun onTimeClick() {
        TimeSelectionBottomSheetDialogFragment().apply {
            show(supportFragmentManager, TimeSelectionBottomSheetDialogFragment.TAG)
        }
    }

    override fun onOccasionClick() {
        startNewActivity(OccasionListActivity::class.java, isFinishAll = false, isCurrentFinish = true)
    }

    override fun pictureClick() {
        showOptions()
    }

    private fun showOptions(){
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
//                        showImagePickerOptions()
                        mContext?.let { MyImagePicker.selectImage(it) }
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri: Uri? = data?.getParcelableExtra("path")
                selectedFilePath = FilePath.getPath(mContext!!, uri!!)
                utils!!.loadImageWithoutCash(
                    mContext,
                    uri.toString(),
                    mActivityEditOccasionBinding!!.ivPicture,
                    R.drawable.ic_image_pick
                )
                if (CommonUtils.isExist(selectedFilePath)) {
                    if (uploadFile(selectedFilePath)) imageUrl = selectedFilePath!!
                } else {
                    Toast.makeText(mContext, "خطا در بارگذاری", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    //android upload file to server
    fun uploadFile(selectedFilePath: String?): Boolean {
        val selectedFile = File(selectedFilePath!!)
        return if (!selectedFile.isFile) {
            runOnUiThread { Toast.makeText(
                mContext,
                selectedFilePath + "این فایل وجود ندارد: ",
                Toast.LENGTH_SHORT
            ).show() }
            false
        } else {
            true
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_permission_title))
        builder.setMessage(getString(R.string.dialog_permission_message))
        builder.setPositiveButton(getString(R.string.go_to_settings)) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            getString(R.string.cancel)
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun showDatePicker(){
        val picker = PersianDatePickerDialog(this)
            .setPositiveButtonString("انتخاب")
//            .setNegativeButton("بیخیال")
            .setTodayButton("امروز")
            .setTodayButtonVisible(true)
            .setMinYear(PersianDatePickerDialog.THIS_YEAR)
            .setMaxYear(1404)
//            .setMaxMonth(PersianDatePickerDialog.THIS_MONTH)
//            .setMaxDay(PersianDatePickerDialog.THIS_DAY)
            .setInitDate(PersianDatePickerDialog.THIS_YEAR,
                PersianDatePickerDialog.THIS_MONTH,
                PersianDatePickerDialog.THIS_DAY)
            .setActionTextColor(Color.GRAY)
//            .setTypeFace(typeface)
            .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
            .setShowInBottomSheet(true)
            .setListener(object : PersianPickerListener {
                @SuppressLint("SetTextI18n")
                override fun onDateSelected(@NotNull persianPickerDate: PersianPickerDate) {
                    Log.d(TAG, "onDateSelected: " + persianPickerDate.timestamp) //675930448000
                    Log.d(
                        TAG,
                        "onDateSelected: " + persianPickerDate.gregorianDate
                    ) //Mon Jun 03 10:57:28 GMT+04:30 1991
                    Log.d(
                        TAG,
                        "onDateSelected: " + persianPickerDate.persianLongDate
                    ) // دوشنبه  13  خرداد  1370
                    Log.d(TAG, "onDateSelected: " + persianPickerDate.persianMonthName) //خرداد
                    Log.d(
                        TAG,
                        "onDateSelected: " + PersianCalendarUtils.isPersianLeapYear(
                            persianPickerDate.persianYear
                        )
                    ) //true
//                    Toast.makeText(
//                        this@NewOccasionActivity,
//                        persianPickerDate.persianYear.toString() + "/" + persianPickerDate.persianMonth + "/" + persianPickerDate.persianDay,
//                        Toast.LENGTH_SHORT
//                    ).show()
                    mActivityEditOccasionBinding!!.tvDate.text =
                        persianPickerDate.persianYear.toString() + "/" +
                                persianPickerDate.persianMonth + "/" +
                                persianPickerDate.persianDay
                }

                override fun onDismissed() {}
            })

        picker.show()
    }

    @SuppressLint("SetTextI18n")
    fun selectTime(hour: String?, minute: String?){
        mActivityEditOccasionBinding!!.tvTime.text = "$hour:$minute"
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

}
