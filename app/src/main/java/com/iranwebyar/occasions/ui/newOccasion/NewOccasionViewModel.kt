package com.iranwebyar.occasions.ui.newOccasion

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.iranwebyar.occasions.data.DataManager
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

    val selectedOccasion: ObservableField<String> = ObservableField()

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

    fun requestRegisterOccasion(title: String, type: String, date: String, time: String, image: String?) {


    }
}