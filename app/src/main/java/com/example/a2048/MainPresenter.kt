package com.example.a2048

import android.content.SharedPreferences

class MainPresenter(
    private var view: MainContract.View?,
    private val settings: SettingsManager
) : MainContract.Presenter {

    override fun onResume() {
        view?.showContinueButton(settings.hasSavedGame)
        view?.updateMusicIcon(settings.isMusicEnabled)
    }

    override fun onStartNewGameClicked() {
        settings.hasSavedGame = false
        view?.navigateToGame()
    }

    override fun onContinueGameClicked() {
        view?.navigateToGame()
    }

    override fun onInfoClicked() {
        view?.navigateToInfo()
    }

    override fun onExitClicked() {
        view?.exitApp()
    }

    override fun onMusicSettingsClicked() {
        view?.showMusicDialog(settings.isMusicEnabled)
    }

    override fun toggleMusic(isOn: Boolean) {
        settings.isMusicEnabled = isOn
        view?.updateMusicIcon(isOn)
    }

    fun onDestroy() {
        view = null
    }
}