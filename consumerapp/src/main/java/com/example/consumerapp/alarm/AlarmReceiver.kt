package com.example.consumerapp.alarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.consumerapp.MainActivity
import com.example.consumerapp.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val TYPE_DAILY = "Daily Reminder"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"


        private const val ID_DAILY = 100
        private const val TIME_DAILY = "9" // set the alarm time here
    }

    override fun onReceive(context: Context, intent: Intent) {

        showAlarmNotification(context)
    }

    fun setDailyReminder(context: Context, type: String, time: String) {

       //if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent : PendingIntent = Intent(context, AlarmReceiver::class.java) .let {
            PendingIntent.getBroadcast(
                    context,
                    ID_DAILY, it, 0
            )
        }

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //intent.putExtra(EXTRA_MESSAGE, message)
        //intent.putExtra(EXTRA_TYPE, type)

        val timeArray =
            TIME_DAILY.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            intent
        )
        Toast.makeText(context, "Daily Reminder ON", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, "Daily Reminder OFF", Toast.LENGTH_SHORT).show()
    }

    private fun showAlarmNotification(
        context: Context?
    ) {

        val channelId = "rockyard"
        val channelName = "daily notification"

        val message = context?.resources?.getString(R.string.daily_message)

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context)
                .addParentStack(MainActivity::class.java)
                .addNextIntent(intent)
                .getPendingIntent(ID_DAILY, PendingIntent.FLAG_UPDATE_CURRENT)


        val notificationManagerCompat =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Daily Reminder")
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mBuilder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()
        notificationManagerCompat.notify(100, notification)
    }
}