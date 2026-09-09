package com.example.a2048

import kotlin.random.Random

class GameRepository {
    var matrix = Array(4) { Array(4) { 0 } }
        private set

    init {
        addNewElement()
        addNewElement()
    }

    fun restart() {
        matrix = Array(4) { Array(4) { 0 } }
        score = 0
        addNewElement()
        addNewElement()
    }

    var score = 0

    fun addNewElement() {
        val list = ArrayList<Pair<Int, Int>>()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (matrix[i][j] == 0)
                    list.add(Pair(i, j))
            }
        }

        if (list.isEmpty()) return

        val randomCell = list.random()
        val value = if (Random.nextDouble() < 0.9) 2 else 4

        matrix[randomCell.first][randomCell.second] = value
    }

    private fun checkAndApply(temp: Array<Array<Int>>) {
        var isChanged = false
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (matrix[i][j] != temp[i][j]) {
                    isChanged = true
                    break
                }
            }
        }

        if (isChanged) {
            for (i in 0 until 4) {
                matrix[i] = temp[i].copyOf()
            }
            addNewElement()
        }
    }

    fun moveToLeft() {
        val temp = Array(4) { Array(4) { 0 } }
        var isAdded: Boolean

        for (i in 0 until 4) {
            val ls = ArrayList<Int>()
            isAdded = false

            for (j in 0 until 4) {
                if (matrix[i][j] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[i][j])
                } else {
                    if (ls.last() == matrix[i][j] && !isAdded) {
                        ls[ls.lastIndex] = 2 * ls.last()
                        score += ls[ls.lastIndex]
                        isAdded = true
                    } else {
                        ls.add(matrix[i][j])
                        isAdded = false
                    }
                }
            }

            for (k in 0 until ls.size) {
                temp[i][k] = ls[k]
            }
        }
        checkAndApply(temp)
    }

    fun moveToUp() {
        val temp = Array(4) { Array(4) { 0 } }
        var isAdded: Boolean

        for (i in 0 until 4) {
            val ls = ArrayList<Int>()
            isAdded = false

            for (j in 0 until 4) {
                if (matrix[j][i] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[j][i])
                } else {
                    if (ls.last() == matrix[j][i] && !isAdded) {
                        ls[ls.lastIndex] = 2 * ls.last()
                        score += ls[ls.lastIndex]
                        isAdded = true
                    } else {
                        ls.add(matrix[j][i])
                        isAdded = false
                    }
                }
            }

            for (k in 0 until ls.size) {
                temp[k][i] = ls[k]
            }
        }
        checkAndApply(temp)
    }

    fun moveToRight() {
        val temp = Array(4) { Array(4) { 0 } }
        var isAdded: Boolean

        for (i in 0 until 4) {
            val ls = ArrayList<Int>()
            isAdded = false

            for (j in 3 downTo 0) {
                if (matrix[i][j] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[i][j])
                } else {
                    if (ls.last() == matrix[i][j] && !isAdded) {
                        ls[ls.lastIndex] = 2 * ls.last()
                        score += ls[ls.lastIndex]
                        isAdded = true
                    } else {
                        ls.add(matrix[i][j])
                        isAdded = false
                    }
                }
            }

            for (k in 0 until ls.size) {
                temp[i][3 - k] = ls[k]
            }
        }
        checkAndApply(temp)
    }

    fun moveToDown() {
        val temp = Array(4) { Array(4) { 0 } }
        var isAdded: Boolean

        for (j in 0 until 4) {
            val ls = ArrayList<Int>()
            isAdded = false

            for (i in 3 downTo 0) {
                if (matrix[i][j] == 0) continue
                if (ls.isEmpty()) {
                    ls.add(matrix[i][j])
                } else {
                    if (ls.last() == matrix[i][j] && !isAdded) {
                        ls[ls.lastIndex] = 2 * ls.last()
                        score += ls[ls.lastIndex]
                        isAdded = true
                    } else {
                        ls.add(matrix[i][j])
                        isAdded = false
                    }
                }
            }

            for (k in 0 until ls.size) {
                temp[3 - k][j] = ls[k]
            }
        }
        checkAndApply(temp)
    }

    fun getMatrixAsString(): String {
        val sb = StringBuilder()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                sb.append(matrix[i][j]).append(",")
            }
        }
        return sb.toString().removeSuffix(",")
    }

    fun setMatrixFromString(matrixStr: String) {
        val items = matrixStr.split(",")
        if (items.size != 16) return
        var index = 0
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                matrix[i][j] = items[index].toInt()
                index++
            }
        }
    }

    fun checkMatrix(): Boolean {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (matrix[i][j] == 0) return false
            }
        }

        for (i in 0 until 4) {
            for (j in 0 until 3) {
                if (matrix[i][j] == matrix[i][j + 1]) return false
            }
        }

        for (i in 0 until 3) {
            for (j in 0 until 4) {
                if (matrix[i][j] == matrix[i + 1][j]) return false
            }
        }

        return true
    }
}