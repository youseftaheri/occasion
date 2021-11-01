package com.iranwebyar.occasions.ui.newOccasion

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.iranwebyar.occasions.data.DataManager
import com.iranwebyar.occasions.data.model.db.Occasion
import com.iranwebyar.occasions.ui.base.BaseViewModel
import com.iranwebyar.occasions.utils.Const
import com.iranwebyar.occasions.utils.rx.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewOccasionViewModel
@Inject
constructor(dataManager: DataManager?, schedulerProvider: SchedulerProvider?) : BaseViewModel<NewOccasionNavigator?>(dataManager!!, schedulerProvider!!) {

    private val selectedOccasion: ObservableField<String> = ObservableField()

    fun onSelectOccasion(){
        navigator!!.selectOccasion()
    }

    fun back() {
        navigator!!.back()
    }

    fun selectOccasion(occasion: String){
        selectedOccasion.set(occasion)
    }

    fun pictureClick() {
        navigator!!.pictureClick()
    }

    fun onSubmitClick() {
        navigator!!.onSubmitClick()
    }

    fun onDateClick() {
        navigator!!.onDateClick()
    }

    fun onTimeClick() {
        navigator!!.onTimeClick()
    }

    fun requestRegisterOccasion(title: String, type: String,
                                date: String, time: String, image: String?,
                                alarm: Boolean, notification: Boolean) {
         navigator!!.showLoading()
            viewModelScope.launch (Dispatchers.Main) { // launches coroutine in main thread
                try {
                    val occasion = Occasion()
//                    occasion.id = 5
                    occasion.type = type
                    occasion.title = title
                    occasion.date = date
                    occasion.time = time
                    occasion.image = image
                    occasion.alarm = alarm
                    occasion.notification = notification
                    dataManager.insertOccasion(occasion)
                    navigator!!.successfulOccasionAdd()
                } catch (e: Exception) {
                    navigator!!.hideLoading()
                    navigator!!.handleError(e.message)
                }
            }
            navigator!!.hideLoading()
    }
}