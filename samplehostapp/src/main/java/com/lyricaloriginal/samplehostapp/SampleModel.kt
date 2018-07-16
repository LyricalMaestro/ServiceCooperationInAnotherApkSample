package com.lyricaloriginal.samplehostapp

import android.os.Handler
import android.os.HandlerThread

/**
 * SampleServiceで処理を行う具体的な中身を記述する場所
 */
class SampleModel(listener: Listener?) {

    interface Listener {
        fun onProgressNotified(msg: String)
    }

    private val listener: Listener?
    private val uiHandler: Handler
    private val progressNotifyTask: ProgressNotifyTask = ProgressNotifyTask()
    private var workerThread: HandlerThread? = null
    private var workerHandler: Handler? = null

    init {
        uiHandler = Handler()
        this.listener = listener
    }

    fun start() {
        if (workerThread != null) {
            return
        }

        workerThread = HandlerThread("worker")
        workerThread!!.start()
        workerHandler = Handler(workerThread!!.looper)
        workerHandler!!.post(progressNotifyTask)
    }

    fun stop() {
        if (workerThread != null) {
            workerHandler?.removeCallbacks(progressNotifyTask)
            workerThread!!.quitSafely()
            workerThread = null
            workerHandler = null
        }
    }

    inner class ProgressNotifyTask : Runnable {

        private var count: Int = 0

        override fun run() {
            count++
            uiHandler.post {
                listener?.onProgressNotified("HELLO WORLD count = " + count)
            }

            workerHandler?.postDelayed(this, 10 * 1000)
        }
    }
}