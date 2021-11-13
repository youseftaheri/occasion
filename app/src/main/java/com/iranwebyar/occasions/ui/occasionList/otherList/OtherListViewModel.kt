package com.iranwebyar.occasions.ui.occasionList.otherList

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iranwebyar.occasions.data.DataManager
import com.iranwebyar.occasions.data.model.OccasionsPOJO
import com.iranwebyar.occasions.ui.base.BaseViewModel
import com.iranwebyar.occasions.utils.rx.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtherListViewModel
@Inject
constructor(dataManager: DataManager?, schedulerProvider: SchedulerProvider?) : BaseViewModel<OtherListNavigator?>(dataManager!!, schedulerProvider!!) {

    private val cardListItemsLiveData: MutableLiveData<List<OccasionsPOJO.Occasion?>> = MutableLiveData()

    @JvmField
    val emptyMessageVisibility: ObservableField<Boolean> = ObservableField(false)

    init {
//        fetchData
    }

    val fetchData: Unit
        get() {
            viewModelScope.launch (Dispatchers.Main) { // launches coroutine in main thread
                try {
//                    val fakeOccasions = Gson().fromJson(FakeQuestions().data, OccasionsPOJO.Data::class.java)
//                    if (fakeOccasions != null){
//                        var occasion : Occasion
//                        for (i in fakeOccasions.occasions!!.indices) {
//                            occasion = Occasion(
//                                fakeOccasions.occasions!![i].id!!,
//                            )
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

                    val occasions: MutableList<OccasionsPOJO.Occasion> = ArrayList()
                    val ss = dataManager.allOccasions()
                    for (dbOccasion in ss!!){
                        if(dbOccasion.type == "متفرقه") {
                            val occasion = OccasionsPOJO.Occasion()
                            occasion.id = dbOccasion.id
                            occasion.type = dbOccasion.type
                            occasion.title = dbOccasion.title
                            occasion.image = dbOccasion.image
                            occasion.date = dbOccasion.date
                            occasion.time = dbOccasion.time
                            occasion.alarm = dbOccasion.alarm
                            occasion.notification = dbOccasion.notification
                            occasions.add(occasion)
                        }
                    }

                    dataManager.occasionsList = occasions

                    cardListItemsLiveData.value = dataManager.occasionsList
                    emptyMessageVisibility.set(cardListItemsLiveData.value!!.isEmpty())
                    navigator!!.setCards()
                } catch (e: Exception) {
                    navigator!!.hideLoading()
                    navigator!!.handleError(e.message)
                }
            }
        }


    fun cancelAlarm(cardId: Long) {
        viewModelScope.launch(Dispatchers.Main) { // launches coroutine in main thread
            try {
                navigator!!.cancelAlarm(dataManager.findOccasionById(cardId))
                deleteCard(cardId)
            } catch (e: Exception) {
                navigator!!.hideLoading()
                navigator!!.handleError(e.message)
            }
        }
    }

    fun deleteCard(cardId: Long){
        viewModelScope.launch (Dispatchers.Main) { // launches coroutine in main thread
            try {
                dataManager.deleteOccasionById(cardId)
                val occasions: MutableList<OccasionsPOJO.Occasion> = ArrayList()
                val ss = dataManager.allOccasions()
                for (dbOccasion in ss!!){
                    if(dbOccasion.id != cardId && dbOccasion.type == "متفرقه") {
                        val occasion = OccasionsPOJO.Occasion()
                        occasion.id = dbOccasion.id
                        occasion.type = dbOccasion.type
                        occasion.title = dbOccasion.title
                        occasion.image = dbOccasion.image
                        occasion.date = dbOccasion.date
                        occasion.time = dbOccasion.time
                        occasion.alarm = dbOccasion.alarm
                        occasion.notification = dbOccasion.notification
                        occasions.add(occasion)
                    }
                }
                dataManager.occasionsList = occasions
                cardListItemsLiveData.value = dataManager.occasionsList
                navigator!!.successfulOccasionDelete()
            } catch (e: Exception) {
                navigator!!.hideLoading()
                navigator!!.handleError(e.message)
            }
        }
    }

    fun getCardItemsLiveData(): LiveData<List<OccasionsPOJO.Occasion?>> {
        return cardListItemsLiveData
    }

}
