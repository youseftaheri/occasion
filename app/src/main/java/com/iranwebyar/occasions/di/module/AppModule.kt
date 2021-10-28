package com.iranwebyar.occasions.di.module

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.iranwebyar.occasions.data.AppDataManager
import com.iranwebyar.occasions.data.DataManager
import com.iranwebyar.occasions.data.local.prefs.AppPreferencesHelper
import com.iranwebyar.occasions.data.local.prefs.PreferencesHelper
import com.iranwebyar.occasions.data.remote.ApiHelper
import com.iranwebyar.occasions.data.remote.Apis
import com.iranwebyar.occasions.data.remote.AppApiHelper
import com.iranwebyar.occasions.di.PreferenceInfo
import com.iranwebyar.occasions.utils.CommonUtils
import com.iranwebyar.occasions.utils.Const
import com.iranwebyar.occasions.utils.rx.AppSchedulerProvider
import com.iranwebyar.occasions.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideApiHelper(appApiHelper: AppApiHelper): ApiHelper {
        return appApiHelper
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Apis {
        return retrofit.create(Apis::class.java)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Provides
    @Singleton
    fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper {
        return appPreferencesHelper
    }

    @Provides
    @PreferenceInfo
    fun providePreferenceName(): String {
        return Const.PREF_NAME
    }

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Singleton
    @Provides
    fun provideUtils(context: Context?): CommonUtils {
        return CommonUtils
    }

//    @Singleton
//    @Provides
//    fun provideEntryPagerAdapter(activity: EntryActivity): EntryPagerAdapter {
//        return EntryPagerAdapter(activity.supportFragmentManager, activity)
//    }

//    @Singleton
//    @Provides
//    fun provideNewsAdapter(): NewsAdapter {
//        return NewsAdapter(ArrayList())
//    }
//
//    @Singleton
//    @Provides
//    fun provideNewsLinearLayoutManager(activity: NewsActivity?): LinearLayoutManager {
//        return LinearLayoutManager(activity)
//    }

//    @Provides
//    fun provideAnalysesAdapter(): AnalysesAdapter {
//        return AnalysesAdapter(ArrayList())
//    }
//
//    @Provides
//    fun provideAnalysesLinearLayoutManager(activity: AnalysesActivity?): LinearLayoutManager {
//        return LinearLayoutManager(activity)
//    }
//    @Provides
//    fun provideArticlesAdapter(): ArticlesAdapter {
//        return ArticlesAdapter(ArrayList())
//    }
//
//    @Provides
//    fun provideArticlesLinearLayoutManager(activity: ArticlesActivity?): LinearLayoutManager {
//        return LinearLayoutManager(activity)
//    }
//    @Provides
//    fun provideBooksAdapter(): BooksAdapter {
//        return BooksAdapter(ArrayList())
//    }
//
//    @Provides
//    fun provideBooksLinearLayoutManager(activity: BooksActivity?): LinearLayoutManager {
//        return LinearLayoutManager(activity)
//    }
}