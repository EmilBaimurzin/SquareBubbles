package com.square.game.domain.other

import android.content.Context

class AppSharedPrefs(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)

    fun setVolume() {
        sharedPreferences.edit().putBoolean("VOLUME", !getVolumeState()).apply()
    }

    fun getVolumeState(): Boolean = sharedPreferences.getBoolean("VOLUME", true)

    fun setVibro(value: Boolean) {
        sharedPreferences.edit().putBoolean("VIBRO", value).apply()
    }

    fun getVibro(): Boolean = sharedPreferences.getBoolean("VIBRO", true)

    fun getBestScores(game: Game): Int {
        return when (game) {
            Game.TIMED -> sharedPreferences.getInt("TIMED_SCORES", 0)
            Game.MOVES -> sharedPreferences.getInt("MOVES_SCORES", 0)
            Game.ENDLESS -> sharedPreferences.getInt("ENDLESS_SCORES", 0)
        }
    }

    fun setBestScores(value: Int, game: Game) {
        when (game) {
            Game.TIMED -> sharedPreferences.edit().putInt("TIMED_SCORES", value).apply()
            Game.MOVES -> sharedPreferences.edit().putInt("MOVES_SCORES", value).apply()
            Game.ENDLESS -> sharedPreferences.edit().putInt("ENDLESS_SCORES", value).apply()
        }
    }

}