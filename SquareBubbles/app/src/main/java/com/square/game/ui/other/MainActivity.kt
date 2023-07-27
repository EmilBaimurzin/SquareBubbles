package com.square.game.ui.other

import android.media.MediaPlayer
import android.media.ToneGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.square.game.R
import com.square.game.domain.other.AppSharedPrefs
import com.square.game.domain.other.MusicController

class MainActivity : AppCompatActivity() , MusicController {
    private val viewModel: ActivityViewModel by viewModels()
    private lateinit var mediaPlayer: MediaPlayer
    private val sharedPrefs by lazy {
        AppSharedPrefs(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer.isLooping = true
    }

    override fun onResume() {
        super.onResume()
        startMusic()
    }

    override fun onPause() {
        super.onPause()
        viewModel.currentSec = mediaPlayer.currentPosition
        pauseMusic()
    }

    override fun startMusic() {
        mediaPlayer.setVolume(50.toFloat(), 50.toFloat())
        mediaPlayer.seekTo(viewModel.currentSec)
        mediaPlayer.start()
    }

    override fun pauseMusic() {
        try {
            viewModel.currentSec = mediaPlayer.currentPosition
            mediaPlayer.pause()
        } catch (_: Throwable) {

        }
    }

    override fun saveSec() {
        viewModel.currentSec = mediaPlayer.currentPosition
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}