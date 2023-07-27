package com.square.game.ui.endless

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.square.game.core.library.random
import com.square.game.core.library.safe
import com.square.game.domain.game.GameRepository
import com.square.game.domain.game.adapter.GameItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EndlessViewModel : ViewModel() {
    private val repository = GameRepository()
    private var gameScope = CoroutineScope(Dispatchers.Default)
    var vibroCallback: (() -> Unit)? = null

    var gameValue = true
    var pauseValue = false
    private val _list = MutableLiveData(repository.generateList())
    val list: LiveData<List<GameItem>> = _list

    private val _scores = MutableLiveData(0)
    val scores: LiveData<Int> = _scores

    fun selectItem(index: Int) {
        safe {
            val newList = _list.value!!.toMutableList()
            if (newList[index].value != null) {
                newList[index].isSelected = true
                _list.postValue(newList)
            }
        }
    }

    fun stopTimer() {
        gameScope.cancel()
    }

    fun checkSelectedItems() {
        val currentList = _list.value!!.toMutableList()
        val selectedItem = currentList.find { it.isSelected }
        if (selectedItem != null) {
            val allSelectedItems = currentList.filter { it.isSelected }
            val uniqueItemsList = mutableListOf<Int>()
            allSelectedItems.forEach {
                uniqueItemsList.add(it.value!!)
            }
            val uniqueItems = uniqueItemsList.distinct()
            if (uniqueItems.size == 1 && allSelectedItems.size >= 2) {
                checkAllRows()
                vibroCallback?.invoke()
                increaseScores(allSelectedItems.size)
            } else {
                currentList.map { it.isSelected = false }
                _list.postValue(currentList)
            }
        }
    }

    private fun increaseScores(value: Int) {
        _scores.postValue(_scores.value!! + value)
    }

    private fun checkAllRows() {
        val currentList = _list.value!!.toMutableList()
        repeat(6) { index ->
            val listOfRow = mutableListOf<GameItem>()
            if (!currentList[index].isSelected) {
                listOfRow.add(currentList[index])
            }
            if (!currentList[index + 6].isSelected) {
                listOfRow.add(currentList[index + 6])
            }
            if (!currentList[index + 12].isSelected) {
                listOfRow.add(currentList[index + 12])
            }
            if (!currentList[index + 18].isSelected) {
                listOfRow.add(currentList[index + 18])
            }
            if (!currentList[index + 24].isSelected) {
                listOfRow.add(currentList[index + 24])
            }
            if (!currentList[index + 30].isSelected) {
                listOfRow.add(currentList[index + 30])
            }
            if (!currentList[index + 36].isSelected) {
                listOfRow.add(currentList[index + 36])
            }
            if (!currentList[index + 42].isSelected) {
                listOfRow.add(currentList[index + 42])
            }
            listOfRow.reverse()
            repeat(8) {
                val position = when (it) {
                    0 -> index + 42
                    1 -> index + 36
                    2 -> index + 30
                    3 -> index + 24
                    4 -> index + 18
                    5 -> index + 12
                    6 -> index + 6
                    else -> index + 0
                }
                if (listOfRow.size >= it + 1) {
                    currentList[position].value = listOfRow[it].value
                } else {
                    currentList[position].value = 1 random 6
                }
            }
        }
        currentList.map { it.isSelected = false }
        _list.postValue(currentList)
    }

    override fun onCleared() {
        super.onCleared()
        gameScope.cancel()
    }
}