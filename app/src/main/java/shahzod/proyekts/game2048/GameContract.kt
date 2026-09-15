package shahzod.proyekts.game2048

interface GameContract {
    interface View {
        fun showMatrix(matrix: Array<Array<Int>>, score: Int, record: Int)
        fun showToast(message: String)
        fun showGameOverDialog(score: Int)
        fun showCustomDialog(
            title: String,
            message: String,
            positiveText: String,
            negativeText: String,
            onPositive: () -> Unit,
            onNegative: () -> Unit
        )
        fun closeGame()
        fun playSound()
    }

    interface Presenter {
        fun onCreate()
        fun onMove(side: SideEnum)
        fun onRestartClicked()
        fun onUndoClicked()
        fun onBackClicked()
        fun onPause()
        fun onGameOverRestart()
        fun onGameOverMenu()
    }
}