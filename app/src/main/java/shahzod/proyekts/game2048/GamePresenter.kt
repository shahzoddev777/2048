package shahzod.proyekts.game2048

class GamePresenter(
    private var view: GameContract.View?,
    private val repository: GameRepository,
    private val settings: SettingsManager
) : GameContract.Presenter {

    private var previousMatrix = Array(4) { Array(4) { 0 } }
    private var previousScore = 0
    private var canUndo = false
    private var isWonDialogShown = false

    override fun onCreate() {
        if (settings.hasSavedGame) {
            repository.setMatrixFromString(settings.savedMatrix)
            repository.score = settings.savedScore
        }
        updateUI()
    }

    override fun onMove(side: SideEnum) {
        playSound()
        saveState()
        when (side) {
            SideEnum.DOWN -> repository.moveToDown()
            SideEnum.UP -> repository.moveToUp()
            SideEnum.RIGHT -> repository.moveToRight()
            SideEnum.LEFT -> repository.moveToLeft()
        }
        checkStatus()
    }

    override fun onRestartClicked() {
        playSound()
        view?.showCustomDialog(
            "Restart",
            "Rostdan ham o'yinni qayta boshlamoqchimisiz?",
            "Ha",
            "Yo'q",
            onPositive = {
                repository.restart()
                isWonDialogShown = false
                canUndo = false
                settings.hasSavedGame = false
                updateUI()
                view?.showToast("Restart")
            },
            onNegative = {}
        )
    }

    override fun onUndoClicked() {
        playSound()
        if (canUndo) {
            for (i in 0 until 4) {
                for (j in 0 until 4) {
                    repository.matrix[i][j] = previousMatrix[i][j]
                }
            }
            repository.score = previousScore
            canUndo = false
            updateUI()
            view?.showToast("Orqaga qaytarildi")
        } else {
            view?.showToast("limit tugadi!")
        }
    }

    override fun onBackClicked() {
        playSound()
        view?.showCustomDialog(
            "Chiqish",
            "Rostdan ham o'yindan chiqmoqchimisiz?",
            "Ha",
            "Yo'q",
            onPositive = {
                view?.closeGame()
            },
            onNegative = {}
        )
    }

    override fun onPause() {
        settings.savedMatrix = repository.getMatrixAsString()
        settings.savedScore = repository.score
        settings.hasSavedGame = true
    }

    override fun onGameOverRestart() {
        repository.restart()
        isWonDialogShown = false
        canUndo = false
        settings.hasSavedGame = false
        updateUI()
    }

    override fun onGameOverMenu() {
        view?.closeGame()
    }

    private fun checkStatus() {
        updateUI()
        
        // Win check
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
            // We could show win dialog here if needed
        }

        // Game over check
        if (repository.checkMatrix()) {
            view?.showGameOverDialog(repository.score)
        }
    }

    private fun updateUI() {
        if (repository.score > settings.record) {
            settings.record = repository.score
        }
        view?.showMatrix(repository.matrix, repository.score, settings.record)
    }

    private fun saveState() {
        previousScore = repository.score
        for (i in 0 until 4) {
            previousMatrix[i] = repository.matrix[i].copyOf()
        }
        canUndo = true
    }

    private fun playSound() {
        if (settings.isMusicEnabled) {
            view?.playSound()
        }
    }

    fun onDestroy() {
        view = null
    }
}