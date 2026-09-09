package com.example.a2048

import android.annotation.SuppressLint
import android.content.Intent
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

class game : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val repository = GameRepository()
    private val list = ArrayList<TextView>()
    private val shared by lazy { getSharedPreferences("GamePrefens", MODE_PRIVATE) }
    private var isWonDialogShown = false

    private var previousMatrix = arrayOf(
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )
    private var previousScore = 0
    private var canUndo = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.restarttxt.setOnClickListener {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
            showCustomDialog(
                "Restart",
                "Rostdan ham o'yinni qayta boshlamoqchimisiz?",
                "Ha",
                "Yo'q",
                onPositive = {
                    repository.restart()
                    isWonDialogShown = false
                    canUndo = false
                    showMatrix()
                    shared.edit().putBoolean("has_saved", false).apply()
                    showToast("Restart")
                },
                onNegative = {}
            )
        }

        binding.back.setOnClickListener {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
            showCustomDialog(
                "Chiqish",
                "Rostdan ham o'yindan chiqmoqchimisiz?",
                "Ha",
                "Yo'q",
                onPositive = {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },
                onNegative = {}
            )
        }
        binding.backtxt.setOnClickListener {
            MediaPlayer.create(this, R.raw.music_click)?.apply {
                start()
                setOnCompletionListener { release() }
            }
            if (canUndo) {
                for (i in 0 until 4) {
                    for (j in 0 until 4) {
                        repository.matrix[i][j] = previousMatrix[i][j]
                    }
                }
                repository.score = previousScore
                canUndo = false
                showMatrix()
                showToast("Orqaga qaytarildi")
            } else {
                showToast("limit tugadi!")
            }
        }

        loadViews()

        val continiobtn = shared.getBoolean("has_saved", false)
        if (continiobtn) {
            val savedMatrix = shared.getString("saved_matrix", "") ?: ""
            val savedScore = shared.getInt("saved_score", 0)
            repository.setMatrixFromString(savedMatrix)
            repository.score = savedScore
        }

        val saveewcord = shared.getInt("record", 0)
        binding.recordtxt.text = saveewcord.toString()

        val myTouchListener = MyTouchListener(this)
        myTouchListener.setMoveSideListener {
            MediaPlayer.create(this,R.raw.music_click).apply {
                start()
                setOnCompletionListener { release() }
            }
            saveCurrentStateBeforeMove()
            when (it) {
                SideEnum.DOWN -> {
                    repository.moveToDown()
                    checkGameStatus()
                }

                SideEnum.UP -> {
                    repository.moveToUp()
                    checkGameStatus()
                }

                SideEnum.RIGHT -> {
                    repository.moveToRight()
                    checkGameStatus()
                }

                SideEnum.LEFT -> {
                    repository.moveToLeft()
                    checkGameStatus()
                }
            }
        }

        binding.container.setOnTouchListener(myTouchListener)
        showMatrix()
    }

    private fun saveCurrentStateBeforeMove() {
        previousScore = repository.score
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                previousMatrix[i][j] = repository.matrix[i][j]
            }
        }
        canUndo = true
    }

    private fun checkGameStatus() {
        showMatrix()

        var has2048 = false
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (repository.matrix[i][j] == 2048) {
                    has2048 = true
                    break
                }
            }
        }

        if (has2048 && !isWonDialogShown) {
            isWonDialogShown = true
            showWinDialog()
            return
        }

        if (repository.checkMatrix()) {
            showGameOverDialog()
        }
    }

    private fun showWinDialog() {
        showCustomDialog(
            "Tabriklaymiz!",
            "Siz 2048 koshinini yig'dingiz va yutdingiz!\nSizning ochkongiz: ${repository.score}",
            "Davom etish",
            "OK",
            onPositive = {
                MediaPlayer.create(this,R.raw.music_click).apply {
                    start()
                    setOnCompletionListener { release() }
                }
                isWonDialogShown = true
            },
            onNegative = {
                MediaPlayer.create(this,R.raw.music_click).apply {
                    start()
                    setOnCompletionListener { release() }
                }
            }
        )
    }

    private fun showGameOverDialog() {
        showCustomDialog(
            "O'yin tugadi!",
            "Afsuski siz yutqazdingiz. Yana urinib ko'rasizmi?",
            "Qayta boshlash",
            "Menyu",
            onPositive = {
                repository.restart()
                isWonDialogShown = false
                canUndo = false
                showMatrix()
                shared.edit().putBoolean("has_saved", false).apply()
            },
            onNegative = {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        )
    }

    private fun showCustomDialog(
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
            MediaPlayer.create(this,R.raw.music_click).apply {
                start()
                setOnCompletionListener { release() }
            }
            onPositive()
            dialog.dismiss()
        }

        dialogBinding.btnNegative.setOnClickListener {
            MediaPlayer.create(this,R.raw.music_click).apply {
                start()
                setOnCompletionListener { release() }
            }
            onNegative()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        val matrixString = repository.getMatrixAsString()
        val currentScore = repository.score

        shared.edit().apply {
            putString("saved_matrix", matrixString)
            putInt("saved_score", currentScore)
            putBoolean("has_saved", true)
            apply()
        }
    }

    private fun loadViews() {
        list.clear()
        for (i in 0 until binding.gameGrid.childCount) {
            val textview = binding.gameGrid.getChildAt(i) as TextView
            list.add(textview)
        }
    }

    private fun showMatrix() {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                list[i * 4 + j].text = if (repository.matrix[i][j] == 0) ""
                else repository.matrix[i][j].toString()

                list[i * 4 + j].setBackgroundResource(BackgroundUtil.getColorByAmount(repository.matrix[i][j]))
            }
        }
        binding.currenttxt.text = repository.score.toString()
        val currentRecord = shared.getInt("record", 0)
        if (repository.score > currentRecord) {
            shared.edit().putInt("record", repository.score).apply()
            binding.recordtxt.text = repository.score.toString()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}