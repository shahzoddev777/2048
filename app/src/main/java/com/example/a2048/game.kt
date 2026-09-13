package com.example.a2048

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a2048.databinding.ActivityGameBinding
import com.example.a2048.databinding.DialogCustomBinding

class game : AppCompatActivity(), GameContract.View {
    private lateinit var binding: ActivityGameBinding
    private lateinit var presenter: GameContract.Presenter
    private lateinit var settings: SettingsManager
    private val list = ArrayList<TextView>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settings = SettingsManager(this)
        presenter = GamePresenter(this, GameRepository(), settings)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadViews()
        setupClickListeners()
        setupTouchListener()
        
        presenter.onCreate()
    }

    private fun setupClickListeners() {
        binding.restarttxt.setOnClickListener {
            presenter.onRestartClicked()
        }
        binding.back.setOnClickListener {
            presenter.onBackClicked()
        }
        binding.backtxt.setOnClickListener {
            presenter.onUndoClicked()
        }
    }

    private fun setupTouchListener() {
        val myTouchListener = MyTouchListener(this)
        myTouchListener.setMoveSideListener {
            presenter.onMove(it)
        }
        binding.container.setOnTouchListener(myTouchListener)
    }

    override fun showMatrix(matrix: Array<Array<Int>>, score: Int, record: Int) {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                list[i * 4 + j].text = if (matrix[i][j] == 0) "" else matrix[i][j].toString()
                list[i * 4 + j].setBackgroundResource(BackgroundUtil.getColorByAmount(matrix[i][j]))
            }
        }
        binding.currenttxt.text = score.toString()
        binding.recordtxt.text = record.toString()
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showGameOverDialog(score: Int) {
        showCustomDialog(
            "O'yin tugadi!",
            "Sizning ochkongiz: $score",
            "Qayta boshlash",
            "Menyu",
            onPositive = {
                presenter.onGameOverRestart()
            },
            onNegative = {
                presenter.onGameOverMenu()
            }
        )
    }

    override fun showCustomDialog(
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
        onPositive: () -> Unit,
        onNegative: () -> Unit
    ) {
        val builder = AlertDialog.Builder(this)
        val dialogBinding = DialogCustomBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBinding.dialogTitle.text = title
        dialogBinding.dialogMessage.text = message
        dialogBinding.btnPositive.text = positiveText
        dialogBinding.btnNegative.text = negativeText

        dialogBinding.btnPositive.setOnClickListener {
            if (settings.isMusicEnabled) playSound()
            onPositive()
            dialog.dismiss()
        }

        dialogBinding.btnNegative.setOnClickListener {
            if (settings.isMusicEnabled) playSound()
            onNegative()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun closeGame() {
        finish()
    }

    override fun playSound() {
        MediaPlayer.create(this, R.raw.music_click)?.apply {
            start()
            setOnCompletionListener { release() }
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    private fun loadViews() {
        list.clear()
        for (i in 0 until binding.gameGrid.childCount) {
            val textview = binding.gameGrid.getChildAt(i) as TextView
            list.add(textview)
        }
    }

    override fun onDestroy() {
        (presenter as? GamePresenter)?.onDestroy()
        super.onDestroy()
    }
}