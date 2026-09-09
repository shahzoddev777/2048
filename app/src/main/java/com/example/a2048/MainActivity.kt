package com.example.a2048

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a2048.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val shared by lazy { getSharedPreferences("GamePrefens", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.infobtn.setOnClickListener {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
            val intent = Intent(this, infoActivity::class.java)
            startActivity(intent)
        }
        binding.sharebtn.setOnClickListener {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
            finishAffinity()
        }
        binding.continiobtn.setOnClickListener {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
            val intent = Intent(this, game::class.java)
            startActivity(intent)
        }
        binding.startbtn.setOnClickListener {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
            shared.edit().putBoolean("has_saved", false).apply()

            val intent = Intent(this, game::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val hasSavedGame = shared.getBoolean("has_saved", false)

        if (hasSavedGame) {
            binding.continiobtn.visibility = View.VISIBLE
        } else {
            binding.continiobtn.visibility = View.GONE
        }
    }
}