package com.lyricaloriginal.samplehostapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

/**
 * ホストアプリ側のActivity。
 * このActivityがなかったら開発環境からホストアプリのインストール+実行ができない。
 */
class SampleHostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_host)
    }
}
