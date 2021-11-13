package com.iranwebyar.occasions.utils


import android.app.AlarmManager
import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import com.iranwebyar.occasions.data.model.db.Occasion
import saman.zamani.persiandate.PersianDate
import java.util.*

class AlarmUtil {
    companion object {
        private const val TAG = "AlarmUtil"

        private fun getTriggerTime(dateString: String, timeString: String): Long {
//            val date = SimpleDateFormat("EEE, d MMM yyyy").parse(dateString)
//            val time = SimpleDateFormat("h:mm a").parse(timeString)
//            val calendar = Calendar.getInstance()
//            calendar.time = date ?: Date()
//            val initialYear = calendar.get(Calendar.YEAR)
//            val initialMonth = calendar.get(Calendar.MONTH)
//            val initialDate = calendar.get(Calendar.DAY_OF_MONTH)
//            calendar.time = time ?: Date()
//            val initialHourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
//            val initialMinute = calendar.get(Calendar.MINUTE)
//            calendar.set(initialYear, initialMonth, initialDate, initialHourOfDay, initialMinute)
//            val timeDiff = calendar.timeInMillis - Calendar.getInstance().timeInMillis
//            return SystemClock.elapsedRealtime() + timeDiff

            val pDate = PersianDate()
            pDate.shYear = dateString.split("/")[0].toInt()
            pDate.shMonth = dateString.split("/")[1].toInt()
            pDate.shDay = dateString.split("/")[2].toInt()
            pDate.hour = timeString.split(":")[0].toInt()
            pDate.minute = timeString.split(":")[1].toInt()
            pDate.second = 0

            val convertedDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tehran"))
            if(timeString.split(":")[0].toInt() == 12)
                convertedDate.time.time = pDate.toDate().time
            else
                convertedDate.time = pDate.toDate()

            // Set Current Date
            val currentDate = Calendar.getInstance()

            // Set Event Date
            val eventDate = Calendar.getInstance()
            eventDate[Calendar.YEAR] = convertedDate[Calendar.YEAR]
            eventDate[Calendar.MONTH] = convertedDate[Calendar.MONTH]
            eventDate[Calendar.DAY_OF_MONTH] = convertedDate[Calendar.DAY_OF_MONTH]
            eventDate[Calendar.HOUR_OF_DAY] = convertedDate[Calendar.HOUR_OF_DAY]
            eventDate[Calendar.MINUTE] = convertedDate[Calendar.MINUTE]
            eventDate[Calendar.SECOND] = convertedDate[Calendar.SECOND]
            eventDate.timeZone = TimeZone.getTimeZone("Asia/Tehran")

            val ss = eventDate.timeInMillis - currentDate.timeInMillis
            // Find how many milliseconds until the event
            return SystemClock.elapsedRealtime() + (eventDate.timeInMillis - currentDate.timeInMillis)
//            return SystemClock.elapsedRealtime() + 20000
        }

        private fun getRepeatTime(value: Int, unit: String): Long {
            return when (unit) {
                "Minute" ->
                    value * millisMinute
                "Hour" ->
                    value * millisHour
                "Day" ->
                    value * millisDay
                "Week" ->
                    value * millisWeek
                "Month" ->
                    value * millisMonth
                else ->
                    value * millisDay
            }
        }

        fun createAlarm(
            context: Context,
            reminder: Occasion,
            alarmManager: AlarmManager
        ) {
            val alarmPendingIntent = AlarmReceiver.getAlarmPendingIntent(
                context,
                reminder
            )
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                getTriggerTime(reminder.date!!, reminder.time!!),
                alarmPendingIntent
            )
//            updateAlarmWhenReboot(context,PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
        }

        fun cancelAlarm(
            context:Context,
            reminder: Occasion,
            alarmManager: AlarmManager
        ) {
            val alarmPendingIntent = AlarmReceiver.getAlarmPendingIntent(
                context.applicationContext,
                reminder
            )
            Log.d(TAG, "cancelAlarm: ")
            alarmManager.cancel(alarmPendingIntent)
//            updateAlarmWhenReboot(context,PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
        }


//        private fun updateAlarmWhenReboot(context:Context,state:Int){
//            val receiver = ComponentName(context,BootReceiver::class.java)
//            context.packageManager.setComponentEnabledSetting(
//                receiver,
//                state,
//                PackageManager.DONT_KILL_APP
//            )
//        }
    }
}