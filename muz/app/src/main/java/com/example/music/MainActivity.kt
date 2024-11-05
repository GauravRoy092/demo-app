package com.example.music

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the MediaPlayer with a music file from the res/raw directory
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer.isLooping = true // Loop the music if desired
        mediaPlayer.setVolume(0.5f, 0.5f) // Set initial volume
        val totalTime = mediaPlayer.duration

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Reference to the play/pause button
        val playPauseButton = findViewById<ImageView>(R.id.playPauseButton)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        // SeekBar setup
        seekBar.max = totalTime
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Update SeekBar position as the music plays
        Thread {
            while (true) {
                try {
                    if (mediaPlayer.isPlaying) {
                        seekBar.progress = mediaPlayer.currentPosition
                    }
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()

        // Click listener to toggle play and pause
        playPauseButton.setOnClickListener {
            isPlaying = !isPlaying
            if (isPlaying) {
                playPauseButton.setImageResource(R.drawable.pause_stop_svgrepo_com)
                mediaPlayer.start() // Start playing music
            } else {
                playPauseButton.setImageResource(R.drawable.music_play_player_svgrepo_com)
                mediaPlayer.pause() // Pause the music
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release() // Release the media player resources
    }
}
