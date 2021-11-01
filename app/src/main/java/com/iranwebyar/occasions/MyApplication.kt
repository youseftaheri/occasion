package com.iranwebyar.occasions

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.iranwebyar.mellichange.utils.ResourceProvider
import com.iranwebyar.occasions.utils.Const
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.SSLContext
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

//Main application class
@HiltAndroidApp
class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        try {
            ProviderInstaller.installIfNeeded(applicationContext)
            val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        ViewPump.init(
            ViewPump.builder()
            .addInterceptor(
                CalligraphyInterceptor(
                CalligraphyConfig.Builder()
                    .setDefaultFontPath(Const.FONT_FILE)
                    .setFontAttrId(R.attr.fontPath)
                    .build())
            )
            .build()
        )
    }

    fun setLanguage(context: Context, languageCode: String): Context {
        var context = context
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            context = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        return context
    }

    private var mResourceProvider: ResourceProvider? = null
    val resourceProvider: ResourceProvider
        get() {
            if (mResourceProvider == null) mResourceProvider = ResourceProvider(this)
            return mResourceProvider!!
        }
}