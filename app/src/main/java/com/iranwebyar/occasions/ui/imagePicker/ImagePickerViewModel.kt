package com.iranwebyar.occasions.ui.imagePicker

import com.iranwebyar.occasions.data.DataManager
import com.iranwebyar.occasions.ui.base.BaseViewModel
import com.iranwebyar.occasions.utils.rx.SchedulerProvider

class ImagePickerViewModel (dataManager: DataManager?, schedulerProvider: SchedulerProvider?) :
    BaseViewModel<ImagePickerNavigator?>(dataManager!!, schedulerProvider!!) {
}