package com.example.a2048

interface MainContract {
    interface View {
        fun showContinueButton(visible: Boolean)
        fun navigateToGame()
        fun navigateToInfo()
        fun exitApp()
        fun showMusicDialog(isMusicOn: Boolean)
        fun updateMusicIcon(isMusicOn: Boolean)
    }

    interface Presenter {
        fun onResume()
        fun onStartNewGameClicked()
        fun onContinueGameClicked()
        fun onInfoClicked()
        fun onExitClicked()
        fun onMusicSettingsClicked()
        fun toggleMusic(isOn: Boolean)
    }
}