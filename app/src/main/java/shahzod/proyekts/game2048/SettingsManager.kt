package shahzod.proyekts.game2048

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val shared: SharedPreferences = context.getSharedPreferences("GamePrefens", Context.MODE_PRIVATE)

    var isMusicEnabled: Boolean
        get() = shared.getBoolean("music_enabled", true)
        set(value) = shared.edit().putBoolean("music_enabled", value).apply()

    var hasSavedGame: Boolean
        get() = shared.getBoolean("has_saved", false)
        set(value) = shared.edit().putBoolean("has_saved", value).apply()

    var record: Int
        get() = shared.getInt("record", 0)
        set(value) = shared.edit().putInt("record", value).apply()

    var savedMatrix: String
        get() = shared.getString("saved_matrix", "") ?: ""
        set(value) = shared.edit().putString("saved_matrix", value).apply()

    var savedScore: Int
        get() = shared.getInt("saved_score", 0)
        set(value) = shared.edit().putInt("saved_score", value).apply()
}