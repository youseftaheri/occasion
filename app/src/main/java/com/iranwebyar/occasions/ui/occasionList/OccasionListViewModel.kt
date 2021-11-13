package com.iranwebyar.occasions.ui.occasionList

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.iranwebyar.occasions.data.DataManager
import com.iranwebyar.occasions.data.FakeQuestions
import com.iranwebyar.occasions.data.model.OccasionsPOJO
import com.iranwebyar.occasions.data.model.db.Occasion
import com.iranwebyar.occasions.ui.base.BaseViewModel
import com.iranwebyar.occasions.utils.rx.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OccasionListViewModel
@Inject
constructor(dataManager: DataManager?, schedulerProvider: SchedulerProvider?) : BaseViewModel<OccasionListNavigator?>(dataManager!!, schedulerProvider!!) {

    val entry: Unit
        get() {
//                viewModelScope.launch (Dispatchers.Main) { // launches coroutine in main thread
//                try {
////                    dataManager.deleteAll()
//                    val fakeOccasions = Gson().fromJson(FakeQuestions().data, OccasionsPOJO.Data::class.java)
//                    if (fakeOccasions != null){
//                        var occasion : Occasion
//                        for (i in fakeOccasions.occasions!!.indices) {
//                            occasion = Occasion()
//                            occasion.id = fakeOccasions.occasions!![i].id
////                            occasion.occasionId = fakeOccasions.occasions!![i].id!!.toLong()
//                            occasion.type = fakeOccasions.occasions!![i].type
//                            occasion.title = fakeOccasions.occasions!![i].title
//                            occasion.image = fakeOccasions.occasions!![i].image
//                            occasion.date = fakeOccasions.occasions!![i].date
//                            occasion.time = fakeOccasions.occasions!![i].time
//                            occasion.alarm = fakeOccasions.occasions!![i].alarm
//                            occasion.notification = fakeOccasions.occasions!![i].notification
//                            dataManager.insertOccasion(occasion)
//                        }
//                    }
//
//                    val occasions: MutableList<OccasionsPOJO.Occasion> = ArrayList()
//                    val ss = dataManager.allOccasions()
//                    for (dbOccasion in ss!!){
//                        if(dbOccasion.type == "تولد") {
//                            var occasion = OccasionsPOJO.Occasion()
//                            occasion.id = dbOccasion.id
//                            occasion.type = dbOccasion.type
//                            occasion.title = dbOccasion.title
//                            occasion.image = dbOccasion.image
//                            occasion.date = dbOccasion.date
//                            occasion.time = dbOccasion.time
//                            occasion.alarm = dbOccasion.alarm
//                            occasion.notification = dbOccasion.notification
//                            occasions.add(occasion)
//                        }
//                    }
//
//                    dataManager.occasionsList = occasions
//
//
//                    cardListItemsLiveData.value = dataManager.occasionsList
//                    emptyMessageVisibility.set(cardListItemsLiveData.value!!.isEmpty())
//                } catch (e: Exception) {
//                    navigator!!.hideLoading()
//                    navigator!!.handleError(e.message)
//                }
//            }
//            navigator!!.hideLoading()
            navigator!!.setUp()
        }


    fun onAddCardClick() {
        navigator!!.onAddCardClick()
    }


    fun setSelectedOccasionToEdit(occasion: OccasionsPOJO.Occasion) {
        viewModelScope.launch {
            try {
                dataManager.selectedOccasion = occasion
                navigator!!.openEditActivity()
            } catch (e: Exception) {
                navigator!!.hideLoading()
                navigator!!.handleError(e.message)
            }
        }
    }


}
