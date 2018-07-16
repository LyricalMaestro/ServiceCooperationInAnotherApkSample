package com.lyricaloriginal.sampleclientapp

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ClientActivity"

        const val ACTION_RUN_HOST_SERVICE = "com.lyricaloriginal.samplehostapp.RUN"

        const val REQUEST_RESIST = 10
        const val RESPONSE_RESIST = 20

        const val REQUEST_SEND_MSG = 30
    }

    private val serviceConnection = ServiceConnectionImpl()

    private lateinit var receiveFromServiceHandler: ReceiveFromServiceHandler
    private lateinit var receiveFromServiceMessenger: Messenger

    private var sendToServerMessenger: Messenger? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        receiveFromServiceHandler = ReceiveFromServiceHandler()
        receiveFromServiceMessenger = Messenger(receiveFromServiceHandler)
    }

    override fun onResume() {
        super.onResume()

        val intent = Intent().also {
            it.setAction(ACTION_RUN_HOST_SERVICE)
                    .addCategory(Intent.CATEGORY_DEFAULT)
        }

        startService(intent)
        bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE)
    }

    override fun onPause() {
        super.onPause()

        val intent = Intent().also {
            it.setAction(ACTION_RUN_HOST_SERVICE)
                    .addCategory(Intent.CATEGORY_DEFAULT)
        }

        unbindService(serviceConnection)
        stopService(intent)
    }

    private fun appendMessage(msg: String) {
        findViewById<TextView>(R.id.msg_text_view).append(msg + "\n")
    }

    inner class ServiceConnectionImpl : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            sendToServerMessenger = Messenger(binder)

            val message = Message.obtain(null, REQUEST_RESIST).also {
                it.replyTo = receiveFromServiceMessenger
            }
            sendToServerMessenger!!.send(message)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            sendToServerMessenger = null
        }
    }

    inner class ReceiveFromServiceHandler : Handler() {

        override fun handleMessage(msg: Message?) {
            try {
                when (msg?.what) {
                    RESPONSE_RESIST -> {
                        appendMessage("Serviceとの連携準備完了")
                    }
                    REQUEST_SEND_MSG -> {
                        val bundle = msg?.obj as Bundle
                        appendMessage(bundle.getString("msg"))
                    }
                }
            } catch (ex: RemoteException) {
                Log.e(TAG, ex.localizedMessage, ex)
            }
            super.handleMessage(msg)
        }
    }
}
