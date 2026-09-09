package com.example.a2048

import android.content.ClipData.newIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a2048.databinding.ActivityInfoBinding
import androidx.core.net.toUri

class infoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.back.setOnClickListener {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.btnGithub.setOnClickListener {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://github.com/shahzoddev777".toUri()
                )
            )
        }
        binding.btnTelegram.setOnClickListener {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://t.me/Shahzodbek_reyimboyev".toUri()
                )
            )
        }
        binding.btnLinkedIn.setOnClickListener {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://www.linkedin.com/in/shahzodbek-reyimboyev-206a05427?utm_source=share_via&utm_content=profile&utm_medium=member_android".toUri()
                )
            )
        }
    }
}