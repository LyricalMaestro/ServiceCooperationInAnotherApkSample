package com.lyricaloriginal.samplehostapp

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

/**
 * 外部apkとやり取りをするためのService.
 */
class SampleService : Service(), SampleModel.Listener {

    companion object {
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
