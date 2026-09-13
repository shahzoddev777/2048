package com.example.a2048

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a2048.databinding.ActivityMainBinding
import com.example.a2048.databinding.DialogMusicBinding

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: MainContract.Presenter
    private lateinit var settings: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settings = SettingsManager(this)
        presenter = MainPresenter(this, settings)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.infobtn.setOnClickListener {
            playClickSound()
            presenter.onInfoClicked()
        }
        binding.sharebtn.setOnClickListener {
            playClickSound()
            presenter.onExitClicked()
        }
        binding.continiobtn.setOnClickListener {
            playClickSound()
            presenter.onContinueGameClicked()
        }
        binding.startbtn.setOnClickListener {
            playClickSound()
            presenter.onStartNewGameClicked()
        }
        binding.musicbtn.setOnClickListener {
            playClickSound()
            presenter.onMusicSettingsClicked()
        }
    }

    private fun playClickSound() {
        if (settings.isMusicEnabled) {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun showContinueButton(visible: Boolean) {
        binding.continiobtn.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun navigateToGame() {
        val intent = Intent(this, game::class.java)
        startActivity(intent)
    }

    override fun navigateToInfo() {
        val intent = Intent(this, infoActivity::class.java)
        startActivity(intent)
    }

    override fun exitApp() {
        finishAffinity()
    }

    override fun showMusicDialog(isMusicOn: Boolean) {
        val builder = AlertDialog.Builder(this)
        val dialogBinding = DialogMusicBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // UI feedback for selected state could be added here
        dialogBinding.btnMusicOn.setOnClickListener {
            playClickSound()
            presenter.toggleMusic(true)
            dialog.dismiss()
        }
        dialogBinding.btnMusicOff.setOnClickListener {
            presenter.toggleMusic(false)
            dialog.dismiss()
        }
        dialogBinding.btnClose.setOnClickListener {
            playClickSound()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun updateMusicIcon(isMusicOn: Boolean) {
    }

    override fun onDestroy() {
        (presenter as? MainPresenter)?.onDestroy()
        super.onDestroy()
    }
}