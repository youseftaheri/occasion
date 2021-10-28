package com.iranwebyar.occasions.ui.quizPage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iranwebyar.occasions.data.DataManager
import com.iranwebyar.occasions.data.FakeQuestions
import com.iranwebyar.occasions.data.model.QuestionsPOJO
import com.iranwebyar.occasions.utils.CoroutineTestRule
import com.iranwebyar.occasions.utils.TestCoroutineRule
import com.iranwebyar.occasions.utils.rx.TestSchedulerProvider
import com.iranwebyar.occasions.utils.Const
import com.google.gson.Gson
import io.reactivex.schedulers.TestScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class QuizViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Mock
    var mSchedulesCallback: QuizNavigator? = null

    @Mock
    var mMockDataManager: DataManager? = null
    private var mSchedulesViewModel: QuizViewModel? = null
    private var mTestScheduler: TestScheduler? = null

    @Before
    @Throws(java.lang.Exception::class)
    open fun setUp(): Unit {
        mTestScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(mTestScheduler!!)
        mSchedulesViewModel = QuizViewModel(mMockDataManager, testSchedulerProvider)
        mSchedulesViewModel!!.setNavigator(mSchedulesCallback)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mTestScheduler = null
        mSchedulesViewModel = null
        mSchedulesCallback = null
    }

    @Test
    fun testShowDetailsError(){
        testCoroutineRule.runBlockingTest {
            val contactResponse = Gson().fromJson(FakeQuestions().data, QuestionsPOJO.Data::class.java)
            doReturn(contactResponse)
                .`when`(mMockDataManager)
                ?.questionsData//("1111111111")

            mSchedulesViewModel?.showDetails("1111111111")
            verify(mSchedulesCallback)?.showLoading()
            verify(mSchedulesCallback)?.hideLoading()
            verify(mSchedulesCallback, atLeastOnce())!!.handleError(contactResponse.meta!!.errorDetail)
        }
    }

    @Test
    fun testShowDetailsApi(){
        testCoroutineRule.runBlockingTest {
            val contactResponse = ContactPOJO(Const.TWO_HUNDRED, "test")
            doReturn(contactResponse)
                .`when`(mMockDataManager)
                ?.getContactResults("1111111111")

            mSchedulesViewModel?.showDetails("1111111111")
            verify(mSchedulesCallback)?.showLoading()
            verify(mSchedulesCallback, atLeastOnce())?.hideLoading()
        }
    }
}
