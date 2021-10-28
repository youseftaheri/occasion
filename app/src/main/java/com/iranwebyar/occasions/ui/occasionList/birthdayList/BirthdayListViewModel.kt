package com.iranwebyar.occasions.ui.occasionList.birthdayList

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iranwebyar.occasions.data.DataManager
import com.iranwebyar.occasions.data.model.QuestionsPOJO
import com.iranwebyar.occasions.ui.base.BaseViewModel
import com.iranwebyar.occasions.utils.Const
import com.iranwebyar.occasions.utils.rx.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class BirthdayListViewModel
@Inject
constructor(dataManager: DataManager?, schedulerProvider: SchedulerProvider?) : BaseViewModel<BirthdayListNavigator?>(dataManager!!, schedulerProvider!!) {

    val cardListItemsLiveData: MutableLiveData<List<QuestionsPOJO.Answer>?> = MutableLiveData()

    @JvmField
    val emptyMessageVisibility: ObservableField<Boolean> = ObservableField(false)

    init {
        cardListItemsLiveData.value = emptyList()
    }

    val fetchData: Unit
        get() {

        }

    fun getCardItemsLiveData(): LiveData<List<QuestionsPOJO.Answer>?> {
        return cardListItemsLiveData
    }

}