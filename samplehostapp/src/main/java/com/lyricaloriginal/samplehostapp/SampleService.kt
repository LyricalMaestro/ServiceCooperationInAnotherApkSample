package com.lyricaloriginal.samplehostapp

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import android.support.v4.app.NotificationCompat
import android.app.NotificationManager
import android.app.NotificationChannel
import android.annotation.TargetApi
import android.app.Notification


/**
 * 外部apkとやり取りをするためのService.
 */
class SampleService : Service(), SampleModel.Listener {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "notification"
        const val NOTIFICATION_ID = 1

        const val TAG = "HostService"

        const val REQUEST_RESIST = 10
        const val RESPONSE_RESIST = 20

        const val REQUEST_SEND_MSG = 30
    }

    private lateinit var receiveFromClientHandler: Handler
    private lateinit var receiveFromClientMessenger: Messenger

    private var sendToClientMessenger: Messenger? = null

    private lateinit var model: SampleModel

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("処理中")
                .setContentText("処理中")
                .setWhen(System.currentTimeMillis())
                .build()
        startForeground(NOTIFICATION_ID, notification)

        receiveFromClientHandler = ReceiveFromClientHandler()
        receiveFromClientMessenger = Messenger(receiveFromClientHandler)

        model = SampleModel(this)
        model.start()
    }

    override fun onBind(intent: Intent): IBinder {
        return receiveFromClientMessenger.binder
    }

    override fun onDestroy() {
        super.onDestroy()
        model.stop()
        stopForeground(true)
    }

    override fun onProgressNotified(msg: String) {
        val bundle = Bundle()
        bundle.putString("msg", msg)

        try {
            val message = Message.obtain(null, REQUEST_SEND_MSG, bundle)
            sendToClientMessenger?.send(message)
        } catch (ex: RemoteException) {
            Log.e(TAG, ex.localizedMessage, ex)
        }
    }

    @TargetApi(26)
    private fun createNotificationChannel() {
        val name = "ホストアプリ通知用" // 通知チャンネル名
        val importance = NotificationManager.IMPORTANCE_HIGH // デフォルトの重要度
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)

        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.enableVibration(true)
        channel.enableLights(true)
        channel.setShowBadge(false) // ランチャー上でアイコンバッジを表示するかどうか

        // NotificationManagerCompatにcreateNotificationChannel()は無い。
        val nm = getSystemService(NotificationManager::class.java)
        nm!!.createNotificationChannel(channel)
    }

    /**
     * 外部apkのClientから送信されたメッセージを受信しそれに応じて処理を行うクラス
     */
    inner class ReceiveFromClientHandler : Handler() {

        override fun handleMessage(msg: Message?) {
            try {
                when (msg?.what) {
                    REQUEST_RESIST -> {
                        Log.e(TAG, "REQUEST_RESIST  received.")
                        if (msg!!.replyTo != null) {
                            sendToClientMessenger = msg!!.replyTo
                            sendToClientMessenger!!.send(Message.obtain(null, RESPONSE_RESIST))
                        }
                    }
                }
            } catch (ex: RemoteException) {
                Log.e(TAG, ex.localizedMessage, ex)
            }
            super.handleMessage(msg)
        }
    }
}
