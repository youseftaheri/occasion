package com.iranwebyar.occasions.ui.occasionList

import com.iranwebyar.occasions.data.DataManager
import com.iranwebyar.occasions.ui.base.BaseViewModel
import com.iranwebyar.occasions.utils.rx.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OccasionListViewModel
@Inject
constructor(dataManager: DataManager?, schedulerProvider: SchedulerProvider?) : BaseViewModel<OccasionListNavigator?>(dataManager!!, schedulerProvider!!) {

    val entry: Unit
        get() {
            navigator!!.setUp()
        }


    fun deleteCard(cardId: String){

    }

    fun onAddCardClick() {
        navigator!!.onAddCardClick()
    }
}
