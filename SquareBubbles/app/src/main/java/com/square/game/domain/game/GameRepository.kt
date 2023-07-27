package com.square.game.domain.game

import com.square.game.core.library.random
import com.square.game.domain.game.adapter.GameItem

class GameRepository {
    fun generateList(): List<GameItem> {
        val listToReturn = mutableListOf<GameItem>()
        repeat(48) {
            listToReturn.add(GameItem(value = 1 random 6))
        }
        return listToReturn
    }
}